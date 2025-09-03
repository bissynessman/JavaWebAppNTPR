package tvz.ntpr.core.utils;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.font.PdfSimpleFont;
import com.itextpdf.kernel.font.PdfTrueTypeFont;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import org.jsoup.nodes.Element;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import tvz.ntpr.core.rest.TimeApi;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneOffset;

public class HtmlToPdf {
    private static final String OUTPUT_DIRECTORY = "app-generated\\student_reports";

    private static final TimeApi TIME_API = new TimeApi();

    public static File scrapeHtmlToPdf(String url) throws IOException {
        Path jarDirectory = Paths.get(System.getProperty("java.class.path")).toAbsolutePath().getParent();
        Path studentReportsDirectory = jarDirectory.resolve(OUTPUT_DIRECTORY);
        String timestamp = String.valueOf(TIME_API.getCurrentTime().toEpochSecond(ZoneOffset.UTC));
        File output =
                Paths.get(studentReportsDirectory.toString(), "student_report(" + timestamp + ").pdf").toFile();
        Files.createDirectories(studentReportsDirectory);
        org.jsoup.nodes.Document htmlDoc = Jsoup.connect(url).get();

        PdfWriter writer = new PdfWriter(output);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document pdfDocument = new Document(pdfDoc);

        String title = htmlDoc.title();
        pdfDocument.add(
                new Paragraph(title)
                        .setFont(FontSupplier.BOLD)
                        .setFontSize(18)
                        .setTextAlignment(TextAlignment.CENTER));

        Elements bodyChildren = htmlDoc.body().children();
        for (Element element : bodyChildren)
            convertHtmlElementToPdf(element, pdfDocument);

        pdfDocument.close();

        return output;
    }

    private static void convertHtmlElementToPdf(Element element, Document pdfDocument) {
        switch (element.tagName()) {
            case "p":
                pdfDocument.add(
                        new Paragraph(element.text()).setFont(FontSupplier.REGULAR).setFontSize(12).setMarginTop(5));
                break;
            case "h1":
                pdfDocument.add(
                        new Paragraph(element.text()).setFont(FontSupplier.BOLD).setFontSize(18).setMarginTop(15));
                break;
            case "h2":
                pdfDocument.add(
                        new Paragraph(element.text()).setFont(FontSupplier.BOLD).setFontSize(16).setMarginTop(12));
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

    private static class FontSupplier {
        private static final PdfFont REGULAR;
        private static final PdfFont BOLD;

        static {
            PdfFont regular;
            PdfFont bold;
            try {
                regular = PdfFontFactory.createFont(
                        StandardFonts.HELVETICA,
                        PdfEncodings.WINANSI,
                        PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
                bold = PdfFontFactory.createFont(
                        StandardFonts.HELVETICA_BOLD,
                        PdfEncodings.WINANSI,
                        PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
            } catch (IOException e) {
                e.printStackTrace();
                regular = null;
                bold = null;
            }

            REGULAR = regular;
            BOLD = bold;
        }
    }
}
