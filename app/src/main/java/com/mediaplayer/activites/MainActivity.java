package com.mediaplayer.activites;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.mediaplayer.R;
import com.mediaplayer.services.IconService;
import com.mediaplayer.services.MobileArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<Long> fileSize = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageButton backImgBtn = (ImageButton) findViewById(R.id.back_btn);
        backImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        createVideoList(getVideoFiles(), getVideoThumbnail(), getVideoDuration(), fileSize);
    }

    public List<String> getVideoFiles() {
        String[] columns = {MediaStore.MediaColumns.DATA,
                MediaStore.MediaColumns._ID,
                MediaStore.MediaColumns.SIZE};
        Cursor cursor = this.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, columns, null, null, MediaStore.MediaColumns.TITLE);
        List<String> videoFileList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                videoFileList.add(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)));
                fileSize.add(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE)));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return videoFileList;
    }

    public List<String> getVideoThumbnail() {
        final List<String> thumbLocation = new ArrayList<>();
        for (final String filepath : getVideoFiles()) {
            final IconService objIconService = new IconService();
            objIconService.ctx = this;
            thumbLocation.add(objIconService.checkThumb(filepath));
        }
        return thumbLocation;
    }

    public List<Long> getVideoDuration() {
        String[] columns = {MediaStore.Video.VideoColumns.DURATION};
        Cursor cursor = this.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, columns, null, null, MediaStore.MediaColumns.TITLE);
        List<Long> videoDuration = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                String strDuration = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DURATION));
                long duration = Long.parseLong(strDuration);
                videoDuration.add(duration);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return videoDuration;
    }

    public void createVideoList(final List<String> videoFiles, List<String> videoThumbnail, List<Long> duration, List<Long> FileSize) {
        ListView listView = new ListView(this);
        listView.setAdapter(new MobileArrayAdapter(this, videoFiles, videoThumbnail, duration, FileSize));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String fileName = videoFiles.get(position);
                Intent intent = new Intent(MainActivity.this, PlayFile.class);
                Bundle bundle = new Bundle();
                bundle.putString("fileName", fileName); // value for another activities
                bundle.putStringArrayList("allVideoPath", (ArrayList<String>) videoFiles);
                intent.putExtras(bundle); // bundle saved as extras
                startActivity(intent);
            }
        });
        LinearLayout lnrlayout = (LinearLayout) findViewById(R.id.lnrLayout);
        lnrlayout.addView(listView);
    }

}
