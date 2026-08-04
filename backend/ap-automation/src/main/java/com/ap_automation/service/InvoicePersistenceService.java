package com.ap_automation.service;

import com.ap_automation.dto.response.InvoiceExtractionResponse;
import com.ap_automation.dto.response.ValidationResult;
import com.ap_automation.entity.Invoice;

public interface InvoicePersistenceService {

    Invoice saveInvoice(
            InvoiceExtractionResponse extraction,
            ValidationResult validation,
            String pdfPath);

}