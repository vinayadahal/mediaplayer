package com.mediaplayer.activites;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.mediaplayer.R;
import com.mediaplayer.services.CleanUpService;
import com.mediaplayer.services.IconService;
import com.mediaplayer.services.MobileArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Long> fileSize = new ArrayList<>();
    private Toolbar toolbar;
    private MobileArrayAdapter objMobileArrayAdapter;
    private Context ctx = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.action_bar);// for showing menu item
        setSupportActionBar(toolbar);// for showing menu item
        getSupportActionBar().setTitle(null); // for showing menu item
        ImageButton backImgBtn = (ImageButton) findViewById(R.id.back_btn);
        backImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        createVideoList(getVideoFiles(), getVideoThumbnail(), getVideoDuration(), fileSize);
        cleanUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { // should be define to attach menu item to action/toolbar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_item_activity, menu);
        MenuItem refreshBtn = menu.findItem(R.id.refresh_btn);
        refreshBtn.setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { // for handling menu item click
        switch (item.getItemId()) {
            case R.id.refresh_btn:
                System.out.println("refresh clicked");
                refreshIcons();
        }
        return super.onOptionsItemSelected(item);
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

    public void createVideoList(final List<String> videoFiles, final List<String> videoThumbnail, final List<Long> duration, final List<Long> FileSize) {
        objMobileArrayAdapter = new MobileArrayAdapter(this, videoFiles, videoThumbnail, duration, FileSize);
        ListView listView = new ListView(this);
        listView.setAdapter(objMobileArrayAdapter);
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
        refreshIcons();
    }

    public void refreshIcons() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                objMobileArrayAdapter.notifyDataSetChanged();
            }
        }, 5000);
    }

    public void cleanUp() {
        Thread th = new Thread() {
            @Override
            public void run() {
                CleanUpService objCleanUpService = new CleanUpService();
                objCleanUpService.deleteTempPlayBack(getVideoFiles(), ctx);
                objCleanUpService.deleteThumbnail(getVideoFiles());
            }
        };
        th.start();
    }
}
