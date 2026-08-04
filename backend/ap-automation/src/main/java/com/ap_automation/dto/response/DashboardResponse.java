package com.ap_automation.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardResponse {

    private long totalInvoices;

    private long matchedInvoices;

    private long pendingInvoices;

    private long needsReviewInvoices;

    private long totalPurchaseOrders;

    private long totalReceivingReports;

    private long totalUsers;

}