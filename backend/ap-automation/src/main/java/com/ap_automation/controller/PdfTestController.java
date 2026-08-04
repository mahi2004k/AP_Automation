package com.ap_automation.controller;

import com.ap_automation.util.PdfTextExtractor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class PdfTestController {

    @GetMapping("/pdf")
    public String test() {

        System.out.println("PDF API CALLED");

        return PdfTextExtractor.extractText(
                "uploads/invoice.pdf"
        );
    }
}
