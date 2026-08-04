package com.ap_automation.service.impl;

import com.ap_automation.dto.response.AuditLogResponse;
import com.ap_automation.entity.AuditLog;
import com.ap_automation.repository.AuditLogRepository;
import com.ap_automation.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;

    @Override
    public void log(
            String action,
            String entityType,
            Long entityId,
            String username,
            String details) {

        System.out.println("========= AUDIT LOG METHOD CALLED =========");

        AuditLog auditLog = AuditLog.builder()
                .action(action)
                .entityType(entityType)
                .entityId(entityId)
                .username(username)
                .details(details)
                .build();

        auditLogRepository.save(auditLog);

        System.out.println("========= AUDIT LOG SAVED =========");

    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLogResponse> getInvoiceHistory(Long invoiceId) {

        return auditLogRepository
                .findByEntityTypeAndEntityIdOrderByCreatedAtAsc(
                        "INVOICE",
                        invoiceId
                )
                .stream()
                .map(this::map)
                .toList();

    }

    private AuditLogResponse map(AuditLog auditLog) {

        return AuditLogResponse.builder()
                .id(auditLog.getId())
                .action(auditLog.getAction())
                .entityType(auditLog.getEntityType())
                .entityId(auditLog.getEntityId())
                .username(auditLog.getUsername())
                .details(auditLog.getDetails())
                .createdAt(auditLog.getCreatedAt())
                .build();



    }

}