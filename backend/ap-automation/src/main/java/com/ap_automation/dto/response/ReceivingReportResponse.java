package com.ap_automation.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReceivingReportResponse {

    private Long id;

    private String reportNumber;

    private LocalDate receivedDate;

    private Long purchaseOrderId;

    private List<ReceivingReportItemResponse> items;

}