package com.ap_automation.service;

import com.ap_automation.dto.request.PurchaseOrderRequest;
import com.ap_automation.dto.response.PurchaseOrderResponse;

import java.util.List;

public interface PurchaseOrderService {

    PurchaseOrderResponse create(PurchaseOrderRequest request);

    PurchaseOrderResponse getById(Long id);

    PurchaseOrderResponse getByPoNumber(String poNumber);

    List<PurchaseOrderResponse> getAll();

    PurchaseOrderResponse update(Long id, PurchaseOrderRequest request);

    void delete(Long id);

}