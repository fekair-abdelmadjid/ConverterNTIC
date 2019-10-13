package com.example.web;

import com.example.service.Converte;
import com.example.service.DownloadFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UploadFileController {

    @Autowired
    private DownloadFile downloadFile;

    @Autowired
    private Converte converte;

    private static String uploadDirectory = System.getProperty("user.dir") + "/uploads";


    @RequestMapping(value = "/pdffromtxt/index", method = RequestMethod.GET)
    public String showpdffromtxt() {

        return "PDFFromTxt";
    }

    @RequestMapping(value = "/pdffromimage/index", method = RequestMethod.GET)
    public String showpdffromimage() {

        return "PDFFromImage";
    }

    @RequestMapping(value = "/imagefrompdf/index", method = RequestMethod.GET)
    public String showimagefrompdf() {

        return "ImageFromPDF";
    }

    @RequestMapping(value = "/pdffromtxt/upload", method = RequestMethod.POST)
    @ResponseBody
    public void pdffromtxtConverte(Model model, @RequestParam("files") MultipartFile[] files, HttpServletResponse response) {
        String fileNames = this.upload(model, files, response);
        converte.generatePDFFromTxt(fileNames);
        downloadFile.Download(fileNames + ".pdf", uploadDirectory, response);
    }

    @RequestMapping(value = "/pdffromimage/upload", method = RequestMethod.POST)
    @ResponseBody
    public void imagefrompdfConverte(Model model, @RequestParam("files") MultipartFile[] files, HttpServletResponse response){
        String fileNames = this.upload(model, files, response);
        converte.generatePDFFromImage(fileNames, "png");
        downloadFile.Download(fileNames + ".pdf", uploadDirectory, response);
    }


    @RequestMapping(value = "/imagefrompdf/upload", method = RequestMethod.POST)
    @ResponseBody
    public void pdffromimageConverte(Model model, @RequestParam("files") MultipartFile[] files, HttpServletResponse response){
        String fileNames = this.upload(model, files, response);
        converte.generateImageFromPDF(fileNames, "jpg");
        downloadFile.Download(fileNames + ".jpg", uploadDirectory, response);
    }


    public String upload(Model model, @RequestParam("files") MultipartFile[] files, HttpServletResponse response) {
        StringBuilder fileNames = new StringBuilder();
        for (MultipartFile file : files) {
            Path fileNameAndPath = Paths.get(uploadDirectory, file.getOriginalFilename());
            fileNames.append(file.getOriginalFilename() + " ");
            try {
                Files.write(fileNameAndPath, file.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        model.addAttribute("msg", "Successfully uploaded files " + fileNames.toString());
        return fileNames.toString();
    }


    public void deleteFile(String filename) {
        try {
            File file = new File(uploadDirectory + "/" + filename);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
