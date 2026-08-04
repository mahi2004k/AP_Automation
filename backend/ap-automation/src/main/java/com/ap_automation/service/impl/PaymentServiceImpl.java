package com.ap_automation.service.impl;

import com.ap_automation.dto.request.PaymentRequest;
import com.ap_automation.dto.response.PaymentResponse;
import com.ap_automation.entity.Invoice;
import com.ap_automation.entity.Payment;
import com.ap_automation.enums.InvoiceStatus;
import com.ap_automation.enums.PaymentStatus;
import com.ap_automation.exception.InvoiceNotFoundException;
import com.ap_automation.exception.PaymentAlreadyExistsException;
import com.ap_automation.exception.PaymentNotFoundException;
import com.ap_automation.repository.InvoiceRepository;
import com.ap_automation.repository.PaymentRepository;
import com.ap_automation.service.AuditLogService;
import com.ap_automation.service.EmailService;
import com.ap_automation.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;
    private final AuditLogService auditLogService;
    private final EmailService emailService;

    @Override
    public PaymentResponse makePayment(Long invoiceId,
                                       PaymentRequest request) {

        paymentRepository.findByInvoiceId(invoiceId)
                .ifPresent(payment -> {
                    throw new PaymentAlreadyExistsException(
                            "Payment already exists for this invoice."
                    );
                });

        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() ->
                        new InvoiceNotFoundException("Invoice not found."));

        if (invoice.getStatus() != InvoiceStatus.APPROVED) {
            throw new IllegalStateException(
                    "Only APPROVED invoices can be paid."
            );
        }

        Payment payment = Payment.builder()
                .paymentNumber("PAY-" +
                        UUID.randomUUID()
                                .toString()
                                .substring(0, 8)
                                .toUpperCase())
                .amount(invoice.getTotalAmount())
                .paymentDate(LocalDate.now())
                .paymentMethod(request.getPaymentMethod())
                .transactionReference(request.getTransactionReference())
                .status(PaymentStatus.PAID)
                .invoice(invoice)
                .build();

        Payment saved = paymentRepository.save(payment);

        auditLogService.log(
                "PAYMENT",
                "INVOICE",
                invoice.getId(),
                "FINANCE",
                "Payment completed. Transaction Ref: "
                        + payment.getTransactionReference()
        );

        invoice.setPaymentStatus(PaymentStatus.PAID);
        invoice.setStatus(InvoiceStatus.PAID);

        invoiceRepository.save(invoice);

        if (invoice.getVendorEmail() != null && !invoice.getVendorEmail().isBlank()) {
            try {
                emailService.sendPaymentEmail(
                        invoice.getVendorEmail(),
                        invoice.getInvoiceNumber(),
                        payment.getTransactionReference()
                );
            } catch (Exception e) {
                log.warn("Failed to send payment confirmation email to {}: {}",
                        invoice.getVendorEmail(), e.getMessage());
            }
        }

        return map(saved);

    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getById(Long id) {

        return map(
                paymentRepository.findById(id)
                        .orElseThrow(() ->
                                new PaymentNotFoundException(
                                        "Payment not found."
                                ))
        );

    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getByInvoice(Long invoiceId) {

        return map(
                paymentRepository.findByInvoiceId(invoiceId)
                        .orElseThrow(() ->
                                new PaymentNotFoundException(
                                        "Payment not found."
                                ))
        );

    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponse> getAll() {

        return paymentRepository.findAll()
                .stream()
                .map(this::map)
                .toList();

    }

    private PaymentResponse map(Payment payment) {

        return PaymentResponse.builder()
                .id(payment.getId())
                .paymentNumber(payment.getPaymentNumber())
                .invoiceId(payment.getInvoice().getId())
                .amount(payment.getAmount())
                .paymentDate(payment.getPaymentDate())
                .paymentMethod(payment.getPaymentMethod())
                .transactionReference(payment.getTransactionReference())
                .status(payment.getStatus())
                .build();

    }

}