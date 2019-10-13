package com.example.service;

import com.example.web.UploadFileController;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;


@Service
public class Converte {

    private static String uploadDirectory = System.getProperty("user.dir") + "/uploads";

    public HttpServletResponse response;

    @Autowired
    private UploadFileController uploadFileController;

    @Autowired
    private DownloadFile downloadFile;


    Model model;

    //HTML to PDF
    public void generatePDFFromHTML(String filename) {
        try {
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(uploadDirectory + filename + ".pdf"));
            document.open();
            XMLWorkerHelper.getInstance().parseXHtml(writer, document, new FileInputStream(filename));
            document.close();
        } catch (Exception e) {

        }
    }


    //PDF to Image
    public void generateImageFromPDF(String filename, String extension) {
        try {
            PDDocument document = PDDocument.load(new File(uploadDirectory + "/" + filename));
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            for (int page = 0; page < document.getNumberOfPages(); ++page) {
                BufferedImage bim = pdfRenderer.renderImageWithDPI(
                        page, 300, ImageType.RGB);
                ImageIOUtil.writeImage(
                        bim, String.format(uploadDirectory + "/" + filename, page + 1, extension), 300);
            }
            document.close();
        } catch (Exception e) {

        }
    }

    // Image to PDF
    @ResponseBody
    public void generatePDFFromImage(String filename, String extension) {
        try {
            Document document = new Document();
            String input = uploadDirectory + "/" + filename + "." + extension;
            String output = uploadDirectory + "/" + filename + ".pdf";
            FileOutputStream fos = new FileOutputStream(output);

            PdfWriter writer = PdfWriter.getInstance(document, fos);
            writer.open();
            document.open();
            document.add(Image.getInstance((new URL(input))));
            document.close();
            writer.close();
        } catch (Exception e) {

        }
    }

    @ResponseBody
    public void generatePDFFromTxt(String filename) {
        try {
            Document pdfDoc = new Document(PageSize.A4);
            PdfWriter.getInstance(pdfDoc, new FileOutputStream(uploadDirectory + "/" + filename + ".pdf"))
                    .setPdfVersion(PdfWriter.PDF_VERSION_1_7);
            pdfDoc.open();
            Font myfont = new Font();
            myfont.setStyle(Font.NORMAL);
            myfont.setSize(11);
            pdfDoc.add(new Paragraph("\n"));
            BufferedReader br = new BufferedReader(new FileReader(uploadDirectory + "/" + filename));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                Paragraph para = new Paragraph(strLine + "\n", myfont);
                para.setAlignment(Element.ALIGN_JUSTIFIED);
                pdfDoc.add(para);
            }
            pdfDoc.close();
            br.close();
            uploadFileController.deleteFile(filename);
            downloadFile.Download(filename + ".pdf", uploadDirectory, response);

        } catch (Exception e) {

        }
    }


}
