package com.ap_automation.service;

import com.ap_automation.dto.request.ReceivingReportRequest;
import com.ap_automation.dto.response.ReceivingReportResponse;

import java.util.List;

public interface ReceivingReportService {

    ReceivingReportResponse create(ReceivingReportRequest request);

    ReceivingReportResponse getById(Long id);

    List<ReceivingReportResponse> getAll();

    ReceivingReportResponse update(Long id, ReceivingReportRequest request);

    void delete(Long id);

}