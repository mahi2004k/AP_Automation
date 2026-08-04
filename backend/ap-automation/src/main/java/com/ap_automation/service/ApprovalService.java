package com.ap_automation.service;

import com.ap_automation.dto.request.ApprovalRequest;
import com.ap_automation.dto.response.ApprovalResponse;
import com.ap_automation.dto.response.InvoiceDetailResponse;

import java.util.List;

public interface ApprovalService {

    ApprovalResponse approve(Long invoiceId, ApprovalRequest request);

    ApprovalResponse reject(Long invoiceId, ApprovalRequest request);

    List<InvoiceDetailResponse> getPendingApprovals();

}
