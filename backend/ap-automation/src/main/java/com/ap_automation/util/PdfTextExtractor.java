package com.ap_automation.util;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;

public class PdfTextExtractor {

    public static String extractText(String pdfPath) {

        try (PDDocument document = Loader.loadPDF(new File(pdfPath))) {

            PDFTextStripper stripper = new PDFTextStripper();

            return stripper.getText(document);

        } catch (IOException e) {
            e.printStackTrace();   // Add this line
            throw new RuntimeException("Unable to read PDF.", e);
        }

    }
}
