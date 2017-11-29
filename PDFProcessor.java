package org.tm.util.pdf.utils;


import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import java.io.IOException;

public class PDFProcessor {

    public static void split(String sourceFile, int pageFrom, int pageTo, String targetFile) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(sourceFile));
        PdfDocument toDocument = new PdfDocument(new PdfWriter(targetFile));
        pdfDoc.copyPagesTo(pageFrom, pageTo, toDocument);
    	pdfDoc.close();
    	toDocument.close();
    }
    
    
    public static boolean pageValidation(String fileName, int pageFrom, int pageTo) {
        PdfDocument pdfDoc;
        int totalPages = 0;
		try {
			pdfDoc = new PdfDocument(new PdfReader(fileName));
	        totalPages = pdfDoc.getNumberOfPages();
		} catch (IOException e) {
			e.printStackTrace();
		}
        if (pageFrom >= 1 && pageFrom <= pageTo && pageTo <= totalPages) {
        	return true;
        } else {
        	return false;
        }
    }
    
}









