package seedu.address.model;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class PdfConverterTest {
    private final float horizontalWrap = 432;
    private final float xInit = 90;
    private final float yInit = 702;

    private float x = 90;
    private float y = 702;
    private final float margin = 5;
    private float yInitTable = -1;
    private final float leftMarginPosition = 90;
    private final float rightMarginPosition = 522;

    private final PDFont fontBold = PDType1Font.HELVETICA_BOLD;
    private final PDFont fontItalic = PDType1Font.HELVETICA_OBLIQUE;
    private final PDFont font = PDType1Font.HELVETICA;
    private final Color black = Color.BLACK;
    private final Color red = new Color(194, 47, 40);
    private final Color yellow = new Color(219, 168, 0);
    private final Color green = new Color(0, 100, 0);
    private Color curColor;

    private final int fontTitleSize = 20;

    private final int fontHeadingSize = 18;
    private final int fontTableHeaderSize = 16;

    private final int fontTableContentSize = 14;
    private final int fontDateSize = 12;

    private PDDocument document;

    private PDPage page;

    private PDPageContentStream contentStream;
    private final PdfConverter pdfConverter = new PdfConverter();

    private String STRING_1 = "THIS_IS_A_SAMPLE_STRING:)";

    @Test
    public void elementsNotSetUp_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> this.document.close());
        assertThrows(NullPointerException.class, () -> this.document.addPage(page));
        assertThrows(NullPointerException.class, () -> new PDPageContentStream(document, page));
        assertThrows(NullPointerException.class, () -> this.contentStream.beginText());
        assertThrows(NullPointerException.class, () -> this.contentStream.endText());
        assertThrows(NullPointerException.class, () -> this.contentStream.close());
    }

    @Test
    public void setUpContentStream_contentStreamOpened() throws IOException {
        pdfConverter.setUpContentStream(this.font, this.fontTableContentSize, this.x, this.y);
    }

    @Test
    public void exportProgress_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> pdfConverter.exportProgress(null));
    }

    @Test
    public void getNumberOfCharsPossible_nullString_returnsZero() throws IOException {
        assertTrue(pdfConverter.getNumberOfCharsPossible("", font, fontTableContentSize,
                this.horizontalWrap) == 0);
    }

    @Test
    public void getNumberOfCharsPossible_validString_returnsValidWrappedStringLength() throws IOException {
        int numberOfChars = pdfConverter.getNumberOfCharsPossible(STRING_1, font, fontTableContentSize, (float) 50.0);
        String wrappedString = STRING_1.substring(0, numberOfChars);
        float wrappedStringLength = pdfConverter.textLength(wrappedString + "-", font, fontTableContentSize);

        assertTrue(wrappedStringLength <= (float) 50.0);
    }

    @Test
    public void textLength_validString_equalsTotalLengthOfEachCharacter() throws IOException {
        float totalLength = 0;
        for(int i = 0; i < STRING_1.length(); i++) {
            totalLength += font.getStringWidth(String.valueOf(STRING_1.charAt(i))) / 1000 * fontTableContentSize;
        }

        float totalExpectedLength = pdfConverter.textLength(STRING_1, font, fontTableContentSize);
        assertTrue(Math.abs(totalLength - totalExpectedLength) < 0.01);
    }

    @Test
    public void textHeightPlusTwoMargins_validString_equalsTwoTimesHalfTextHeightPlusMargin() throws IOException {
        float halfHeight = (font.getFontDescriptor().getCapHeight() / 1000 * fontTableContentSize) / 2 + this.margin;
        float expectedHeight = (font.getFontDescriptor().getCapHeight() / 1000 * fontTableContentSize) + 2
                * this.margin;
        assertTrue(Math.abs(halfHeight * 2 - expectedHeight) < 0.01);
    }

}
