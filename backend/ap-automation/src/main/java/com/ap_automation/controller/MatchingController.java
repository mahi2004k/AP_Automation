package com.ap_automation.controller;

import com.ap_automation.dto.response.MatchResultResponse;
import com.ap_automation.service.ThreeWayMatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/matching")
@RequiredArgsConstructor
public class MatchingController {

    private final ThreeWayMatchingService threeWayMatchingService;

    @PostMapping("/{invoiceId}")
    public ResponseEntity<MatchResultResponse> matchInvoice(
            @PathVariable Long invoiceId) {

        MatchResultResponse response =
                threeWayMatchingService.match(invoiceId);

        return ResponseEntity.ok(response);
    }
}