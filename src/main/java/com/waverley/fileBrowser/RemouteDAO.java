package com.waverley.fileBrowser;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by Andrey on 6/8/2017.
 */
@Transactional
@Component
public class RemouteDAO {



    Toolses toolses = new Toolses();

    public SmbFile smbGetFilesFromFolder(String path){

        String user = "user1";
        String pass = "user1";

        //String path = "smb://DESKTOP-N3GMKA8/Shared/";

        NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication("", user, pass);

        SmbFile smbFile = null;
        try {
            smbFile = new SmbFile(path, auth);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return smbFile;
    }

    //get all files and folders from server throw smb protocol
    public ArrayList<SmbFile> smbGetAllFilesFoldersFromFolder(ArrayList<SmbFile> smbFilesList, SmbFile smbFile) throws ParseException, IOException {
        SmbFile[] smbFiles = smbFile.listFiles();

        for(int i = 0; i < smbFiles.length; ++i) {
            if(!smbFiles[i].getName().contains(".ini")) {
                smbFilesList.add(smbFiles[i]);
            }

            if(smbFiles[i].isDirectory()) {
                ArrayList smbFilesList2 = new ArrayList();
                smbFilesList.addAll(this.smbGetAllFilesFoldersFromFolder(smbFilesList2, smbFiles[i]));
            }
        }

        return smbFilesList;
    }

    public void smbCreateFolder(String foldername, String urlDirectoryForFolder){
       // String user = "user1";
      //  String pass = "user1";

        String path = urlDirectoryForFolder + "/" + foldername;

    //    NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication("", user, pass);

        SmbFile smbFile = null;
        try {
            smbFile = smbGetFilesFromFolder(path); // new SmbFile(path, auth);
            smbFile.mkdir();

        } catch (SmbException e) {
            e.printStackTrace();
        }

    }

    public void createImage(byte [] bytes, String fileName, String urlDirectoryForFolder ) throws ParseException, IOException {

        SmbFileOutputStream smbFileDestOutputStream = smbFileDest(fileName);
        toolses.writingNewFile(bytes, smbFileDestOutputStream);

    }

    public SmbFileOutputStream smbFileDest(String fileName) throws ParseException, IOException {


        String path = "smb://DESKTOP-N3GMKA8/Shared/" +fileName;


        SmbFileOutputStream smbFileDestOutputStream = new SmbFileOutputStream(smbGetFilesFromFolder(path));

        return smbFileDestOutputStream;

    }



}
