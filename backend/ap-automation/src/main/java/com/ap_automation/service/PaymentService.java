package com.ap_automation.service;

import com.ap_automation.dto.request.PaymentRequest;
import com.ap_automation.dto.response.PaymentResponse;

import java.util.List;

public interface PaymentService {

    PaymentResponse makePayment(Long invoiceId,
                                PaymentRequest request);

    PaymentResponse getById(Long id);

    PaymentResponse getByInvoice(Long invoiceId);

    List<PaymentResponse> getAll();

}