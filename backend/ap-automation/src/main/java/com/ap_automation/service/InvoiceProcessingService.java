package com.ap_automation.service;

import com.ap_automation.dto.response.InvoiceResponse;
import org.springframework.web.multipart.MultipartFile;

public interface InvoiceProcessingService {

    InvoiceResponse process(MultipartFile file);

}