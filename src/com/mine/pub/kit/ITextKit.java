package com.mine.pub.kit;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;


public class ITextKit {
//	public static final String DEST = "WebContent/output/headers.pdf";
//    public static final String HTML = "WebContent/templates/headers.html";
    public static final String FONT = "song.ttf";
//    public static void main(String[] args) throws IOException, DocumentException {
//        File file = new File(DEST);
//        file.getParentFile().mkdirs();
//        new ITextKit().createPdf(DEST);
//    }
 
    /**
     * Creates a PDF with the words "Hello World"
     * @param file
     * @throws IOException
     * @throws DocumentException
     */
    public static void createPdf(String file, String HTML) throws IOException, DocumentException {
        // step 1
        Document document = new Document();
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
        writer.setTagged();
        // step 3
        document.open();
        //加载字体
        XMLWorkerFontProvider fontProvider = new XMLWorkerFontProvider() {
            @Override
            public Font getFont(String fontname, String encoding, boolean embedded, float size, int style, BaseColor color) {
                //你的字体文件的位置
                //这里把所有字体都设置为宋体了,可以根据fontname的值设置字体
                Font font = null;
                try {
                     font = new Font(com.itextpdf.text.pdf.BaseFont.createFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
                    font.setStyle(style);
                    font.setColor(color);
                    if (size>0){
                        font.setSize(size);
                    }
                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return font;
            }
        };

        // step 4
        XMLWorkerHelper.getInstance().parseXHtml(writer, document,
                new FileInputStream(HTML), Charset.forName("UTF-8"), fontProvider);
        // step 5
        document.close();
    }
}
