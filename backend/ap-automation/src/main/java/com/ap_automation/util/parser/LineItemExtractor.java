package com.ap_automation.util.parser;

import com.ap_automation.dto.response.LineItemResponse;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LineItemExtractor {

    private LineItemExtractor(){}

    /**
     * Expected format:
     *
     * Dell Laptop        2   45000   90000
     * Mouse              3     500    1500
     */
    public static List<LineItemResponse> extract(String text){

        List<LineItemResponse> items = new ArrayList<>();

        Pattern pattern = Pattern.compile(
                "^(.+?)\\s+(\\d+)\\s+(\\d+(?:\\.\\d+)?)\\s+(\\d+(?:\\.\\d+)?)$",
                Pattern.MULTILINE);

        Matcher matcher = pattern.matcher(text);

        while(matcher.find()){

            items.add(
                    LineItemResponse.builder()
                            .description(matcher.group(1).trim())
                            .quantity(new BigDecimal(matcher.group(2)))
                            .unitPrice(new BigDecimal(matcher.group(3)))
                            .amount(new BigDecimal(matcher.group(4)))
                            .build()
            );

        }

        return items;

    }

}