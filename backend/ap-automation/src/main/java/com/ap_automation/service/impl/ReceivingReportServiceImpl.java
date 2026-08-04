package com.ap_automation.service.impl;

import com.ap_automation.dto.request.ReceivingReportItemRequest;
import com.ap_automation.dto.request.ReceivingReportRequest;
import com.ap_automation.dto.response.ReceivingReportItemResponse;
import com.ap_automation.dto.response.ReceivingReportResponse;
import com.ap_automation.entity.PurchaseOrder;
import com.ap_automation.entity.ReceivingReport;
import com.ap_automation.entity.ReceivingReportItem;
import com.ap_automation.exception.PurchaseOrderNotFoundException;
import com.ap_automation.exception.ReceivingReportAlreadyExistsException;
import com.ap_automation.exception.ReceivingReportNotFoundException;
import com.ap_automation.repository.PurchaseOrderRepository;
import com.ap_automation.repository.ReceivingReportRepository;
import com.ap_automation.service.ReceivingReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReceivingReportServiceImpl implements ReceivingReportService {

    private final ReceivingReportRepository receivingReportRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;

    @Override
    public ReceivingReportResponse create(ReceivingReportRequest request) {

        receivingReportRepository.findByReportNumber(request.getReportNumber())
                .ifPresent(report -> {
                    throw new ReceivingReportAlreadyExistsException(
                            "Receiving Report already exists."
                    );
                });

        validateItems(request);

        PurchaseOrder purchaseOrder =
                purchaseOrderRepository.findById(request.getPurchaseOrderId())
                        .orElseThrow(() ->
                                new PurchaseOrderNotFoundException(
                                        "Purchase Order not found."
                                ));

        ReceivingReport report = new ReceivingReport();

        report.setReportNumber(request.getReportNumber());
        report.setReceivedDate(request.getReceivedDate());
        report.setPurchaseOrder(purchaseOrder);

        addItems(report, request.getItems());

        ReceivingReport saved = receivingReportRepository.save(report);

        return map(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ReceivingReportResponse getById(Long id) {

        ReceivingReport report =
                receivingReportRepository.findById(id)
                        .orElseThrow(() ->
                                new ReceivingReportNotFoundException(
                                        "Receiving Report not found."
                                ));

        return map(report);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReceivingReportResponse> getAll() {

        return receivingReportRepository.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public ReceivingReportResponse update(Long id,
                                          ReceivingReportRequest request) {

        ReceivingReport report =
                receivingReportRepository.findById(id)
                        .orElseThrow(() ->
                                new ReceivingReportNotFoundException(
                                        "Receiving Report not found."
                                ));

        receivingReportRepository.findByReportNumber(request.getReportNumber())
                .ifPresent(existing -> {
                    if (!existing.getId().equals(id)) {
                        throw new ReceivingReportAlreadyExistsException(
                                "Receiving Report already exists."
                        );
                    }
                });

        validateItems(request);

        PurchaseOrder purchaseOrder =
                purchaseOrderRepository.findById(request.getPurchaseOrderId())
                        .orElseThrow(() ->
                                new PurchaseOrderNotFoundException(
                                        "Purchase Order not found."
                                ));

        report.setReportNumber(request.getReportNumber());
        report.setReceivedDate(request.getReceivedDate());
        report.setPurchaseOrder(purchaseOrder);

        report.getItems().clear();

        addItems(report, request.getItems());

        ReceivingReport updated =
                receivingReportRepository.save(report);

        return map(updated);
    }

    @Override
    public void delete(Long id) {

        ReceivingReport report =
                receivingReportRepository.findById(id)
                        .orElseThrow(() ->
                                new ReceivingReportNotFoundException(
                                        "Receiving Report not found."
                                ));

        receivingReportRepository.delete(report);
    }

    /**
     * Validate that at least one item exists.
     */
    private void validateItems(ReceivingReportRequest request) {

        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new IllegalArgumentException(
                    "Receiving Report must contain at least one item."
            );
        }
    }

    /**
     * Convert DTO items into Entity items.
     */
    private void addItems(ReceivingReport report,
                          List<ReceivingReportItemRequest> items) {

        for (ReceivingReportItemRequest dto : items) {

            ReceivingReportItem item = new ReceivingReportItem();

            item.setDescription(dto.getDescription());
            item.setQuantityReceived(dto.getQuantityReceived());
            item.setReceivingReport(report);

            report.getItems().add(item);
        }
    }

    /**
     * Entity -> Response DTO
     */
    private ReceivingReportResponse map(ReceivingReport report) {

        return ReceivingReportResponse.builder()
                .id(report.getId())
                .reportNumber(report.getReportNumber())
                .receivedDate(report.getReceivedDate())
                .purchaseOrderId(report.getPurchaseOrder().getId())
                .items(
                        report.getItems()
                                .stream()
                                .map(item ->
                                        ReceivingReportItemResponse.builder()
                                                .id(item.getId())
                                                .description(item.getDescription())
                                                .quantityReceived(item.getQuantityReceived())
                                                .build()
                                )
                                .toList()
                )
                .build();
    }
}