package com.ap_automation.controller;

import com.ap_automation.dto.response.InvoiceDetailResponse;
import com.ap_automation.dto.response.InvoiceResponse;
import com.ap_automation.enums.InvoiceStatus;
import com.ap_automation.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public InvoiceResponse uploadInvoice(
            @RequestParam("file") MultipartFile file
    ){

        return invoiceService.uploadInvoice(file);
    }

    @GetMapping
    public List<InvoiceDetailResponse> getAll(
            @RequestParam(required = false) InvoiceStatus status
    ) {

        return invoiceService.getAll(status);
    }

    @GetMapping("/{id}")
    public InvoiceDetailResponse getById(@PathVariable Long id) {

        return invoiceService.getById(id);
    }

    @GetMapping("/{id}/file")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) {

        Resource file = invoiceService.loadInvoiceFile(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + file.getFilename() + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(file);
    }
}
