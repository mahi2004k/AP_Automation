package com.ap_automation.util;

import com.ap_automation.dto.response.InvoiceDetailResponse;
import com.ap_automation.dto.response.LineItemResponse;
import com.ap_automation.entity.Invoice;

import java.util.List;

public class InvoiceMapper {

    private InvoiceMapper() {}

    public static InvoiceDetailResponse toDetailResponse(Invoice invoice) {

        List<LineItemResponse> items = invoice.getInvoiceItems()
                .stream()
                .map(item -> LineItemResponse.builder()
                        .description(item.getDescription())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .amount(item.getAmount())
                        .build())
                .toList();

        return InvoiceDetailResponse.builder()
                .id(invoice.getId())
                .invoiceNumber(invoice.getInvoiceNumber())
                .poNumber(invoice.getPoNumber())
                .invoiceDate(invoice.getInvoiceDate())
                .dueDate(invoice.getDueDate())
                .vendorName(invoice.getVendorName())
                .vendorEmail(invoice.getVendorEmail())
                .vendorGstNumber(invoice.getVendorGstNumber())
                .currency(invoice.getCurrency())
                .subtotal(invoice.getSubtotal())
                .taxAmount(invoice.getTaxAmount())
                .totalAmount(invoice.getTotalAmount())
                .status(invoice.getStatus())
                .paymentStatus(invoice.getPaymentStatus())
                .extractionConfidence(invoice.getExtractionConfidence())
                .hasFile(invoice.getPdfUrl() != null && !invoice.getPdfUrl().isBlank())
                .uploadedBy(invoice.getUploadedBy() != null ? invoice.getUploadedBy().getEmail() : null)
                .approvedBy(invoice.getApprovedBy() != null ? invoice.getApprovedBy().getEmail() : null)
                .approvedAt(invoice.getApprovedAt())
                .approvalRemarks(invoice.getApprovalRemarks())
                .createdAt(invoice.getCreatedAt())
                .items(items)
                .build();
    }
}
