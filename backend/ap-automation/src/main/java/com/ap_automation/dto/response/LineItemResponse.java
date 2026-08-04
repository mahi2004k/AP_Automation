package com.ap_automation.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LineItemResponse {

    private String description;

    private BigDecimal quantity;

    private BigDecimal unitPrice;

    private BigDecimal amount;

}