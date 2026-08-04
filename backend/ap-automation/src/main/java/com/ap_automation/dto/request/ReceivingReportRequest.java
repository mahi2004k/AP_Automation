package com.ap_automation.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReceivingReportRequest {

    @NotBlank
    private String reportNumber;

    @NotNull
    private LocalDate receivedDate;

    @NotNull
    private Long purchaseOrderId;

    @Valid
    private List<ReceivingReportItemRequest> items;

}