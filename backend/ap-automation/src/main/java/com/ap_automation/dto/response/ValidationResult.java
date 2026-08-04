package com.ap_automation.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidationResult {

    private boolean valid;

    private BigDecimal calculatedTotal;

    private BigDecimal invoiceTotal;

    private String message;

}