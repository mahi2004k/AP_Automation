package com.ap_automation.repository;

import com.ap_automation.entity.MatchResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MatchResultRepository
        extends JpaRepository<MatchResult, Long> {

    Optional<MatchResult> findByInvoiceId(Long invoiceId);

}