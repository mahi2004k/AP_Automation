package com.ap_automation.service.impl;

import com.ap_automation.dto.response.InvoiceDetailResponse;
import com.ap_automation.dto.response.InvoiceExtractionResponse;
import com.ap_automation.dto.response.InvoiceResponse;
import com.ap_automation.dto.response.LineItemResponse;
import com.ap_automation.dto.response.ValidationResult;
import com.ap_automation.entity.Invoice;
import com.ap_automation.entity.InvoiceItem;
import com.ap_automation.entity.PurchaseOrder;
import com.ap_automation.entity.User;
import com.ap_automation.enums.InvoiceStatus;
import com.ap_automation.exception.InvoiceNotFoundException;
import com.ap_automation.repository.InvoiceItemRepository;
import com.ap_automation.repository.InvoiceRepository;
import com.ap_automation.repository.PurchaseOrderRepository;
import com.ap_automation.repository.UserRepository;
import com.ap_automation.service.AuditLogService;
import com.ap_automation.service.FileStorageService;
import com.ap_automation.service.InvoiceExtractionService;
import com.ap_automation.service.InvoiceService;
import com.ap_automation.util.PdfTextExtractor;
import com.ap_automation.util.parser.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;

    private final FileStorageService fileStorageService;

    private final InvoiceExtractionService invoiceExtractionService;

    private final InvoiceItemRepository invoiceItemRepository;

    private final PurchaseOrderRepository purchaseOrderRepository;

    private final AuditLogService auditLogService;

    private final UserRepository userRepository;

    @Override
    @Transactional
    public InvoiceResponse uploadInvoice(MultipartFile file) {

        if (file.isEmpty()) {
            throw new RuntimeException("Please upload a PDF.");
        }

        // Save uploaded PDF
        String filePath = fileStorageService.saveFile(file);


        // Extract text from PDF
        String pdfText = PdfTextExtractor.extractText(filePath);


        // AI Extraction
        InvoiceExtractionResponse extraction =
                invoiceExtractionService.extract(pdfText);



        // Validate totals
        ValidationResult validation =
                ValidationUtil.validate(
                        extraction.getLineItems(),
                        extraction.getTotalAmount()
                );


        // Create Invoice Entity
        Invoice invoice = new Invoice();

        invoice.setInvoiceNumber(
                extraction.getInvoiceNumber()
        );

        invoice.setVendorName(
                extraction.getVendorName()
        );

        invoice.setInvoiceDate(
                extraction.getInvoiceDate()
        );

        invoice.setTotalAmount(
                extraction.getTotalAmount()
        );

        invoice.setTaxAmount(
                extraction.getTaxAmount()
        );

        invoice.setPdfUrl(filePath);

        // Track who uploaded the invoice (required for audit trail below)
        User currentUser = getCurrentUserOrNull();
        invoice.setUploadedBy(currentUser);



    /*
       ===============================
       AUTO PO MATCHING LOGIC
       ===============================
    */


        String extractedPoNumber = extraction.getPoNumber();


        if (extractedPoNumber != null &&
                !extractedPoNumber.isBlank()) {


            // PDF contains PO Number

            invoice.setPoNumber(extractedPoNumber);


        } else {


            // PDF does not contain PO Number

            PurchaseOrder purchaseOrder =
                    purchaseOrderRepository
                            .findTopByVendorName(
                                    extraction.getVendorName()
                            )
                            .orElse(null);



            if (purchaseOrder != null) {

                invoice.setPoNumber(
                        purchaseOrder.getPoNumber()
                );

            }

        }



        // Set status

        if (validation.isValid()) {

            invoice.setStatus(
                    InvoiceStatus.EXTRACTED
            );

        } else {

            invoice.setStatus(
                    InvoiceStatus.NEEDS_REVIEW
            );
        }



        // Save Invoice

        Invoice saved  = invoiceRepository.save(invoice);

        auditLogService.log(
                "OCR",
                "INVOICE",
                saved.getId(),
                "SYSTEM",
                "OCR extraction completed."
        );

        auditLogService.log(
                "UPLOAD",
                "INVOICE",
                saved.getId(),
                currentUser != null ? currentUser.getEmail() : "SYSTEM",
                "Invoice uploaded successfully."
        );



        // Save Invoice Items

        for (LineItemResponse item :
                extraction.getLineItems()) {


            InvoiceItem invoiceItem =
                    new InvoiceItem();


            invoiceItem.setDescription(
                    item.getDescription()
            );


            invoiceItem.setQuantity(
                    item.getQuantity()
            );


            invoiceItem.setUnitPrice(
                    item.getUnitPrice()
            );


            invoiceItem.setAmount(
                    item.getAmount()
            );


            invoiceItem.setInvoice(invoice);


            invoiceItemRepository.save(invoiceItem);

        }



        return InvoiceResponse.builder()

                .id(invoice.getId())

                .invoiceNumber(
                        invoice.getInvoiceNumber()
                )

                .message(
                        "Invoice processed successfully."
                )

                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<InvoiceDetailResponse> getAll(InvoiceStatus status) {

        List<Invoice> invoices = (status == null)
                ? invoiceRepository.findAll()
                : invoiceRepository.findByStatus(status);

        return invoices.stream()
                .map(this::map)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public InvoiceDetailResponse getById(Long id) {

        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() ->
                        new InvoiceNotFoundException("Invoice not found."));

        return map(invoice);
    }

    @Override
    @Transactional(readOnly = true)
    public Resource loadInvoiceFile(Long id) {

        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() ->
                        new InvoiceNotFoundException("Invoice not found."));

        if (invoice.getPdfUrl() == null || invoice.getPdfUrl().isBlank()) {
            throw new InvoiceNotFoundException("No file attached to this invoice.");
        }

        try {
            Path path = Path.of(invoice.getPdfUrl());
            Resource resource = new UrlResource(path.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new InvoiceNotFoundException("Invoice file could not be read.");
            }

            return resource;

        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid file path for invoice.", e);
        }
    }

    // ==========================
    // Logged-in User (nullable — some flows may run without an
    // authenticated principal, e.g. tests or system jobs)
    // ==========================

    private User getCurrentUserOrNull() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            return null;
        }

        return userRepository.findByEmail(authentication.getName())
                .orElse(null);
    }

    // ==========================
    // Mapper
    // ==========================

    private InvoiceDetailResponse map(Invoice invoice) {
        return com.ap_automation.util.InvoiceMapper.toDetailResponse(invoice);
    }
}
