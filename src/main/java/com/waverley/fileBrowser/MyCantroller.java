package com.waverley.fileBrowser;

import org.apache.commons.fileupload.FileUploadException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.validation.Valid;

import java.io.*;
import java.text.ParseException;
import java.util.*;

/**
 * Created by Andrey on 5/25/2017.
 */



@Controller
public class MyCantroller {

    private String remouteURL = "smb://DESKTOP-N3GMKA8/Shared/";

    @Autowired
    Service service;
    @Autowired
    PropertyHolder propertyHolder;
    @Autowired
    LocalDAO localDAO;

    @RequestMapping(value = "/uploa", method = RequestMethod.POST)
    @ResponseBody
    public void uploadFile(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException, FileUploadException {


    }

    @RequestMapping(value = "/show", method = RequestMethod.POST)
    @ResponseBody
    public void showFile(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException, FileUploadException {
        response.setContentType("image/jpg");
        response.getOutputStream().write(localDAO.showOneFile());


    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public void uploadFirmware(@RequestParam(value = "file") ArrayList<MultipartFile> files) {

        System.out.println("files.size( " + files.size());

        service.uploadFile(files);

    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    @ResponseBody
    public void downloadFile(HttpServletRequest request, HttpServletResponse response) {

    }

    @RequestMapping(value = "/rename", method = RequestMethod.GET)
    @ResponseBody
    public void renameFile(HttpServletRequest request, HttpServletResponse response) {


}

    @RequestMapping(value = "/readOpen", method = RequestMethod.GET)
    @ResponseBody
    public void readOpenFile(HttpServletRequest request, HttpServletResponse response) {

    }

    @RequestMapping(value = "/createFolder", method = RequestMethod.POST)
    @ResponseBody
    public void createNewFolder( String nameFodler) {

        service.createNewFolder(nameFodler, remouteURL);

    }
    @RequestMapping(value = "/copy", method = RequestMethod.GET)
    @ResponseBody
    public void copyFile(HttpServletRequest request, HttpServletResponse response) {

    }

    @RequestMapping(value = "/move", method = RequestMethod.GET)
    @ResponseBody
    public void moveFile(HttpServletRequest request, HttpServletResponse response) {

    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    public void deleteFile  (HttpServletRequest request, HttpServletResponse response) {

    }

}
