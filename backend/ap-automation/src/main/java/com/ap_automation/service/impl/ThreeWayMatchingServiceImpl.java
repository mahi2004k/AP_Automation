package com.ap_automation.service.impl;

import com.ap_automation.dto.response.MatchResultResponse;
import com.ap_automation.entity.*;
import com.ap_automation.enums.InvoiceStatus;
import com.ap_automation.enums.MatchStatus;
import com.ap_automation.repository.InvoiceRepository;
import com.ap_automation.repository.MatchResultRepository;
import com.ap_automation.repository.PurchaseOrderRepository;
import com.ap_automation.repository.ReceivingReportRepository;
import com.ap_automation.service.AuditLogService;
import com.ap_automation.service.ThreeWayMatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ThreeWayMatchingServiceImpl implements ThreeWayMatchingService {

    private final InvoiceRepository invoiceRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final ReceivingReportRepository receivingReportRepository;
    private final MatchResultRepository matchResultRepository;
    private final AuditLogService auditLogService;

    @Override
    public MatchResultResponse match(Long invoiceId) {

        // ==============================
        // 1. Load Invoice
        // ==============================

        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() ->
                        new RuntimeException("Invoice not found"));

        // ==============================
        // 2. Load Purchase Order
        // ==============================

        PurchaseOrder purchaseOrder =
                purchaseOrderRepository.findByPoNumber(invoice.getPoNumber())
                        .orElseThrow(() ->
                                new RuntimeException("Purchase Order not found"));

        // ==============================
        // 3. Load Receiving Report
        // ==============================

        ReceivingReport receivingReport =
                receivingReportRepository
                        .findByPurchaseOrderId(purchaseOrder.getId())
                        .orElseThrow(() ->
                                new RuntimeException("Receiving Report not found"));

        // ==============================
        // 4. Vendor & PO Comparison
        // ==============================

        boolean vendorMatched =
                invoice.getVendorName()
                        .equalsIgnoreCase(purchaseOrder.getVendorName());

        boolean poMatched =
                invoice.getPoNumber()
                        .equalsIgnoreCase(purchaseOrder.getPoNumber());

        // ==============================
        // 5. Convert Lists into Maps
        // ==============================

        Map<String, PurchaseOrderItem> poItems =
                purchaseOrder.getItems()
                        .stream()
                        .collect(Collectors.toMap(
                                item -> item.getDescription().toLowerCase(),
                                Function.identity()
                        ));

        Map<String, ReceivingReportItem> rrItems =
                receivingReport.getItems()
                        .stream()
                        .collect(Collectors.toMap(
                                item -> item.getDescription().toLowerCase(),
                                Function.identity()
                        ));

        boolean itemMatched = true;
        boolean quantityMatched = true;
        boolean priceMatched = true;
        boolean totalMatched = true;

        StringBuilder remarks = new StringBuilder();

        // ==============================
        // 6. Compare Every Invoice Item
        // ==============================

        for (InvoiceItem invoiceItem : invoice.getInvoiceItems()) {

            PurchaseOrderItem poItem =
                    poItems.get(invoiceItem.getDescription().toLowerCase());

            if (poItem == null) {

                itemMatched = false;

                remarks.append("Item ")
                        .append(invoiceItem.getDescription())
                        .append(" not found in Purchase Order.\n");

                continue;
            }

            ReceivingReportItem rrItem =
                    rrItems.get(invoiceItem.getDescription().toLowerCase());

            if (rrItem == null) {

                quantityMatched = false;

                remarks.append("Item ")
                        .append(invoiceItem.getDescription())
                        .append(" not received.\n");

                continue;
            }

            // Quantity

            if (invoiceItem.getQuantity()
                    .compareTo(rrItem.getQuantityReceived()) != 0) {

                quantityMatched = false;

                remarks.append(invoiceItem.getDescription())
                        .append(" quantity mismatch. ")
                        .append("Received ")
                        .append(rrItem.getQuantityReceived())
                        .append(", Invoice ")
                        .append(invoiceItem.getQuantity())
                        .append("\n");
            }

            // Price

            if (invoiceItem.getUnitPrice()
                    .compareTo(poItem.getUnitPrice()) != 0) {

                priceMatched = false;

                remarks.append(invoiceItem.getDescription())
                        .append(" price mismatch. ")
                        .append("PO ")
                        .append(poItem.getUnitPrice())
                        .append(", Invoice ")
                        .append(invoiceItem.getUnitPrice())
                        .append("\n");
            }

        }

        // ==============================
        // 7. Compare Total Amount
        // ==============================

        BigDecimal calculatedTotal =
                purchaseOrder.getItems()
                        .stream()
                        .map(item ->
                                item.getQuantity()
                                        .multiply(item.getUnitPrice()))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (invoice.getTotalAmount()
                .compareTo(calculatedTotal) != 0) {

            totalMatched = false;

            remarks.append("Invoice total mismatch.\n");
        }

        // ==============================
        // 8. Decide Final Status
        // ==============================

        MatchStatus status;

        if (vendorMatched
                && poMatched
                && itemMatched
                && quantityMatched
                && priceMatched
                && totalMatched) {

            status = MatchStatus.MATCHED;

        } else {

            status = MatchStatus.NEEDS_REVIEW;
        }

        // ==============================
        // 9. Save Match Result
        // ==============================

        MatchResult result = matchResultRepository
                .findByInvoiceId(invoice.getId())
                .orElse(new MatchResult());

        result.setInvoice(invoice);
        result.setVendorMatched(vendorMatched);
        result.setPoMatched(poMatched);
        result.setItemMatched(itemMatched);
        result.setQuantityMatched(quantityMatched);
        result.setPriceMatched(priceMatched);
        result.setTotalMatched(totalMatched);
        result.setRemarks(remarks.toString());
        result.setStatus(status);


        auditLogService.log(
                "MATCH",
                "INVOICE",
                invoice.getId(),
                "SYSTEM",
                status == MatchStatus.MATCHED
                        ? "Three-way matching successful."
                        : remarks.toString()
        );

        matchResultRepository.save(result);

        // ==============================
        // 10. Update Invoice Status
        // ==============================

        if (status == MatchStatus.MATCHED) {
            invoice.setStatus(InvoiceStatus.MATCHED);
        } else {
            invoice.setStatus(InvoiceStatus.NEEDS_REVIEW);
        }

        invoiceRepository.save(invoice);

        // ==============================
        // 11. Return Response
        // ==============================

        return MatchResultResponse.builder()
                .invoiceId(invoice.getId())
                .vendorMatched(vendorMatched)
                .poMatched(poMatched)
                .itemMatched(itemMatched)
                .quantityMatched(quantityMatched)
                .priceMatched(priceMatched)
                .totalMatched(totalMatched)
                .remarks(remarks.toString())
                .status(status)
                .build();
    }
}