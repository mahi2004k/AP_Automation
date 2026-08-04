package com.ap_automation.service;

import com.ap_automation.dto.response.InvoiceExtractionResponse;

public interface InvoiceExtractionService {

    InvoiceExtractionResponse extract(String pdfText);
}
