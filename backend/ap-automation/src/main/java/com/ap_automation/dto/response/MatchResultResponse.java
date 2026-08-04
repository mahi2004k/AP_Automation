package com.ap_automation.dto.response;

import com.ap_automation.enums.MatchStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchResultResponse {

    private Long invoiceId;

    private boolean vendorMatched;

    private boolean poMatched;

    private boolean itemMatched;

    private boolean quantityMatched;

    private boolean priceMatched;

    private boolean totalMatched;

    private String remarks;

    private MatchStatus status;

}