package com.ap_automation.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReceivingReportItemResponse {

    private Long id;

    private String description;

    private BigDecimal quantityReceived;

}