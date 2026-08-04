package com.ap_automation.repository;

import com.ap_automation.entity.ReceivingReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReceivingReportRepository
        extends JpaRepository<ReceivingReport, Long> {

    Optional<ReceivingReport> findByReportNumber(String reportNumber);

    Optional<ReceivingReport> findByPurchaseOrderId(Long purchaseOrderId);

}