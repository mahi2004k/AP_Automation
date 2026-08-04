package com.ap_automation.controller;

import com.ap_automation.dto.request.ApprovalRequest;
import com.ap_automation.dto.response.ApprovalResponse;
import com.ap_automation.dto.response.InvoiceDetailResponse;
import com.ap_automation.service.ApprovalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/approvals")
@RequiredArgsConstructor
public class ApprovalController {

    private final ApprovalService approvalService;

    // ==========================
    // Pending Approvals (invoices that passed 3-way match)
    // ==========================

    @GetMapping("/pending")
    public ResponseEntity<List<InvoiceDetailResponse>> getPendingApprovals() {

        return ResponseEntity.ok(
                approvalService.getPendingApprovals()
        );
    }

    // ==========================
    // Approve Invoice
    // ==========================

    @PostMapping("/{invoiceId}/approve")
    public ResponseEntity<ApprovalResponse> approveInvoice(
            @PathVariable Long invoiceId,
            @Valid @RequestBody ApprovalRequest request) {

        return ResponseEntity.ok(
                approvalService.approve(invoiceId, request)
        );
    }

    // ==========================
    // Reject Invoice
    // ==========================

    @PostMapping("/{invoiceId}/reject")
    public ResponseEntity<ApprovalResponse> rejectInvoice(
            @PathVariable Long invoiceId,
            @Valid @RequestBody ApprovalRequest request) {

        return ResponseEntity.ok(
                approvalService.reject(invoiceId, request)
        );
    }

}