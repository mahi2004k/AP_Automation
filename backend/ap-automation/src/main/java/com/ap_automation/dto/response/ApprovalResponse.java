package com.ap_automation.dto.response;

import com.ap_automation.enums.InvoiceStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApprovalResponse {

    private Long invoiceId;

    private InvoiceStatus status;

    private String approvedBy;

    private LocalDateTime approvedAt;

    private String remarks;

}