package com.mediaplayer.components;


import android.database.Cursor;
import android.os.Handler;
import android.provider.MediaStore;

import com.mediaplayer.services.FileService;
import com.mediaplayer.variables.CommonArgs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PlayBackResume {

    public String getVideoDuration(String filename) {
        String[] columns = {MediaStore.Video.VideoColumns.DURATION};
        String[] whereVal = {filename};
        Cursor cursor = CommonArgs.playFileCtx.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, columns, MediaStore.MediaColumns.DATA + "=?" + "", whereVal, null);
        String strDuration = null;
        if (cursor.moveToFirst()) {
            strDuration = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DURATION));
        }
        cursor.close();
        return strDuration;
    }

    public void resumeFrom(final String filePath) {
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
                    if (!getVideoDuration(filePath).equals(text.toString().trim()) && getVideoDuration(filePath) != null)
                        new PopUpDialog().showResumeDialog(text);
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
