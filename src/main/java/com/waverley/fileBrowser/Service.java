package com.waverley.fileBrowser;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.swing.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Andrey on 5/29/2017.
 */
@Transactional
@Component
public class Service {

    @Autowired
    Toolses toolses;
    @Autowired
    RemouteDAO remouteDAO;
    @Autowired
    LocalDAO localDAO;

    private String remouteURL = "smb://DESKTOP-N3GMKA8/Shared/";

    public void uploadFile(ArrayList <MultipartFile> files)  {

        for(int i = 0; i<files.size(); i++) {
            MultipartFile file = files.get(i);
            byte[] bytes = new byte[0];
            try {
                bytes = file.getBytes();
                String fileName = file.getOriginalFilename();
                remouteDAO.createImage(bytes, fileName, remouteURL);
                localDAO.createImagePreview(bytes, fileName, remouteURL);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public void downloadFile() {

    }

    public void renameFile() {

    }


    public void readOpenFile() {


    }

    public void createNewFolder(String nameFolder, String urlDirectoryForFolder) {

            remouteDAO.smbCreateFolder(nameFolder, urlDirectoryForFolder);
            localDAO.createFolderLocal(nameFolder, urlDirectoryForFolder);
    }

    public void copyFile() {

    }

    public void moveFile() {

    }

    public void deleteFile  () {

    }
}
