package com.mediaplayer.activites;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.mediaplayer.R;
import com.mediaplayer.services.MobileArrayAdapter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String rawExternalStorage = System.getenv("EXTERNAL_STORAGE");
//        String rawSecondaryStoragesStr = System.getenv("SECONDARY_STORAGE");
//        String rawSecondaryStoragesStr = System.getenv("EXTERNAL_SDCARD_STORAGE");

        System.out.println("External Storage::::: " + rawExternalStorage);
//        System.out.println("Secondary Storage:::::" + Environment.getExternalStorageState());
        SearchFile(new File(rawExternalStorage));
//        SearchFile(new File(rawSecondaryStoragesStr));

    }

    public void SearchFile(File dir) {
        String extension = ".mp4";
        File listFile[] = dir.listFiles();
        final List<String> FileList = new ArrayList<>();
        List<String> thumb = new ArrayList<>();
        final List<String> dir_name = new ArrayList<>();
        if (listFile == null) {
            return;
        }
        for (int i = 0; i < listFile.length; i++) {
            if (listFile[i].isDirectory()) {
                if (listFile[i].isHidden()) {
                    continue;
                }
                SearchFile(listFile[i]);
            } else {
                if (listFile[i].getName().endsWith(extension)) {
                    FileList.add(listFile[i].getName());
                    System.out.println("File Location::::: " + dir + "/" + listFile[i].getName());
                    String imgFile = checkThumb(dir.toString(), listFile[i].getName());
                    thumb.add(imgFile);
                    dir_name.add(dir + "/");
                }
            }
        }
        ListView listView = new ListView(this);
        listView.setAdapter(new MobileArrayAdapter(this, FileList, thumb));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String fileName = dir_name.get(position) + FileList.get(position).toString();
//                System.out.println("File name after click::::::::: " );
                Intent intent = new Intent(MainActivity.this, PlayFile.class);
                Bundle bundle = new Bundle();
                bundle.putString("fileName", fileName); // value for another activities
                intent.putExtras(bundle); // bundle saved as extras
                startActivity(intent);
            }
        });

        LinearLayout lnrlayout = (LinearLayout) findViewById(R.id.lnrLayout);
        lnrlayout.addView(listView);
    }

    public String checkThumb(String dir, String filename) {
        String thumb_location = dir + "/.thumb";
        String full_filename = filename + ".bmp";
        File folder = new File(thumb_location);
        if (!folder.exists()) {
            if (!folder.mkdir()) {
                System.out.println("Failed to create directory!");
            }
            return null;
        }
        System.out.println("Directory is created!");
        if (!new File(thumb_location + "/" + full_filename).exists()) {
            System.out.println("no thumbnail. Creating new.......");
            return makeThumb(dir + "/" + filename, thumb_location + "/" + full_filename);
        }
        return thumb_location + "/" + full_filename;
    }

    public String makeThumb(String video_path, String image_path) {
        final Bitmap thumb = ThumbnailUtils.createVideoThumbnail(video_path, MediaStore.Images.Thumbnails.MICRO_KIND);
        if (thumb == null) {
            return null;
        }
        if (!new File(image_path).exists()) {
            System.out.println("creating bmp:::::: " + image_path);
            File imgFile = new File(image_path);
            try {
                imgFile.createNewFile();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                thumb.compress(Bitmap.CompressFormat.PNG, 100, bos);
                byte[] bitmapdata = bos.toByteArray();
                FileOutputStream fos = new FileOutputStream(imgFile);
                fos.write(bitmapdata);
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("returning make img " + image_path);
        return image_path;
    }

    public Bitmap makeThumb(String video_path) {
        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(video_path, MediaStore.Images.Thumbnails.MICRO_KIND);
        return thumb;
    }
}
