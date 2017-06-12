package com.waverley.fileBrowser;

import jcifs.smb.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.stream.FileImageInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by Andrey on 6/8/2017.
 */
@Transactional
@Component
public class LocalDAO {
    String localURL = "H:/workspace/SharedLocal";
    Toolses toolses = new Toolses();

    //use recurtion get all files folders from local directory
    public ArrayList<File> getAllFilesFoldersFromPreviewFolder(String pathFolderWithPreview, ArrayList<File> filesList) throws ParseException, IOException {

        File file = new File(pathFolderWithPreview);
        File[] files = file.listFiles();
        for(int i=0; i<files.length; i++) {
            filesList.add(files[i]);
            if(files[i].isDirectory()){
                String pathFolderWithPreviewInside = pathFolderWithPreview+files[i].getName()+"/";

                ArrayList<File> filesList2 = new ArrayList<File>();
                filesList.addAll(getAllFilesFoldersFromPreviewFolder(pathFolderWithPreviewInside, filesList2));
            }
        }
        return filesList;
    }

    //create previews and folders in locale directories
    public void createFilesPreviewAndFolders(ArrayList<SmbFile> smbFileArrayList, String rootFolder) throws MalformedURLException, UnknownHostException, SmbException {
        // rootFolder = "H:/workspace/SharedLocal/";
        for(int i = 0; i<smbFileArrayList.size(); i++){

            //devide full url of the smbFile, for receivinig relatively way of the file
            String[] partesOftheSourceWay = smbFileArrayList.get(i).getCanonicalPath().split("Shared");
            //combine local root derectory with  elatively way of the file
            String filenameWithWay = rootFolder.concat(partesOftheSourceWay[1]);

            try {
                //create New File
                if(!smbFileArrayList.get(i).isDirectory() && (smbFileArrayList.get(i).getName().contains(".jpg")) ) {
                    new File(filenameWithWay).createNewFile();
                    SmbFileInputStream smbFileInputStream = new SmbFileInputStream(smbFileArrayList.get(i));
                    toolses.saveScaledImage(smbFileInputStream, filenameWithWay);
                }else if (smbFileArrayList.get(i).isDirectory()) {
                    new File(filenameWithWay).mkdir();
                }
            } catch (IOException e) {
                //create New Folder
                partesOftheSourceWay = filenameWithWay.split("/");
                String[] folderDestination = ArrayUtils.remove(partesOftheSourceWay, partesOftheSourceWay.length-1);
                String addressOfTheFile = StringUtils.join(folderDestination, "/");
                new File(addressOfTheFile).mkdir();
                //create New File again
                try {
                    new File(filenameWithWay).createNewFile();
                    SmbFileInputStream smbFileInputStream = new SmbFileInputStream(smbFileArrayList.get(i));
                    toolses.saveScaledImage(smbFileInputStream, filenameWithWay);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public void smartCheckCompareSmbFilesAndFilesPreview(ArrayList<SmbFile> smbFileList, ArrayList<File> fileList) throws MalformedURLException, UnknownHostException, SmbException {
        ArrayList<String> previewNames = new ArrayList<>();
        ArrayList<String> smbFileNames = new ArrayList<>();

        for (int i = 0; i < smbFileList.size(); i++) {
            String[] partesOfsmbFile = smbFileList.get(i).getCanonicalPath().split("Shared");
            smbFileNames.add(partesOfsmbFile[1]);
        }


        for (int i = 0; i < fileList.size(); i++) {
            String changeSlash = fileList.get(i).getPath().replace("\\", "/");
            String[] partesOftheFilepreview = changeSlash.split("SharedLocal");
            previewNames.add(partesOftheFilepreview[1]);
        }

        // Create Preview
        for (int i = 0; i < smbFileNames.size(); i++) {
            if(!previewNames.contains(smbFileNames.get(i))){
                ArrayList<SmbFile> smbFileForPreview = new ArrayList<>();
                smbFileForPreview.add(smbFileList.get(i));
                createFilesPreviewAndFolders(smbFileForPreview, "H:/workspace/SharedLocal");
            }
        }

        // Delete the preview
        for (int i = 0; i < previewNames.size(); i++) {
            if(!smbFileNames.contains(previewNames.get(i))){
                fileList.get(i).delete();
            }
        }
    }

  //  Creating Preview
    public void createImagePreview(byte [] bytes, String fileName, String remouteURL) throws ParseException, IOException {



        String[] partesOftheSourceWay = remouteURL.split("Shared");
        //combine local root derectory with  elatively way of the file
        if(partesOftheSourceWay[1]!=null) {
            String filenameWithWay = localURL.concat(partesOftheSourceWay[1]).concat("/" + fileName);
        }
        String filenameWithWay = localURL.concat("/" + fileName);
        new File(filenameWithWay).createNewFile();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        byte [] bytesPreview = toolses.saveScaledImage(byteArrayInputStream, filenameWithWay);

    }

    public void createFolderLocal(String nameFolder, String urlDirectoryForFolder){



        String[] partesOftheSourceWay = urlDirectoryForFolder.split("Shared");
        //combine local root derectory with  elatively way of the file
        if(partesOftheSourceWay[1]!=null) {
            String folderWithWay = localURL.concat(partesOftheSourceWay[1]).concat("/" + nameFolder);
        }
        String folderWithWay = localURL.concat("/" + nameFolder);
        new File(folderWithWay).mkdir();
    };

    public byte[] showOneFile() throws ParseException, IOException {

        File file = new File("H:/workspace/SharedLocal/test2.jpg");

        Path path = Paths.get("H:/workspace/SharedLocal/test2.jpg");
        byte[] data = Files.readAllBytes(path);
        return data;
    }
}
