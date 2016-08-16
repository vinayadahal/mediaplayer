package com.mediaplayer.activites;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.mediaplayer.R;
import com.mediaplayer.components.Effects;

import java.util.Timer;
import java.util.TimerTask;

public class PlayFile extends AppCompatActivity {

    private RelativeLayout title_control;
    private long duration;
    private Thread progressUpdate;
    private MediaPlayer mediaPlayer;
    private VideoView videoView;
    private ImageButton playBtn;
    private ImageButton pauseBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //hides notification bar
        setContentView(R.layout.activity_play_file);
        Bundle b = getIntent().getExtras();
        String filePath = null; // or other values
        String videoFileName = null;
        if (b != null) {
            filePath = b.getString("fileName");
            videoFileName = b.getString("videoFileName");
        }
        playFile(filePath, videoFileName);
        playBtn = (ImageButton) findViewById(R.id.play_btn);
        pauseBtn = (ImageButton) findViewById(R.id.pause_btn);
        playBtn.setVisibility(View.GONE);
        pauseBtn.setVisibility(View.VISIBLE);

    }

    public void playFile(String filename, String videoFileName) {
        title_control = (RelativeLayout) findViewById(R.id.title_control);
        title_control.setVisibility(View.INVISIBLE);
        TextView vidTitle = (TextView) findViewById(R.id.vid_title);
        vidTitle.setText("Now Playing: " + videoFileName);
        videoView = (VideoView) findViewById(R.id.videoView);
        videoView.setVideoURI(Uri.parse(filename));
        videoView.start();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                duration = videoView.getDuration();
                mediaPlayer = mp; // used to pause video
                progressBarUpdate();
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                finish();
            }
        });
    }

    public void screenTouch(View view) {
        title_control.setVisibility(View.VISIBLE);
        title_control.postDelayed(new Runnable() {
            @Override
            public void run() {
                new Effects().fadeOut(title_control);
            }
        }, 3000);
    }


    public void progressBarUpdate() {
        progressUpdate = new Thread() {
            public void run() {
                ProgressBar progressBar = (ProgressBar) findViewById(R.id.vid_progressbar);
                progressBar.setProgress(0);
                progressBar.setMax(100);
                int i = 0;
                while (progressBar.getProgress() <= 100) {
                    long current = videoView.getCurrentPosition();
//                    System.out.println("duration - " + duration + " current- " + current);
//                    System.out.println("Progress::::::: " + current * 100 / duration);
//                    System.out.println("minutes " + ((duration / 1000) / 60));
                    int currentTimeInSec = (int) (current / 1000);
                    progressBar.setProgress(currentTimeInSec);
                    if (current == 0) {
                        System.out.println("current is 0 for:::::: " + i++);
                        if (i >= 15000) {
                            i = 0;
                            break;
                        }
                    }
                    if (progressBar.getProgress() >= 100) {
                        break;
                    }
                }
            }
        };
        progressUpdate.start();
    }

    public void pauseVideo(View view) {
        playBtn.setVisibility(View.VISIBLE);
        pauseBtn.setVisibility(View.GONE);
        mediaPlayer.pause();
    }

    public void playVideo(View view) {
        playBtn.setVisibility(View.GONE);
        pauseBtn.setVisibility(View.VISIBLE);
        mediaPlayer.start();
    }
}
