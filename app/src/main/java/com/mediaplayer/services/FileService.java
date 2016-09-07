package com.mediaplayer.services;

import com.mediaplayer.variables.CommonArgs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class FileService {

    StringBuilder text = new StringBuilder();

    public StringBuilder readFile(String filename) {
        System.out.println("Reading File::::::");
        File file = new File(CommonArgs.playFileCtx.getApplicationContext().getFilesDir() + "/" + filename);
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
            return text;
        } catch (IOException e) {
            System.out.println("exception ----------------> " + e);
            return null;
        }
    }

    public boolean writeFile(String data, String filename) {
        System.out.println("Writing File::::");
        FileOutputStream outputStream;
        try {
            outputStream = CommonArgs.playFileCtx.openFileOutput(filename, CommonArgs.playFileCtx.MODE_PRIVATE);
            outputStream.write(data.getBytes());
            outputStream.close();
            return true;
        } catch (Exception e) {
            System.out.println("WriteToFile failed >>>>>" + e);
            return false;
        }
    }

}
