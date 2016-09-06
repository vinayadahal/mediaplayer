package com.mediaplayer.activites;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.mediaplayer.R;
import com.mediaplayer.components.PlayBackResume;
import com.mediaplayer.components.PopUpDialog;
import com.mediaplayer.components.SeekBarVisibility;
import com.mediaplayer.listeners.PlayFileTouchListener;
import com.mediaplayer.listeners.VideoOnCompletionListener;
import com.mediaplayer.listeners.VideoOnPreparedListener;
import com.mediaplayer.services.FileService;
import com.mediaplayer.services.MathService;
import com.mediaplayer.services.MediaControl;
import com.mediaplayer.variables.CommonArgs;

import java.io.File;
import java.util.List;


public class PlayFile extends AppCompatActivity {

    private ImageButton playBtn, pauseBtn, originalBtn, fullscreenBtn, portraitBtn, landscapeBtn;
    List<String> allVideoPath = null;
    String filePath = null; // or other values
    Boolean isPaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //hides notification bar
        setContentView(R.layout.activity_play_file);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            filePath = bundle.getString("fileName");
            allVideoPath = bundle.getStringArrayList("allVideoPath");
        }
        initGlobalVariable();
        playFile(filePath);
        initLocalVariable();
        setInitPlayerView();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            new PlayBackResume().setResumePoint(filePath, CommonArgs.videoView.getCurrentPosition());
            CommonArgs.mediaPlayer.stop(); // stops video playback
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
        new PlayBackResume().setResumePoint(filePath, CommonArgs.videoView.getCurrentPosition());
//        CommonArgs.mediaPlayer.pause();
//        isPaused = true;
        playBtn.setVisibility(View.VISIBLE);
        pauseBtn.setVisibility(View.GONE);
        super.onPause();
    }
//
//    @Override
//    public void onResume() {
//        if (isPaused)
//            CommonArgs.mediaPlayer.start();
//        super.onResume();
//    }

    public void closeActivity(View view) {
        CommonArgs.mediaPlayer.stop();
        finish();
    }

    public void initGlobalVariable() {
        CommonArgs.title_control = (RelativeLayout) findViewById(R.id.title_control);
        CommonArgs.seekBar = (SeekBar) findViewById(R.id.vid_seekbar);
        CommonArgs.currentTimeTxt = (TextView) findViewById(R.id.current_time);
        CommonArgs.notification_txt = (TextView) findViewById(R.id.notification_txt);
        CommonArgs.rl_play_file = (RelativeLayout) findViewById(R.id.play_file_relative_layout);
        CommonArgs.videoView = (VideoView) findViewById(R.id.videoView);
        CommonArgs.audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        CommonArgs.rl_volume_seekbar = (RelativeLayout) findViewById(R.id.rl_volume_seekbar);
        CommonArgs.volumeSeekBar = (SeekBar) findViewById(R.id.volume_seekbar);
        CommonArgs.volumeSeekBar.setMax(15);
        CommonArgs.rl_brightness_seekbar = (RelativeLayout) findViewById(R.id.rl_brightness_seekbar);
        CommonArgs.brightnessSeekBar = (SeekBar) findViewById(R.id.brightness_seekbar);
        CommonArgs.brightnessSeekBar.setMax(10);
        CommonArgs.playFileCtx = this;
    }

    public void initLocalVariable() {
        playBtn = (ImageButton) findViewById(R.id.play_btn);
        pauseBtn = (ImageButton) findViewById(R.id.pause_btn);
        originalBtn = (ImageButton) findViewById(R.id.original_btn);
        fullscreenBtn = (ImageButton) findViewById(R.id.fullscreen_btn);
        portraitBtn = (ImageButton) findViewById(R.id.portrait_btn);
        landscapeBtn = (ImageButton) findViewById(R.id.landscape_btn);
    }

    public void setInitPlayerView() {
        playBtn.setVisibility(View.GONE);
        fullscreenBtn.setVisibility(View.GONE);
        landscapeBtn.setVisibility(View.GONE);
    }

    public void playFile(String filename) {
        TextView vidTitle = (TextView) findViewById(R.id.vid_title);
        vidTitle.setText("Now Playing: " + new File(filename).getName());
        CommonArgs.title_control.setVisibility(View.GONE);
        CommonArgs.videoView.setVideoURI(Uri.parse(filename));
        CommonArgs.videoView.start();
        CommonArgs.rl_volume_seekbar.setVisibility(View.GONE);
        CommonArgs.rl_brightness_seekbar.setVisibility(View.GONE);
        CommonArgs.rl_play_file.setOnTouchListener(new PlayFileTouchListener());
        VideoOnPreparedListener objVideoOnPreparedListener = new VideoOnPreparedListener();
        objVideoOnPreparedListener.totalTime = (TextView) findViewById(R.id.total_time);
        CommonArgs.videoView.setOnPreparedListener(objVideoOnPreparedListener);
        VideoOnCompletionListener objVideoOnCompletionListener = new VideoOnCompletionListener();
        objVideoOnCompletionListener.ctx = this;
        CommonArgs.videoView.setOnCompletionListener(objVideoOnCompletionListener);
        new PlayBackResume().resumeFrom(filename);
    }

    public void pauseVideo(View view) {
        playBtn.setVisibility(View.VISIBLE);
        pauseBtn.setVisibility(View.GONE);
        CommonArgs.mediaPlayer.pause();
    }

    public void playVideo(View view) {
        playBtn.setVisibility(View.GONE);
        pauseBtn.setVisibility(View.VISIBLE);
        CommonArgs.mediaPlayer.start();
    }

    public void previous(View view) {
        int nextFile = new MediaControl().previousBtnAction(allVideoPath, filePath);
        playFile(allVideoPath.get(nextFile)); // playing new file
        filePath = allVideoPath.get(nextFile); // recording current playing file for future use
    }

    public void next(View view) {
        int nextFile = new MediaControl().nextBtnAction(allVideoPath, filePath); // gets next file array's index
        playFile(allVideoPath.get(nextFile)); // playing new file
        filePath = allVideoPath.get(nextFile); // recording current playing file for future use
    }

    public void originalSize(View view) {
        new MediaControl().setOriginalSize(fullscreenBtn, originalBtn);
    }

    public void fullScreenSize(View view) {
        new MediaControl().setFullscreen(fullscreenBtn, originalBtn);
    }

    public void changeOrientationPortrait(View view) {
        portraitBtn.setVisibility(View.GONE);
        landscapeBtn.setVisibility(View.VISIBLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void changeOrientationLandscape(View view) {
        landscapeBtn.setVisibility(View.GONE);
        portraitBtn.setVisibility(View.VISIBLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    public void showVolumeSeekbar(View view) {
        new SeekBarVisibility().showVolumeSeekbar();
    }

    public void setBrightness(View view) {
        new SeekBarVisibility().showBrightnessSeekBar();
    }
}