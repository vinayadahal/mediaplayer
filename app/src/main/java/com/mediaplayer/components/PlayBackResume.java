package com.mediaplayer.components;


import android.os.Handler;

import com.mediaplayer.services.FileService;
import com.mediaplayer.variables.CommonArgs;

import java.io.File;

public class PlayBackResume {

    public void resumeFrom(String filePath) {
        final FileService objFileService = new FileService();
        final String filename = new File(filePath).getName() + ".txt";
        final StringBuilder text = objFileService.readFile(filename);
        if (text == null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    objFileService.writeFile("0", filename); // content will be like time(in msec)
                }
            }, 2000);
            return;
        }
        if (!text.equals("0")) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (CommonArgs.duration != Integer.parseInt(text.toString().trim()))
                        new PopUpDialog().showCustomAlertDialog(text);
                }
            }, 500);
        }
    }

    public void setResumePoint(final String filePath, final int timeInMs) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FileService objFileService = new FileService();
                String filename = new File(filePath).getName() + ".txt";
                objFileService.writeFile(Integer.toString(timeInMs), filename); // content will be like time(in msec)
            }
        }, 1000);
    }

    public void resumePlayBackAuto(String filePath) {
        FileService objFileService = new FileService();
        String filename = new File(filePath).getName() + ".txt";
        StringBuilder text = objFileService.readFile(filename);
        if (text == null) {
            return;
        }
        if (CommonArgs.mediaPlayer != null)
            CommonArgs.mediaPlayer.seekTo(Integer.parseInt(text.toString().trim()));
    }
}
