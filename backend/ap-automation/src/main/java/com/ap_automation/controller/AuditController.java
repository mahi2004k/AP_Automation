package com.ap_automation.controller;

import com.ap_automation.dto.response.AuditLogResponse;
import com.ap_automation.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audit")
@RequiredArgsConstructor
public class AuditController {

    private final AuditLogService auditLogService;

    // =====================================
    // Get Invoice Audit History
    // =====================================

    @GetMapping("/invoice/{invoiceId}")
    public ResponseEntity<List<AuditLogResponse>> getInvoiceHistory(
            @PathVariable Long invoiceId) {

        return ResponseEntity.ok(
                auditLogService.getInvoiceHistory(invoiceId)
        );

    }

}