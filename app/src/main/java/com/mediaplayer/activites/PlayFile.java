package com.mediaplayer.activites;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.mediaplayer.R;
import com.mediaplayer.services.MediaControl;
import com.mediaplayer.services.PlayerSupport;
import com.mediaplayer.variables.CommonArgs;

import java.io.File;
import java.util.List;

public class PlayFile extends AppCompatActivity {

    private ImageButton playBtn, pauseBtn, originalBtn, fullscreenBtn, portraitBtn, landscapeBtn;
    private PlayerSupport objPlayerSupport = new PlayerSupport();
    List<String> allVideoPath = null;
    String filePath = null; // or other values

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

    public void closeActivity(View view) {
        CommonArgs.mediaPlayer.stop();
        finish();
    }

    public void initGlobalVariable() {
        CommonArgs.title_control = (RelativeLayout) findViewById(R.id.title_control);
        CommonArgs.seekBar = (SeekBar) findViewById(R.id.vid_seekbar);
        CommonArgs.currentTimeTxt = (TextView) findViewById(R.id.current_time);
        CommonArgs.notification_txt = (TextView) findViewById(R.id.notification_txt);
        CommonArgs.show_volume = (TextView) findViewById(R.id.show_volume);
        CommonArgs.rl_play_file = (RelativeLayout) findViewById(R.id.play_file_relative_layout);
        CommonArgs.videoView = (VideoView) findViewById(R.id.videoView);
        CommonArgs.audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        CommonArgs.playFileCtx = this;
    }

    public void initLocalVariable() {
        playBtn = (ImageButton) findViewById(R.id.play_btn);
        pauseBtn = (ImageButton) findViewById(R.id.pause_btn);
        originalBtn = (ImageButton) findViewById(R.id.original_btn);
        fullscreenBtn = (ImageButton) findViewById(R.id.fullscreen_btn);
        portraitBtn = (ImageButton) findViewById(R.id.portrait_btn);
        landscapeBtn = (ImageButton) findViewById(R.id.landscape_btn);
        new MediaControl().setScreenSize(this);
    }

    public void setInitPlayerView() {
        playBtn.setVisibility(View.GONE);
        fullscreenBtn.setVisibility(View.GONE);
        landscapeBtn.setVisibility(View.GONE);
        CommonArgs.show_volume.setVisibility(View.GONE);
    }

    public void playFile(String filename) {
        TextView vidTitle = (TextView) findViewById(R.id.vid_title);
        vidTitle.setText("Now Playing: " + new File(filename).getName());
        CommonArgs.title_control.setVisibility(View.INVISIBLE);
        CommonArgs.videoView.setVideoURI(Uri.parse(filename));
        CommonArgs.videoView.start();
        objPlayerSupport.playerScreenTouch();
        objPlayerSupport.setVideoViewListeners(this, (TextView) findViewById(R.id.total_time));
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

}
