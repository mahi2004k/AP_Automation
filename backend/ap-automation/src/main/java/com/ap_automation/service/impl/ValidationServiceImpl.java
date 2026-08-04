package com.ap_automation.service.impl;

import com.ap_automation.dto.response.LineItemResponse;
import com.ap_automation.dto.response.ValidationResult;
import com.ap_automation.service.ValidationService;
import com.ap_automation.util.parser.ValidationUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ValidationServiceImpl implements ValidationService {

    @Override
    public ValidationResult validate(
            List<LineItemResponse> items,
            BigDecimal invoiceTotal) {

        return ValidationUtil.validate(
                items,
                invoiceTotal
        );
    }

}