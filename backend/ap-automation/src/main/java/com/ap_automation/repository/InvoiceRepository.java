package com.ap_automation.repository;

import com.ap_automation.entity.Invoice;
import com.ap_automation.enums.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    long countByStatus(InvoiceStatus status);

    Optional<Invoice> findByInvoiceNumberAndVendorName(
            String invoiceNumber,
            String vendorName
    );

    List<Invoice> findByVendorName(String vendorName);

    List<Invoice> findByStatus(InvoiceStatus status);
}
