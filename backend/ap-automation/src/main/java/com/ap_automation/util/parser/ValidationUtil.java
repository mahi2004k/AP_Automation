package com.ap_automation.util.parser;

import com.ap_automation.dto.response.LineItemResponse;
import com.ap_automation.dto.response.ValidationResult;

import java.math.BigDecimal;
import java.util.List;

public class ValidationUtil {

    private ValidationUtil(){}

    public static ValidationResult validate(
            List<LineItemResponse> items,
            BigDecimal invoiceTotal
    ){

        BigDecimal calculatedTotal = BigDecimal.ZERO;

        for(LineItemResponse item : items){

            calculatedTotal =
                    calculatedTotal.add(item.getAmount());

        }

        boolean valid =
                calculatedTotal.compareTo(invoiceTotal) == 0;

        return ValidationResult.builder()
                .valid(valid)
                .calculatedTotal(calculatedTotal)
                .invoiceTotal(invoiceTotal)
                .message(valid
                        ? "Invoice is valid."
                        : "Invoice total does not match line items.")
                .build();

    }

}