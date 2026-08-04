package com.ap_automation.service.impl;

import com.ap_automation.dto.request.PurchaseOrderItemRequest;
import com.ap_automation.dto.request.PurchaseOrderRequest;
import com.ap_automation.dto.response.PurchaseOrderItemResponse;
import com.ap_automation.dto.response.PurchaseOrderResponse;
import com.ap_automation.entity.PurchaseOrder;
import com.ap_automation.entity.PurchaseOrderItem;
import com.ap_automation.enums.PurchaseOrderStatus;
import com.ap_automation.exception.PurchaseOrderAlreadyExistsException;
import com.ap_automation.exception.PurchaseOrderNotFoundException;
import com.ap_automation.repository.PurchaseOrderRepository;
import com.ap_automation.service.PurchaseOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;

    @Override
    public PurchaseOrderResponse create(PurchaseOrderRequest request) {

        purchaseOrderRepository.findByPoNumber(request.getPoNumber())
                .ifPresent(po -> {
                    throw new PurchaseOrderAlreadyExistsException(
                            "Purchase Order already exists."
                    );
                });

        PurchaseOrder purchaseOrder = new PurchaseOrder();

        purchaseOrder.setPoNumber(request.getPoNumber());
        purchaseOrder.setVendorName(request.getVendorName());
        purchaseOrder.setStatus(PurchaseOrderStatus.OPEN);

        for (PurchaseOrderItemRequest dto : request.getItems()) {

            PurchaseOrderItem item = new PurchaseOrderItem();

            item.setDescription(dto.getDescription());
            item.setQuantity(dto.getQuantity());
            item.setUnitPrice(dto.getUnitPrice());

            item.setPurchaseOrder(purchaseOrder);

            purchaseOrder.getItems().add(item);

        }

        PurchaseOrder saved = purchaseOrderRepository.save(purchaseOrder);

        return mapToResponse(saved);

    }

    @Override
    @Transactional(readOnly = true)
    public PurchaseOrderResponse getById(Long id) {

        PurchaseOrder purchaseOrder =
                purchaseOrderRepository.findById(id)
                        .orElseThrow(() ->
                                new PurchaseOrderNotFoundException(
                                        "Purchase Order not found."
                                ));

        return mapToResponse(purchaseOrder);

    }

    @Override
    @Transactional(readOnly = true)
    public PurchaseOrderResponse getByPoNumber(String poNumber) {

        PurchaseOrder purchaseOrder =
                purchaseOrderRepository.findByPoNumber(poNumber)
                        .orElseThrow(() ->
                                new PurchaseOrderNotFoundException(
                                        "Purchase Order not found."
                                ));

        return mapToResponse(purchaseOrder);

    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrderResponse> getAll() {

        return purchaseOrderRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();

    }

    @Override
    public PurchaseOrderResponse update(Long id,
                                        PurchaseOrderRequest request) {

        PurchaseOrder purchaseOrder =
                purchaseOrderRepository.findById(id)
                        .orElseThrow(() ->
                                new PurchaseOrderNotFoundException(
                                        "Purchase Order not found."
                                ));

        purchaseOrder.setPoNumber(request.getPoNumber());
        purchaseOrder.setVendorName(request.getVendorName());

        purchaseOrder.getItems().clear();

        for (PurchaseOrderItemRequest dto : request.getItems()) {

            PurchaseOrderItem item = new PurchaseOrderItem();

            item.setDescription(dto.getDescription());
            item.setQuantity(dto.getQuantity());
            item.setUnitPrice(dto.getUnitPrice());

            item.setPurchaseOrder(purchaseOrder);

            purchaseOrder.getItems().add(item);

        }

        PurchaseOrder updated =
                purchaseOrderRepository.save(purchaseOrder);

        return mapToResponse(updated);

    }

    @Override
    public void delete(Long id) {

        PurchaseOrder purchaseOrder =
                purchaseOrderRepository.findById(id)
                        .orElseThrow(() ->
                                new PurchaseOrderNotFoundException(
                                        "Purchase Order not found."
                                ));

        purchaseOrderRepository.delete(purchaseOrder);

    }

    private PurchaseOrderResponse mapToResponse(PurchaseOrder purchaseOrder) {

        return PurchaseOrderResponse.builder()
                .id(purchaseOrder.getId())
                .poNumber(purchaseOrder.getPoNumber())
                .vendorName(purchaseOrder.getVendorName())
                .status(purchaseOrder.getStatus())
                .items(
                        purchaseOrder.getItems()
                                .stream()
                                .map(item ->
                                        PurchaseOrderItemResponse.builder()
                                                .id(item.getId())
                                                .description(item.getDescription())
                                                .quantity(item.getQuantity())
                                                .unitPrice(item.getUnitPrice())
                                                .build()
                                )
                                .toList()
                )
                .build();

    }

}