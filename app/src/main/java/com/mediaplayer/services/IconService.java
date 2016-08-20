package com.mediaplayer.services;


import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;

import com.mediaplayer.components.MessageAlert;
import com.mediaplayer.variables.Config;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class IconService {

    public Context ctx;

    public String checkThumb(String videoFile) {
        File fileFolder = new File(videoFile);
        String thumbDir = Config.baseThumbPath + "/.dthumb";
        String imageFile = fileFolder.getName() + ".bmp";
        File folder = new File(thumbDir);
        if (!folder.exists()) {
            if (!folder.mkdir()) {
                new MessageAlert().showToast("Failed to create thumbnail directory!", ctx);
            }
            return null;
        }
        if (!new File(thumbDir + "/" + imageFile).exists()) {
            System.out.println("Creating thumbnail ----->");
            return makeThumb(videoFile, thumbDir + "/" + imageFile);
        }
        return thumbDir + "/" + imageFile;
    }

    public String makeThumb(String videoFile, String thumbnailFile) {
        final Bitmap bmp_thumb = ThumbnailUtils.createVideoThumbnail(videoFile, MediaStore.Images.Thumbnails.MICRO_KIND);
        if (bmp_thumb == null) {
            return null;
        }
        if (!new File(thumbnailFile).exists()) {
            File thumbLocation = new File(thumbnailFile);
            File thumbFile = new File(thumbLocation.getParent(), thumbLocation.getName());
            writeImgFile(thumbFile, bmp_thumb);
        }
        return thumbnailFile;
    }

    public void writeImgFile(final File imgFile, final Bitmap bmp_thumb) {
        Thread th = new Thread() {
            @Override
            public void run() {
                try {
                    imgFile.createNewFile();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bmp_thumb.compress(Bitmap.CompressFormat.PNG, 100, bos);
                    byte[] bmp_byte = bos.toByteArray();
                    FileOutputStream fos = new FileOutputStream(imgFile);
                    fos.write(bmp_byte);
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        th.start();
    }

}
