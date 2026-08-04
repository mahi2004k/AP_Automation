package com.ap_automation.dto.request;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReceivingReportItemRequest {

    private String description;

    private BigDecimal quantityReceived;

}