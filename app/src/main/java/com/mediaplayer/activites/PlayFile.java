package com.mediaplayer.activites;

import android.media.MediaPlayer;
import android.media.effect.Effect;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.mediaplayer.Effects;
import com.mediaplayer.R;

import org.w3c.dom.Text;

public class PlayFile extends AppCompatActivity {

    private RelativeLayout title_control;
    private int duration;
    private Thread lTimer;
    private MediaPlayer mediaPlayer;

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
        progressBarUpdate();
    }

    @Override
    protected void onDestroy() { //activity destroy event
        System.out.println("activity destroyed>>>>> ");
        lTimer.isInterrupted();
        lTimer.interrupt();
        super.onDestroy();
    }


    public void playFile(String filename, String videoFileName) {
        title_control = (RelativeLayout) findViewById(R.id.title_control);
        title_control.setVisibility(View.INVISIBLE);
        TextView vidTitle = (TextView) findViewById(R.id.vid_title);
        vidTitle.setText("Now Playing: " + videoFileName);
        VideoView vv = (VideoView) findViewById(R.id.videoView);
        vv.setVideoURI(Uri.parse(filename));
        vv.start();
        vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
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
        lTimer = new Thread() {
            public void run() {
                ProgressBar progressBar = (ProgressBar) findViewById(R.id.vid_progressbar);
                progressBar.setProgress(0);
                progressBar.setMax(100);
                final VideoView videoView = (VideoView) findViewById(R.id.videoView);
                videoView.start();
                videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        duration = videoView.getDuration();
                        mediaPlayer = mp; // used to pause video

                    }
                });
                do {
                    int current = videoView.getCurrentPosition();
                    System.out.println("duration - " + duration + " current- " + current);
                    try {
                        progressBar.setProgress((current * 100 / duration));
                        if (progressBar.getProgress() >= 100) {
                            break;
                        }
                    } catch (Exception e) {
                    }
                } while (progressBar.getProgress() <= 100);
            }
        };
        lTimer.start();
    }

    public void pauseVid(View view) {
        mediaPlayer.pause();
    }

}
