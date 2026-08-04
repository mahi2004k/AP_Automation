package com.ap_automation.controller;

import com.ap_automation.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/test")
    public ResponseEntity<String> sendTestEmail(
            @RequestParam String to) {

        emailService.sendInvoiceApprovedEmail(
                to,
                "INV-1001"
        );

        return ResponseEntity.ok(
                "Email sent successfully."
        );
    }
}