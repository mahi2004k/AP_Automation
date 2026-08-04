package com.ap_automation.controller;

import com.ap_automation.dto.response.InvoiceExtractionResponse;
import com.ap_automation.service.InvoiceExtractionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class ExtractionController {

    private final InvoiceExtractionService extractionService;

    @PostMapping("/extract")
    public InvoiceExtractionResponse extract(
            @RequestBody String text) {

        return extractionService.extract(text);

    }
}
