package com.ap_automation.service;

import com.ap_automation.dto.response.AuditLogResponse;

import java.util.List;

public interface AuditLogService {

    void log(String action,
             String entityType,
             Long entityId,
             String username,
             String details);

    List<AuditLogResponse> getInvoiceHistory(Long invoiceId);

}