package com.ap_automation.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceExtractionResponse {

    private String vendorName;

    private String invoiceNumber;

    private String poNumber;

    private LocalDate invoiceDate;

    private BigDecimal totalAmount;

    private BigDecimal taxAmount;

    @Builder.Default
    private List<LineItemResponse> lineItems = new ArrayList<>();

    private ValidationResult validationResult;

}