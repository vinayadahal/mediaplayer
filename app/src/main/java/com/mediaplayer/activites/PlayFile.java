package com.mediaplayer.activites;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.mediaplayer.R;
import com.mediaplayer.components.Effects;

public class PlayFile extends AppCompatActivity {

    private RelativeLayout title_control;
    private long duration;
    private Thread progressUpdate;
    private MediaPlayer mediaPlayer;
    private VideoView videoView;
    private ImageButton playBtn;
    private ImageButton pauseBtn;
    private ImageButton originalBtn;
    private ImageButton fullscreenBtn;
    private TextView notification_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //hides notification bar
        setContentView(R.layout.activity_play_file);
        Bundle bundle = getIntent().getExtras();
        String filePath = null; // or other values
        String videoFileName = null;
        if (bundle != null) {
            filePath = bundle.getString("fileName");
            videoFileName = bundle.getString("videoFileName");
        }
        playFile(filePath, videoFileName);
        playBtn = (ImageButton) findViewById(R.id.play_btn);
        pauseBtn = (ImageButton) findViewById(R.id.pause_btn);
        originalBtn = (ImageButton) findViewById(R.id.original_btn);
        fullscreenBtn = (ImageButton) findViewById(R.id.fullscreen_btn);
        notification_txt = (TextView) findViewById(R.id.notification_txt);
        playBtn.setVisibility(View.GONE);
        pauseBtn.setVisibility(View.VISIBLE);
        originalBtn.setVisibility(View.VISIBLE);
        fullscreenBtn.setVisibility(View.GONE);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.play_file_relative_layout);
        relativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        title_control.setVisibility(View.VISIBLE);
                        break;
                    case MotionEvent.ACTION_UP:
                        removeNotificateText();
                        title_control.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new Effects().fadeOut(title_control);
                                removeNotificateText();
                            }
                        }, 3000);
                        break;
                }
                return true;
            }
        });
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

    public void progressBarUpdate() {
        progressUpdate = new Thread() {
            public void run() {
                ProgressBar progressBar = (ProgressBar) findViewById(R.id.vid_progressbar);
                progressBar.setProgress(0);
                progressBar.setMax(100);
                int i = 0;
                while (progressBar.getProgress() <= 100) {
                    long current = videoView.getCurrentPosition();
                    int currentTimeInSec = (int) (current * 100 / duration);
                    progressBar.setProgress(currentTimeInSec);
                    if (current == 0) {
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

    public void originalSize(View view) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) videoView.getLayoutParams();
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        videoView.setLayoutParams(layoutParams);
        fullscreenBtn.setVisibility(View.VISIBLE);
        originalBtn.setVisibility(View.GONE);
        notification_txt.setText("Original Size");
    }

    public void fullScreenSize(View view) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) videoView.getLayoutParams();
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 1);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 1);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 1);
        videoView.setLayoutParams(layoutParams);
        originalBtn.setVisibility(View.VISIBLE);
        fullscreenBtn.setVisibility(View.GONE);
        notification_txt.setText("Fullscreen");
    }

    public void removeNotificateText() {
        if (!notification_txt.getText().equals("")) {
            notification_txt.setText("");
        }
    }
}
