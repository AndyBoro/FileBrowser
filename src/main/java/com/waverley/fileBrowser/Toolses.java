package com.waverley.fileBrowser;

import jcifs.smb.*;
import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.*;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class Toolses {


    @Autowired
    PropertyHolder propertyHolder;

    public static void main(String [] args) throws IOException, ParseException {

      PropertyHolder propertyHolder = new PropertyHolder();
        System.out.println("propertyHolder.getLocalURL() " + propertyHolder.getRemouteURL());
        ArrayList<SmbFile> smbFileList = new ArrayList<>();
        //get all files and folders from server throw smb protocol
        LocalDAO localDAO = new LocalDAO();
        RemouteDAO remouteDAO = new RemouteDAO();
     //   smbFileList = remouteDAO.smbGetAllFilesFoldersFromFolder(smbFileList, remouteDAO.smbGetFilesFromFolder());
        System.out.println(smbFileList.size());

        String pathFolderWithPreview = "H:/workspace/SharedLocal/";
  //    localDAO.createFilesPreviewAndFolders(smbFileList, pathFolderWithPreview);

        ArrayList<File> fileList = new ArrayList<>();
//use recurtion get all files folders from local directory
        fileList = localDAO.getAllFilesFoldersFromPreviewFolder(pathFolderWithPreview, fileList);
        System.out.println("fileList.size() " + fileList.size());

    //    toolses.smartCheckCompareSmbFilesAndFilesPreview(smbFileList, fileList);

    }

//Copy file and create new file. I left it only for example
    private void copy()throws ParseException, IOException {
        String user = "user1";
        String pass = "user1";
        NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication("", user, pass);
        String sharedFolder = "Shared";

        String pathSour = "smb://DESKTOP-N3GMKA8/" + sharedFolder + "/test.jpg";

        String pathDest = "smb://DESKTOP-N3GMKA8/" + sharedFolder + "/test2.jpg";

        NtlmPasswordAuthentication auth1 = new NtlmPasswordAuthentication("", user, pass);
        //read file
        SmbFile smbFromFile = new SmbFile(pathSour, auth);
        //create new file
        SmbFile smbToFile = new SmbFile(pathDest, auth);
        //copy first file in another file
        smbFromFile.copyTo(smbToFile);
        //way to write information in new file
        SmbFileOutputStream smbfos = new SmbFileOutputStream(smbToFile);
        smbfos.write("testing....and writing to a file".getBytes());

        String des = "smb://DESKTOP-N3GMKA8/" + sharedFolder + "/test5.jpg";
        SmbFile smbToFileDes = new SmbFile(des, auth);

        //парсим соурсе smb файл в входящий поток
        SmbFileInputStream smbFileSource = new SmbFileInputStream(smbFromFile);
       // byte[] arrByteSmbFileSource = saveScaledImage(smbFileSource);

        byte[] fileBytes = IOUtils.toByteArray(smbFileSource);
        //парсим dest smb файл в исходящий поток
        SmbFileOutputStream smbFileDest = new SmbFileOutputStream(smbToFileDes);

        smbFileDest.write(fileBytes);

        smbFileDest.flush();
        smbFileDest.close();

    }

    //Create - preview
    public byte[] saveScaledImage(InputStream in, String directoryWithFilename){
        String outputFile = directoryWithFilename;

        try {

            ByteArrayOutputStream os = new ByteArrayOutputStream();

            BufferedImage sourceImage = ImageIO.read( new BufferedInputStream(in));
            int width = sourceImage.getWidth();
            int height = sourceImage.getHeight();

            if(width>height){
                float extraSize=    height-100;
                float percentHight = (extraSize/height)*100;
                float percentWidth = width - ((width/100)*percentHight);
                BufferedImage img = new BufferedImage((int)percentWidth, 100, BufferedImage.TYPE_INT_RGB);
                Image scaledImage = sourceImage.getScaledInstance((int)percentWidth, 100, Image.SCALE_SMOOTH);
                img.createGraphics().drawImage(scaledImage, 0, 0, null);
                BufferedImage img2 = new BufferedImage(100, 100 ,BufferedImage.TYPE_INT_RGB);
                img2 = img.getSubimage((int)((percentWidth-100)/2), 0, 100, 100);
                ImageIO.write(img2, "jpg", os);
                ImageIO.write(img2, "jpg", new File(outputFile)); //change path where you want it saved

                byte[] is = os.toByteArray();

                return is;
            }else{
                float extraSize=    width-100;
                float percentWidth = (extraSize/width)*100;
                float  percentHight = height - ((height/100)*percentWidth);
                BufferedImage img = new BufferedImage(100, (int)percentHight, BufferedImage.TYPE_INT_RGB);
                Image scaledImage = sourceImage.getScaledInstance(100,(int)percentHight, Image.SCALE_SMOOTH);
                img.createGraphics().drawImage(scaledImage, 0, 0, null);
                BufferedImage img2 = new BufferedImage(100, 100 ,BufferedImage.TYPE_INT_RGB);
                img2 = img.getSubimage(0, (int)((percentHight-100)/2), 100, 100);
                ImageIO.write(img2, "jpg", os);
                ImageIO.write(img2, "jpg", new File(outputFile));
                byte[] is = os.toByteArray();
                return is;
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public void writingNewFile(byte [] bytes,SmbFileOutputStream smbFileDestOutputStream) throws ParseException, IOException {

        smbFileDestOutputStream.write(bytes);
        smbFileDestOutputStream.flush();
        smbFileDestOutputStream.close();

    }

}
