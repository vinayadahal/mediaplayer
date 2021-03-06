package com.mediaplayer.activites;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.mediaplayer.components.MessageAlert;
import com.mediaplayer.services.CleanUpService;
import com.mediaplayer.services.IconService;
import com.mediaplayer.services.MobileArrayAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {



    private List<Long> fileSize = new ArrayList<>();
    private Toolbar toolbar;
    private MobileArrayAdapter objMobileArrayAdapter;
    private Context ctx = this;
    private int flag_list_created = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getPermission(this);
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
                MediaScannerConnection.scanFile(MainActivity.this,
                        new String[]{Environment.getExternalStorageDirectory().toString()}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String path, Uri uri) {
                                System.out.println("Scan Complete: " + path);
                                System.out.println("Loading Video Files: ");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        cleanUp();
                                        createVideoList(getVideoFiles(), getVideoThumbnail(), getVideoDuration(), fileSize);
                                    }
                                }); // force reload of video file list
                            }
                        }); // refreshes file list
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
                if (checkFileExists(fileName)) { // checks if file exists or not before starting PlayFile activity.
                    Intent intent = new Intent(MainActivity.this, PlayFile.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("fileName", fileName); // value for another activities
                    bundle.putStringArrayList("allVideoPath", (ArrayList<String>) videoFiles);
                    intent.putExtras(bundle); // bundle saved as extras
                    startActivity(intent);
                } else {
                    new MessageAlert().showToast("Can't play this file", ctx);
                }
            }
        });
        LinearLayout lnrlayout = (LinearLayout) findViewById(R.id.lnrLayout);
        lnrlayout.addView(listView);
        if (flag_list_created == 0) {
            flag_list_created = 1;
        } else {
            lnrlayout.removeAllViews();
            lnrlayout.addView(listView);
        }
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
                CleanUpService.deleteTempPlayBack(getVideoFiles(), ctx);
                CleanUpService.deleteThumbnail(getVideoFiles());
            }
        };
        th.start();
    }

    public boolean checkFileExists(String fileName) {
        File videofile = new File(fileName);
        if (videofile.exists()) {
            return true;
        } else {
            return false;
        }
    }

    public void getPermission(Context context){
        String StorageReadPermission=Manifest.permission.READ_EXTERNAL_STORAGE;
        String StorageWritePermission=Manifest.permission.WRITE_EXTERNAL_STORAGE;
        int PermissionGranted= PackageManager.PERMISSION_GRANTED;
        if (ContextCompat.checkSelfPermission(context, StorageReadPermission )!=PermissionGranted) {
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{StorageReadPermission}, 1);
        }
        if (ContextCompat.checkSelfPermission(context, StorageWritePermission )!=PermissionGranted) {
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{StorageWritePermission}, 1);
        }
    }
}