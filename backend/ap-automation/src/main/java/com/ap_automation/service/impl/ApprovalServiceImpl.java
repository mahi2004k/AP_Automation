package com.ap_automation.service.impl;

import com.ap_automation.dto.request.ApprovalRequest;
import com.ap_automation.dto.response.ApprovalResponse;
import com.ap_automation.dto.response.InvoiceDetailResponse;
import com.ap_automation.entity.Invoice;
import com.ap_automation.entity.User;
import com.ap_automation.enums.InvoiceStatus;
import com.ap_automation.exception.InvoiceNotFoundException;
import com.ap_automation.repository.InvoiceRepository;
import com.ap_automation.repository.UserRepository;
import com.ap_automation.service.ApprovalService;
import com.ap_automation.service.AuditLogService;
import com.ap_automation.service.EmailService;
import com.ap_automation.util.InvoiceMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ApprovalServiceImpl implements ApprovalService {

    private static final Logger log = LoggerFactory.getLogger(ApprovalServiceImpl.class);

    private final InvoiceRepository invoiceRepository;
    private final UserRepository userRepository;
    private final AuditLogService auditLogService;
    private final EmailService emailService;

    @Override
    public ApprovalResponse approve(Long invoiceId,
                                    ApprovalRequest request) {

        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() ->
                        new InvoiceNotFoundException("Invoice not found."));

        // Allow only matched invoices
        if (invoice.getStatus() != InvoiceStatus.MATCHED) {
            throw new IllegalStateException(
                    "Only MATCHED invoices can be approved."
            );
        }

        User currentUser = getCurrentUser();

        invoice.setStatus(InvoiceStatus.APPROVED);
        invoice.setApprovedBy(currentUser);
        invoice.setApprovedAt(LocalDateTime.now());
        invoice.setApprovalRemarks(request.getRemarks());

        Invoice saved = invoiceRepository.save(invoice);

        auditLogService.log(
                "APPROVED",
                "INVOICE",
                saved.getId(),
                currentUser.getEmail(),
                request.getRemarks()
        );

        notifyVendor(saved, true, null);

        return map(saved);
    }

    @Override
    public ApprovalResponse reject(Long invoiceId,
                                   ApprovalRequest request) {

        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() ->
                        new InvoiceNotFoundException("Invoice not found."));

        User currentUser = getCurrentUser();

        invoice.setStatus(InvoiceStatus.REJECTED);
        invoice.setApprovedBy(currentUser);
        invoice.setApprovedAt(LocalDateTime.now());
        invoice.setApprovalRemarks(request.getRemarks());

        Invoice saved = invoiceRepository.save(invoice);

        auditLogService.log(
                "REJECTED",
                "INVOICE",
                saved.getId(),
                currentUser.getEmail(),
                request.getRemarks()
        );

        notifyVendor(saved, false, request.getRemarks());

        return map(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InvoiceDetailResponse> getPendingApprovals() {

        return invoiceRepository.findByStatus(InvoiceStatus.MATCHED)
                .stream()
                .map(InvoiceMapper::toDetailResponse)
                .toList();
    }

    // ==========================
    // Best-effort vendor notification. A misconfigured or unreachable
    // mail server must never block the approval/rejection workflow.
    // ==========================

    private void notifyVendor(Invoice invoice, boolean approved, String rejectionReason) {

        String vendorEmail = invoice.getVendorEmail();

        if (vendorEmail == null || vendorEmail.isBlank()) {
            return;
        }

        try {
            if (approved) {
                emailService.sendInvoiceApprovedEmail(vendorEmail, invoice.getInvoiceNumber());
            } else {
                emailService.sendInvoiceRejectedEmail(vendorEmail, invoice.getInvoiceNumber(), rejectionReason);
            }
        } catch (Exception e) {
            log.warn("Failed to send invoice {} notification email to {}: {}",
                    approved ? "approval" : "rejection", vendorEmail, e.getMessage());
        }
    }

    // ==========================
    // Logged-in User
    // ==========================

    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found."));
    }

    // ==========================
    // Mapper
    // ==========================

    private ApprovalResponse map(Invoice invoice) {

        return ApprovalResponse.builder()
                .invoiceId(invoice.getId())
                .status(invoice.getStatus())
                .approvedBy(invoice.getApprovedBy().getEmail())
                .approvedAt(invoice.getApprovedAt())
                .remarks(invoice.getApprovalRemarks())
                .build();

    }

}