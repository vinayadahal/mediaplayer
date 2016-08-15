package com.mediaplayer.services;


import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;

import com.mediaplayer.components.MessageAlert;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileSearch {

    public Context ctx;
    public List<String> FileList = new ArrayList<>();
    public List<String> video_path = new ArrayList<>();
    public List<String> thumb = new ArrayList<>();

    public void searchVideoFile(File folder_name) {
        String extension = ".mp4";
        File listFile[] = folder_name.listFiles();
        if (listFile == null) {
            new MessageAlert().showToast("Unable to read directory content", ctx);
            return;
        }
        for (int i = 0; i < listFile.length; i++) {
            if (listFile[i].isDirectory()) {
                if (listFile[i].isHidden()) {
                    continue;
                }
                searchVideoFile(listFile[i]);
            } else {
                if (listFile[i].getName().endsWith(extension)) {
                    String imgFile = checkThumb(folder_name.toString(), listFile[i].getName());
                    if (imgFile == null) {
                        continue;
                    }
                    FileList.add(listFile[i].getName());
                    thumb.add(imgFile);
                    video_path.add(folder_name + "/");
                }
            }
        }
    }

    public String checkThumb(String folder_name, String filename) {
        String thumb_location = folder_name + "/.thumb";
        String full_filename = filename + ".bmp";
        File folder = new File(thumb_location);
        if (!folder.exists()) {
            if (!folder.mkdir()) {
                new MessageAlert().showToast("Failed to create thumbnail directory!", ctx);
            }
            return null;
        }
        if (!new File(thumb_location + "/" + full_filename).exists()) {
            return makeThumb(folder_name + "/" + filename, thumb_location + "/" + full_filename);
        }
        return thumb_location + "/" + full_filename;
    }

    public String makeThumb(String video_path, String image_path) {
        final Bitmap bmp_thumb = ThumbnailUtils.createVideoThumbnail(video_path, MediaStore.Images.Thumbnails.MICRO_KIND);
        if (bmp_thumb == null) {
            return null;
        }
        if (!new File(image_path).exists()) {
            File imgFile = new File(image_path);
            writeImgFile(imgFile, bmp_thumb);
        }
        return image_path;
    }

    public void writeImgFile(File imgFile, Bitmap bmp_thumb) {
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
}
