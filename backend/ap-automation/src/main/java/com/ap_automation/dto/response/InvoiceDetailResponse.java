package com.ap_automation.dto.response;

import com.ap_automation.enums.CurrencyType;
import com.ap_automation.enums.InvoiceStatus;
import com.ap_automation.enums.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceDetailResponse {

    private Long id;

    private String invoiceNumber;

    private String poNumber;

    private LocalDate invoiceDate;

    private LocalDate dueDate;

    private String vendorName;

    private String vendorEmail;

    private String vendorGstNumber;

    private CurrencyType currency;

    private BigDecimal subtotal;

    private BigDecimal taxAmount;

    private BigDecimal totalAmount;

    private InvoiceStatus status;

    private PaymentStatus paymentStatus;

    private Double extractionConfidence;

    private boolean hasFile;

    private String uploadedBy;

    private String approvedBy;

    private LocalDateTime approvedAt;

    private String approvalRemarks;

    private LocalDateTime createdAt;

    private List<LineItemResponse> items;
}
