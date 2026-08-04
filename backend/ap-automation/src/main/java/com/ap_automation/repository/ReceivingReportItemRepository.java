package com.ap_automation.repository;

import com.ap_automation.entity.ReceivingReportItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceivingReportItemRepository
        extends JpaRepository<ReceivingReportItem, Long> {
}