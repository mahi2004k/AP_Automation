package com.ap_automation.service;

import com.ap_automation.dto.response.MatchResultResponse;

public interface ThreeWayMatchingService {

    MatchResultResponse match(Long invoiceId);

}