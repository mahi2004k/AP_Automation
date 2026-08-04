package com.ap_automation.service;

public interface EmailService {

    void sendInvoiceApprovedEmail(
            String to,
            String invoiceNumber
    );

    void sendInvoiceRejectedEmail(
            String to,
            String invoiceNumber,
            String reason
    );

    void sendPaymentEmail(
            String to,
            String invoiceNumber,
            String transactionId
    );
}
