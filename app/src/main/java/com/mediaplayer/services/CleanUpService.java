package com.mediaplayer.services;


import android.content.Context;

import com.mediaplayer.variables.Config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CleanUpService {

    public static void deleteTempPlayBack(List<String> videoPath, Context ctx) {
        List<String> filesToDelete = new ArrayList<>();
        File internalFile[] = ctx.getApplicationContext().getFilesDir().listFiles();
        for (int i = 0; i < internalFile.length; i++) {
            String internalFileName = internalFile[i].getName();
            Boolean fileFound = false;
            for (int j = 0; j < videoPath.size(); j++) {
                String videoFile = new File(videoPath.get(j)).getName() + ".txt";
                if (internalFileName.equals(videoFile)) {
                    fileFound = true;
                    break;
                }
            }
            if (!fileFound) {
                filesToDelete.add(internalFileName);
            }
        }
        for (int k = 0; k < filesToDelete.size(); k++) {
            File file = new File(ctx.getApplicationContext().getFilesDir(), filesToDelete.get(k));
            boolean deleted = file.delete();
            if (!deleted) {
                System.out.println("Couldn't delete:::: " + filesToDelete.get(k));
            } else {
                System.out.println("deleted File:::: " + filesToDelete.get(k));
            }
        }
    }

    public static void deleteThumbnail(List<String> videoPath) {
        List<String> filesToDelete = new ArrayList<>();
        File imageFile[] = new File(Config.baseThumbPath + "/.dthumb").listFiles();
        if (imageFile==null){
            System.out.println("Found no list of items...");
            return;
        }
        for (int i = 0; i < imageFile.length; i++) {
            String thumbFileName = imageFile[i].getName();
            Boolean fileFound = false;
            for (int j = 0; j < videoPath.size(); j++) {
                String videoFile = new File(videoPath.get(j)).getName() + ".bmp";
                if (thumbFileName.equals(videoFile)) {
                    fileFound = true;
                    break;
                }
            }
            if (!fileFound) {
                filesToDelete.add(thumbFileName);
            }
        }
        for (int k = 0; k < filesToDelete.size(); k++) {
            File file = new File(Config.baseThumbPath + "/.dthumb", filesToDelete.get(k));
            boolean deleted = file.delete();
            if (!deleted) {
                System.out.println("Couldn't delete:::: " + filesToDelete.get(k));
            } else {
                System.out.println("deleted File:::: " + filesToDelete.get(k));
            }
        }
    }

}
