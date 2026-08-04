package com.ap_automation.controller;

import com.ap_automation.dto.request.PurchaseOrderRequest;
import com.ap_automation.dto.response.PurchaseOrderResponse;
import com.ap_automation.service.PurchaseOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchase-orders")
@RequiredArgsConstructor
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PurchaseOrderResponse create(
            @Valid @RequestBody PurchaseOrderRequest request) {

        return purchaseOrderService.create(request);

    }

    @GetMapping("/{id}")
    public PurchaseOrderResponse getById(
            @PathVariable Long id) {

        return purchaseOrderService.getById(id);

    }

    @GetMapping("/number/{poNumber}")
    public PurchaseOrderResponse getByPoNumber(
            @PathVariable String poNumber) {

        return purchaseOrderService.getByPoNumber(poNumber);

    }

    @GetMapping
    public List<PurchaseOrderResponse> getAll() {

        return purchaseOrderService.getAll();

    }

    @PutMapping("/{id}")
    public PurchaseOrderResponse update(
            @PathVariable Long id,
            @Valid @RequestBody PurchaseOrderRequest request) {

        return purchaseOrderService.update(id, request);

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long id) {

        purchaseOrderService.delete(id);

    }

}