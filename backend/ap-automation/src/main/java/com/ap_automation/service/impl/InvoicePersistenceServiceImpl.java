package com.ap_automation.service.impl;

import com.ap_automation.dto.response.InvoiceExtractionResponse;
import com.ap_automation.dto.response.LineItemResponse;
import com.ap_automation.dto.response.ValidationResult;
import com.ap_automation.entity.Invoice;
import com.ap_automation.entity.InvoiceItem;
import com.ap_automation.enums.InvoiceStatus;
import com.ap_automation.repository.InvoiceItemRepository;
import com.ap_automation.repository.InvoiceRepository;
import com.ap_automation.service.AuditLogService;
import com.ap_automation.service.InvoicePersistenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InvoicePersistenceServiceImpl
        implements InvoicePersistenceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceItemRepository invoiceItemRepository;
    private final AuditLogService auditLogService;

    @Override
    @Transactional
    public Invoice saveInvoice(
            InvoiceExtractionResponse extraction,
            ValidationResult validation,
            String pdfPath) {

        Invoice invoice = new Invoice();

        invoice.setInvoiceNumber(extraction.getInvoiceNumber());
        invoice.setVendorName(extraction.getVendorName());
        invoice.setInvoiceDate(extraction.getInvoiceDate());
        invoice.setTaxAmount(extraction.getTaxAmount());
        invoice.setTotalAmount(extraction.getTotalAmount());
        invoice.setPdfUrl(pdfPath);

        invoice.setStatus(
                validation.isValid()
                        ? InvoiceStatus.EXTRACTED
                        : InvoiceStatus.NEEDS_REVIEW
        );

        invoice = invoiceRepository.save(invoice);

        auditLogService.log(
                "OCR",
                "INVOICE",
                invoice.getId(),
                "SYSTEM",
                "OCR extraction completed."
        );

        for (LineItemResponse dto : extraction.getLineItems()) {

            InvoiceItem item = new InvoiceItem();

            item.setDescription(dto.getDescription());
            item.setQuantity(dto.getQuantity());
            item.setUnitPrice(dto.getUnitPrice());
            item.setAmount(dto.getAmount());

            item.setInvoice(invoice);

            invoiceItemRepository.save(item);
        }

        return invoice;
    }

}