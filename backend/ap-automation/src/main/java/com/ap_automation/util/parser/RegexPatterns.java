package com.ap_automation.util.parser;

public class RegexPatterns {

    private RegexPatterns() {}

    public static final String[] INVOICE_NUMBER_PATTERNS = {

            "Invoice\\s*Number\\s*:?\\s*(.+)",

            "Invoice\\s*No\\.?\\s*:?\\s*(.+)",

            "Invoice\\s*#\\s*:?\\s*(.+)",

            "Bill\\s*No\\.?\\s*:?\\s*(.+)",

            "Document\\s*No\\.?\\s*:?\\s*(.+)",

            "Inv\\s*No\\.?\\s*:?\\s*(.+)"

    };


    public static final String[] PO_NUMBER_PATTERNS = {

            "PO\\s*Number\\s*:?\\s*(PO\\d+)",

            "PO\\s*No\\.?\\s*:?\\s*(PO\\d+)",

            "Purchase\\s*Order\\s*:?\\s*(PO\\d+)",

            "P\\.O\\.\\s*Number\\s*:?\\s*(PO\\d+)",

            "Order\\s*Number\\s*:?\\s*(PO\\d+)"

    };


    public static final String[] TOTAL_PATTERNS = {

            "Total\\s*Amount\\s*:?\\s*(\\d+(?:\\.\\d+)?)",

            "Grand\\s*Total\\s*:?\\s*(\\d+(?:\\.\\d+)?)",

            "Net\\s*Amount\\s*:?\\s*(\\d+(?:\\.\\d+)?)",

            "Amount\\s*Payable\\s*:?\\s*(\\d+(?:\\.\\d+)?)"

    };


    public static final String[] GST_PATTERNS = {

            "GST\\s*:?\\s*(\\d+(?:\\.\\d+)?)",

            "Tax\\s*:?\\s*(\\d+(?:\\.\\d+)?)",

            "VAT\\s*:?\\s*(\\d+(?:\\.\\d+)?)"

    };

}