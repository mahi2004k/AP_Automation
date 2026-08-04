package com.ap_automation.controller;

import com.ap_automation.dto.request.PaymentRequest;
import com.ap_automation.dto.response.PaymentResponse;
import com.ap_automation.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    // ==========================
    // Make Payment
    // ==========================

    @PostMapping("/{invoiceId}")
    public ResponseEntity<PaymentResponse> makePayment(
            @PathVariable Long invoiceId,
            @Valid @RequestBody PaymentRequest request) {

        return ResponseEntity.ok(
                paymentService.makePayment(invoiceId, request)
        );
    }

    // ==========================
    // Get Payment By Id
    // ==========================

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponse> getById(
            @PathVariable Long paymentId) {

        return ResponseEntity.ok(
                paymentService.getById(paymentId)
        );
    }

    // ==========================
    // Get Payment By Invoice
    // ==========================

    @GetMapping("/invoice/{invoiceId}")
    public ResponseEntity<PaymentResponse> getByInvoice(
            @PathVariable Long invoiceId) {

        return ResponseEntity.ok(
                paymentService.getByInvoice(invoiceId)
        );
    }

    // ==========================
    // Get All Payments
    // ==========================

    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getAll() {

        return ResponseEntity.ok(
                paymentService.getAll()
        );
    }

}