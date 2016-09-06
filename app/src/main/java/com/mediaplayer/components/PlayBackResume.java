package com.mediaplayer.components;


import com.mediaplayer.services.FileService;
import com.mediaplayer.variables.CommonArgs;

import java.io.File;

public class PlayBackResume {

    public void resumeFrom(String filePath) {
        FileService objFileService = new FileService();
        String filename = new File(filePath).getName() + ".txt";
        StringBuilder text = objFileService.readFile(filename);
        if (text == null) {
            objFileService.writeFile("0", filename); // content will be like time(in msec)
            return;
        }
        if (!text.equals("0")) {
            new PopUpDialog().showAlertDialog(text);
        }
    }

    public void setResumePoint(String filePath, int timeInMs) {
        FileService objFileService = new FileService();
        String filename = new File(filePath).getName() + ".txt";
        objFileService.writeFile(Integer.toString(timeInMs), filename); // content will be like time(in msec)
    }

//    public void resumePlayBackAuto(String filePath) {
//        FileService objFileService = new FileService();
//        String filename = new File(filePath).getName() + ".txt";
//        StringBuilder text = objFileService.readFile(filename);
//        if (text == null) {
//            return;
//        }
//        if (CommonArgs.mediaPlayer != null)
//            CommonArgs.mediaPlayer.seekTo(Integer.parseInt(text.toString().trim()));
//    }
}
