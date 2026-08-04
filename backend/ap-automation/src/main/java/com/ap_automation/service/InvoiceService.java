package com.ap_automation.service;

import com.ap_automation.dto.response.InvoiceDetailResponse;
import com.ap_automation.dto.response.InvoiceResponse;
import com.ap_automation.enums.InvoiceStatus;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface InvoiceService {

    InvoiceResponse uploadInvoice(MultipartFile file);

    List<InvoiceDetailResponse> getAll(InvoiceStatus status);

    InvoiceDetailResponse getById(Long id);

    Resource loadInvoiceFile(Long id);
}
