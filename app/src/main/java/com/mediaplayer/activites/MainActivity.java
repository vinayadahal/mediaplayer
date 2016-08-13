package com.mediaplayer.activites;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.mediaplayer.R;
import com.mediaplayer.services.MobileArrayAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        walkdir();
//        Context.MODE
        String rawExternalStorage = System.getenv("EXTERNAL_STORAGE");
        String rawSecondaryStoragesStr = System.getenv("SECONDARY_STORAGE");
        System.out.println("External Storage::::: " + rawExternalStorage);
        System.out.println("Secondary Storage:::::" + rawSecondaryStoragesStr);
        SearchFile(new File(rawExternalStorage));
//        SearchFile(new File(rawSecondaryStoragesStr));

    }

    public void SearchFile(File dir) {
        String extension = ".mp4";
        File listFile[] = dir.listFiles();
        List<String> FileList = new ArrayList<>();
        List<Bitmap> thumb = new ArrayList<>();
        if (listFile == null) {
            return;
        }
        for (int i = 0; i < listFile.length; i++) {
            if (listFile[i].isDirectory()) {
                SearchFile(listFile[i]);
            } else {
                if (listFile[i].getName().endsWith(extension)) {
                    if (makeThumb(dir + "/" + listFile[i].getName()) == null) {
                        continue;
                    }
                    FileList.add(listFile[i].getName());
                    System.out.println("File Location::::: " + dir + "/" + listFile[i].getName());
                    thumb.add(makeThumb(dir + "/" + listFile[i].getName()));
                }
            }
        }

        ListView listView = new ListView(this);
        listView.setAdapter(new MobileArrayAdapter(this, FileList, thumb));
        LinearLayout lnrlayout = (LinearLayout) findViewById(R.id.lnrLayout);
        lnrlayout.addView(listView);
    }

    public Bitmap makeThumb(String path) {
        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Images.Thumbnails.MICRO_KIND);
        return thumb;
    }

//    public void generateThumbnail() {
//
////        ImageView iv = (ImageView) convertView.findViewById(R.id.imagePreview);
//        ImageView iv = new ImageView(this);
//        ContentResolver crThumb = getContentResolver();
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inSampleSize = 1;
//        Bitmap curThumb = MediaStore.Video.Thumbnails.getThumbnail(crThumb, id, MediaStore.Video.Thumbnails.MICRO_KIND, options);
//        iv.setImageBitmap(curThumb);
//    }

}
