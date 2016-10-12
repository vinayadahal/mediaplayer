package com.mediaplayer.activites;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mediaplayer.R;
import com.mediaplayer.components.PlayBackResume;
import com.mediaplayer.components.PlayFileComponentInit;
import com.mediaplayer.listeners.PlayFileTouchListener;
import com.mediaplayer.listeners.VideoOnCompletionListener;
import com.mediaplayer.listeners.VideoOnPreparedListener;
import com.mediaplayer.variables.CommonArgs;

import java.io.File;

public class PlayFile extends AppCompatActivity {

    private ImageButton playBtn, pauseBtn;
    private VideoOnPreparedListener objVideoOnPreparedListener = new VideoOnPreparedListener();
    private VideoOnCompletionListener objVideoOnCompletionListener = new VideoOnCompletionListener();
    private PlayFileTouchListener objPlayFileTouchListener = new PlayFileTouchListener();
    private Boolean isPaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CommonArgs.isPlaying = true;
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //hides notification bar
        setContentView(R.layout.activity_play_file);
        Toolbar play_file_toolbar = (Toolbar) findViewById(R.id.player_toolbar);// for showing menu item
        setSupportActionBar(play_file_toolbar);// for showing menu item
        getSupportActionBar().setTitle(null); // for showing menu item
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            CommonArgs.currentVideoPath = bundle.getString("fileName");
            CommonArgs.allVideoPath = bundle.getStringArrayList("allVideoPath");
        }
        PlayFileComponentInit objPlayFileComponent = new PlayFileComponentInit();
        CommonArgs.playFileCtx = this;
        objPlayFileComponent.initPlayFileGlobal();
        objPlayFileComponent.initPlayFileLocal();
        playFile(CommonArgs.currentVideoPath);
        initListeners();
        initLocalVariable();
        playBtn.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.play_file_menu, menu);//Menu Resource, Menu
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            new PlayBackResume().setResumePoint(CommonArgs.currentVideoPath, CommonArgs.videoView.getCurrentPosition());
            CommonArgs.mediaPlayer.stop(); // stops video playback
            CommonArgs.isPlaying = false;
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onDestroy() {
        CommonArgs.mediaPlayer.release(); // releases associated resources
        super.onDestroy();
    }

    @Override
    public void onPause() {
        CommonArgs.isPlaying = false;
        new PlayBackResume().setResumePoint(CommonArgs.currentVideoPath, CommonArgs.videoView.getCurrentPosition());
        playBtn.setVisibility(View.VISIBLE);
        pauseBtn.setVisibility(View.GONE);
        super.onPause();
    }

    @Override
    public void onResume() {
        if (!CommonArgs.isPlaying) {
            CommonArgs.setBackgroundOnResume = true;
            playBtn.setVisibility(View.GONE);
            pauseBtn.setVisibility(View.VISIBLE);
        }
        super.onResume();
    }

    public void closeActivity(View view) {
        CommonArgs.mediaPlayer.stop();
        finish();
    }

    public void initLocalVariable() {
        playBtn = (ImageButton) findViewById(R.id.play_btn);
        pauseBtn = (ImageButton) findViewById(R.id.pause_btn);
    }

    public void initListeners() {
        objVideoOnPreparedListener.totalTime = (TextView) findViewById(R.id.total_time);
        CommonArgs.videoView.setOnPreparedListener(objVideoOnPreparedListener);
        objVideoOnCompletionListener.ctx = this;
        CommonArgs.videoView.setOnCompletionListener(objVideoOnCompletionListener);
        CommonArgs.rl_play_file.setOnTouchListener(objPlayFileTouchListener);
    }

    public void playFile(String filename) {
        TextView vidTitle = (TextView) findViewById(R.id.vid_title);
        vidTitle.setText("Now Playing: " + new File(filename).getName());
        CommonArgs.videoView.setVideoURI(Uri.parse(filename));
        new Handler().postDelayed(new Runnable() {
            public void run() {
                new PlayBackResume().resumeFrom(CommonArgs.currentVideoPath);
            }
        }, 100);
    }

    public void pauseVideo(View view) {
        CommonArgs.isPlaying = false;
        playBtn.setVisibility(View.VISIBLE);
        pauseBtn.setVisibility(View.GONE);
        CommonArgs.mediaPlayer.pause();
        CommonArgs.title_control_handler.removeCallbacks(CommonArgs.title_control_runnable); //showing view during pause
        isPaused = true;
    }

    public void playVideo(View view) {
        CommonArgs.isPlaying = true;
        playBtn.setVisibility(View.GONE);
        pauseBtn.setVisibility(View.VISIBLE);
        CommonArgs.handler.postDelayed(CommonArgs.runnable, 10);
        CommonArgs.handler.postDelayed(CommonArgs.subtitle_runnable, 10);
        if (!isPaused) {
            new PlayBackResume().resumePlayBackAuto(CommonArgs.currentVideoPath);
        }
        CommonArgs.mediaPlayer.start();
        new PlayFileTouchListener().hideTitleControl(); // hiding after 3 seconds of playback
        isPaused = false;
    }

}