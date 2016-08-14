package com.mediaplayer.activites;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.VideoView;

import com.mediaplayer.R;

public class PlayFile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_file);
        Bundle b = getIntent().getExtras();
        String value = null; // or other values
        if (b != null) {
            value = b.getString("fileName");
        }
        playFile(value);
    }

    public void playFile(String filename) {
        VideoView vv = (VideoView) findViewById(R.id.videoView1);
        vv.setVideoURI(Uri.parse(filename));
        vv.start();

        vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                finish();
            }
        });
    }
}
