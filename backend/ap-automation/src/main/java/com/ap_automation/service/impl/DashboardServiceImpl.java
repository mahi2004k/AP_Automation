package com.ap_automation.service.impl;

import com.ap_automation.dto.response.DashboardResponse;
import com.ap_automation.enums.InvoiceStatus;
import com.ap_automation.repository.InvoiceRepository;
import com.ap_automation.repository.PurchaseOrderRepository;
import com.ap_automation.repository.ReceivingReportRepository;
import com.ap_automation.repository.UserRepository;
import com.ap_automation.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final InvoiceRepository invoiceRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final ReceivingReportRepository receivingReportRepository;
    private final UserRepository userRepository;

    @Override
    public DashboardResponse getDashboard() {

        return DashboardResponse.builder()
                .totalInvoices(invoiceRepository.count())
                .matchedInvoices(
                        invoiceRepository.countByStatus(
                                InvoiceStatus.MATCHED
                        )
                )
                .pendingInvoices(
                        invoiceRepository.countByStatus(
                                InvoiceStatus.PENDING
                        )
                )
                .needsReviewInvoices(
                        invoiceRepository.countByStatus(
                                InvoiceStatus.NEEDS_REVIEW
                        )
                )
                .totalPurchaseOrders(
                        purchaseOrderRepository.count()
                )
                .totalReceivingReports(
                        receivingReportRepository.count()
                )
                .totalUsers(
                        userRepository.count()
                )
                .build();

    }

}