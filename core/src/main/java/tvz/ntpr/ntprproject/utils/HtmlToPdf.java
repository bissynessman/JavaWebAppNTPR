package tvz.ntpr.ntprproject.utils;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import org.jsoup.nodes.Element;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;

public class HtmlToPdf {
    public static void scrapeHtmlToPdfFile(String url, String userUuid, String outputFilePath) {
        try {
            org.jsoup.nodes.Document htmlDoc = Jsoup.connect(url + "/" + userUuid).get();
            String title = htmlDoc.title();

            PdfFont bold = PdfFontFactory.createFont(
                    StandardFonts.HELVETICA_BOLD,
                    PdfEncodings.WINANSI,
                    PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);

            PdfWriter writer = new PdfWriter(outputFilePath);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document pdfDocument = new Document(pdfDoc);

            pdfDocument.add(new Paragraph(title).setFont(bold).setFontSize(18).setTextAlignment(TextAlignment.CENTER));

            Elements bodyChildren = htmlDoc.body().children();
            for (Element element : bodyChildren) {
                convertHtmlElementToPdf(element, pdfDocument);
            }

            pdfDocument.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static byte[] scrapeHtmlToPdfByteArray(String url, String studentId) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            org.jsoup.nodes.Document htmlDoc = Jsoup.connect(url + "/" + studentId).get();
            String title = htmlDoc.title();

            PdfFont bold = PdfFontFactory.createFont(
                    StandardFonts.HELVETICA_BOLD,
                    PdfEncodings.WINANSI,
                    PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);

            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document pdfDocument = new Document(pdfDoc);

            pdfDocument.add(new Paragraph(title).setFont(bold).setFontSize(18).setTextAlignment(TextAlignment.CENTER));

            Elements bodyChildren = htmlDoc.body().children();
            for (Element element : bodyChildren) {
                convertHtmlElementToPdf(element, pdfDocument);
            }

            pdfDocument.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return outputStream.toByteArray();
    }

    private static void convertHtmlElementToPdf(Element element, Document pdfDocument) throws Exception {
        PdfFont regular = PdfFontFactory.createFont(
                StandardFonts.HELVETICA,
                PdfEncodings.WINANSI,
                PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
        PdfFont bold = PdfFontFactory.createFont(
                StandardFonts.HELVETICA_BOLD,
                PdfEncodings.WINANSI,
                PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);

        switch (element.tagName()) {
            case "p":
                pdfDocument.add(new Paragraph(element.text()).setFont(regular).setFontSize(12).setMarginTop(5));
                break;
            case "h1":
                pdfDocument.add(new Paragraph(element.text()).setFont(bold).setFontSize(18).setMarginTop(15));
                break;
            case "h2":
                pdfDocument.add(new Paragraph(element.text()).setFont(bold).setFontSize(16).setMarginTop(12));
                break;
            case "table":
                Table table = new Table(element.select("thead th").size());
                Elements rows = element.select("tr");
                for (Element row : rows) {
                    Elements cols = row.select("td, th");
                    for (Element col : cols)
                        table.addCell(new Cell().add(new Paragraph(col.text()).setFontSize(10)));
                }
                pdfDocument.add(table.setMarginTop(10));
                break;
            case "div":
                Elements divChildren = element.children();
                for (Element child : divChildren)
                    convertHtmlElementToPdf(child, pdfDocument);
                break;
            default:
                break;
        }
    }
}
