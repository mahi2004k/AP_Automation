package com.ap_automation.entity;

import com.ap_automation.enums.CurrencyType;
import com.ap_automation.enums.InvoiceStatus;
import com.ap_automation.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "invoices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ==========================
    // Invoice Details
    // ==========================
    private String invoiceNumber;

    private String poNumber;

    private LocalDate invoiceDate;

    private LocalDate dueDate;

    // ==========================
    // Vendor Details
    // ==========================
    private String vendorName;

    private String vendorEmail;

    private String vendorGstNumber;

    @Column(length = 1000)
    private String vendorAddress;

    // ==========================
    // Financial Details
    // ==========================
    @Enumerated(EnumType.STRING)
    private CurrencyType currency;

    private BigDecimal subtotal;

    private BigDecimal taxAmount;

    private BigDecimal totalAmount;

    // ==========================
    // Status
    // ==========================
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    private InvoiceStatus status;

    // ==========================
    // AI Processing Details
    // ==========================
    private Double extractionConfidence;

    private Long processingTimeMs;

    // ==========================
    // File Information
    // ==========================
    private String pdfUrl;

    // ==========================
    // Uploaded By
    // ==========================
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by")
    private User uploadedBy;

    // ==========================
// Approval Details
// ==========================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    private LocalDateTime approvedAt;

    @Column(length = 1000)
    private String approvalRemarks;

    // ==========================
    // Invoice Line Items
    // ==========================
    @OneToMany(
            mappedBy = "invoice",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<InvoiceItem> invoiceItems = new ArrayList<>();
}