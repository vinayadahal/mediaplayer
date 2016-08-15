package com.mediaplayer.activites;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.mediaplayer.R;
import com.mediaplayer.services.FileSearch;
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
        System.out.println("External Storage::::: " + rawExternalStorage);
        SearchFile(new File(rawExternalStorage));

    }

    public void SearchFile(File dir) {
        FileSearch objFileSearch = new FileSearch();
        objFileSearch.ctx = this;
        objFileSearch.searchVideoFile(dir);
        List<String> thumb = objFileSearch.thumb;
        final List<String> FileList = objFileSearch.FileList;
        final List<String> video_path = objFileSearch.video_path;
        if (thumb.size() != 0) {
            ListView listView = new ListView(this);
            listView.setAdapter(new MobileArrayAdapter(this, FileList, thumb));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String fileName = video_path.get(position) + FileList.get(position).toString();
                    Intent intent = new Intent(MainActivity.this, PlayFile.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("fileName", fileName); // value for another activities
                    bundle.putString("videoFileName", FileList.get(position));
                    intent.putExtras(bundle); // bundle saved as extras
                    startActivity(intent);
                }
            });
            LinearLayout lnrlayout = (LinearLayout) findViewById(R.id.lnrLayout);
            lnrlayout.addView(listView);
        }
    }
}
