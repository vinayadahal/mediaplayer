package com.mediaplayer.activites;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.mediaplayer.R;
import com.mediaplayer.services.IconService;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        final IconService objIconService = new IconService();
        objIconService.ctx = this;
        final Handler handler = new Handler();
        final Intent intent = new Intent(this, MainActivity.class);
        Thread lTimer = new Thread() {
            public void run() {
                String[] columns = {MediaStore.MediaColumns.DATA, MediaStore.MediaColumns._ID, MediaStore.MediaColumns.SIZE};
                Cursor cursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, columns, null, null, MediaStore.MediaColumns.TITLE);
                if (cursor.moveToFirst()) {
                    while (cursor.isAfterLast() == false) {
                        String fileName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
                        final String file = fileName;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                TextView SplashTextView = (TextView) findViewById(R.id.fileLoading);
                                SplashTextView.setText(file);
                            }
                        });
                        objIconService.checkThumb(fileName);
                        cursor.moveToNext();
                    }
                }
                cursor.close();

                startActivity(intent);
                finish();
            }
        };
        lTimer.start();
    }


}
