package com.ap_automation.service.impl;

import com.ap_automation.dto.response.InvoiceExtractionResponse;
import com.ap_automation.service.InvoiceExtractionService;
import com.ap_automation.util.parser.InvoiceFieldExtractor;
import com.ap_automation.util.parser.LineItemExtractor;
import com.ap_automation.util.parser.RegexPatterns;
import com.ap_automation.util.parser.ValidationUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
public class InvoiceExtractionServiceImpl implements InvoiceExtractionService {


    @Override
    public InvoiceExtractionResponse extract(String text) {


        InvoiceExtractionResponse response =
                new InvoiceExtractionResponse();


        // Extract Vendor
        response.setVendorName(
                extractVendor(text)
        );


        // Extract PO Number
        response.setPoNumber(
                extractPoNumber(text)
        );


        // Extract Invoice Number
        response.setInvoiceNumber(
                InvoiceFieldExtractor.extract(
                        text,
                        RegexPatterns.INVOICE_NUMBER_PATTERNS
                )
        );


        // Extract Invoice Date
        response.setInvoiceDate(
                extractDate(text)
        );


        // Extract Tax
        response.setTaxAmount(
                extractTax(text)
        );


        // Extract Total Amount
        response.setTotalAmount(
                extractTotal(text)
        );


        // Extract Line Items
        response.setLineItems(
                LineItemExtractor.extract(text)
        );


        // Validate invoice
        response.setValidationResult(

                ValidationUtil.validate(
                        response.getLineItems(),
                        response.getTotalAmount()
                )

        );


        return response;
    }



    /**
     * Extract Vendor Name
     *
     * Avoids:
     * INVOICE
     * TAX INVOICE
     * BILL
     * Labels
     */
    private String extractVendor(String text) {


        String[] lines = text.split("\\R");


        for(String line : lines) {


            line = line.trim();


            if(line.isEmpty()) {
                continue;
            }


            String lower =
                    line.toLowerCase();



            // Ignore common invoice headings

            if(lower.equals("invoice")
                    || lower.equals("tax invoice")
                    || lower.equals("bill")
                    || lower.equals("invoice bill")) {

                continue;
            }



            // Ignore fields

            if(lower.contains("invoice number")
                    || lower.contains("invoice no")
                    || lower.contains("date")
                    || lower.contains("po number")
                    || lower.contains("purchase order")
                    || lower.contains("gst")
                    || lower.contains("amount")) {

                continue;
            }



            return line;

        }


        return "";
    }





    /**
     * Extract PO Number
     *
     * Examples:
     *
     * PO Number: PO1001
     * PO No: PO1001
     * Purchase Order: PO1001
     */
    private String extractPoNumber(String text) {

        return InvoiceFieldExtractor.extract(
                text,
                RegexPatterns.PO_NUMBER_PATTERNS
        );

    }





    /**
     * Supports multiple date formats
     */
    private LocalDate extractDate(String text) {


        String[] patterns = {


                "Date\\s*:?\\s*(\\d{2}-\\d{2}-\\d{4})",


                "Invoice\\s*Date\\s*:?\\s*(\\d{2}-\\d{2}-\\d{4})",


                "Date\\s*:?\\s*(\\d{2}/\\d{2}/\\d{4})",


                "Invoice\\s*Date\\s*:?\\s*(\\d{2}/\\d{2}/\\d{4})"

        };



        for(String regex : patterns) {


            String value =
                    InvoiceFieldExtractor.extract(
                            text,
                            new String[]{regex}
                    );


            if(!value.isBlank()) {


                try {


                    if(value.contains("-")) {


                        return LocalDate.parse(
                                value,
                                DateTimeFormatter.ofPattern(
                                        "dd-MM-yyyy"
                                )
                        );

                    }


                    if(value.contains("/")) {


                        return LocalDate.parse(
                                value,
                                DateTimeFormatter.ofPattern(
                                        "dd/MM/yyyy"
                                )
                        );

                    }


                }
                catch(DateTimeParseException ignored){

                }

            }

        }


        return null;
    }





    /**
     * Extract GST / Tax
     */
    private BigDecimal extractTax(String text) {


        String value =
                InvoiceFieldExtractor.extract(
                        text,
                        RegexPatterns.GST_PATTERNS
                );


        if(value.isBlank()) {

            return BigDecimal.ZERO;

        }


        return new BigDecimal(value);

    }





    /**
     * Extract Total Amount
     */
    private BigDecimal extractTotal(String text) {


        String value =
                InvoiceFieldExtractor.extract(
                        text,
                        RegexPatterns.TOTAL_PATTERNS
                );



        if(value.isBlank()) {

            return BigDecimal.ZERO;

        }



        return new BigDecimal(value);

    }

}