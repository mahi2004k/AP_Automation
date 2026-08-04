package com.ap_automation.service.impl;

import com.ap_automation.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendInvoiceApprovedEmail(
            String to,
            String invoiceNumber) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setSubject("Invoice Approved");

        message.setText("""
                Dear Vendor,

                Your invoice has been approved successfully.

                Invoice Number : %s

                Regards,
                AP Automation Team
                """.formatted(invoiceNumber));

        mailSender.send(message);
    }

    @Override
    public void sendInvoiceRejectedEmail(
            String to,
            String invoiceNumber,
            String reason) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setSubject("Invoice Rejected");

        message.setText("""
                Dear Vendor,

                Your invoice has been rejected.

                Invoice Number : %s

                Reason:
                %s

                Regards,
                AP Automation Team
                """.formatted(invoiceNumber, reason));

        mailSender.send(message);
    }

    @Override
    public void sendPaymentEmail(
            String to,
            String invoiceNumber,
            String transactionId) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setSubject("Payment Completed");

        message.setText("""
                Dear Vendor,

                Payment has been completed successfully.

                Invoice Number : %s

                Transaction ID : %s

                Regards,
                AP Automation Team
                """.formatted(invoiceNumber, transactionId));

        mailSender.send(message);
    }
}