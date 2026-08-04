package com.ap_automation.service;

import com.ap_automation.dto.response.ValidationResult;
import com.ap_automation.dto.response.LineItemResponse;

import java.math.BigDecimal;
import java.util.List;

public interface ValidationService {

    ValidationResult validate(
            List<LineItemResponse> items,
            BigDecimal invoiceTotal);

}