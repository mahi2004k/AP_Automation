package com.ap_automation.util.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InvoiceFieldExtractor {

    private InvoiceFieldExtractor(){}

    public static String extract(String text, String[] patterns){

        for(String regex : patterns){

            Pattern pattern = Pattern.compile(
                    regex,
                    Pattern.CASE_INSENSITIVE);

            Matcher matcher = pattern.matcher(text);

            if(matcher.find()){

                return matcher.group(1).trim();

            }

        }

        return "";

    }

}