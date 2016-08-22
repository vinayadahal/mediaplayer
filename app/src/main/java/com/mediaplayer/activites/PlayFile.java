package com.mediaplayer.activites;

import android.content.Context;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.mediaplayer.R;
import com.mediaplayer.services.MediaControl;
import com.mediaplayer.services.PlayerSupport;
import com.mediaplayer.variables.CommonArgs;

import java.io.File;
import java.util.List;

public class PlayFile extends AppCompatActivity {

    private ImageButton playBtn, pauseBtn, originalBtn, fullscreenBtn;
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

    public void initGlobalVariable() {
        CommonArgs.title_control = (RelativeLayout) findViewById(R.id.title_control);
        CommonArgs.seekBar = (SeekBar) findViewById(R.id.vid_seekbar);
        CommonArgs.currentTimeTxt = (TextView) findViewById(R.id.current_time);
        CommonArgs.notification_txt = (TextView) findViewById(R.id.notification_txt);
        CommonArgs.rl_play_file = (RelativeLayout) findViewById(R.id.play_file_relative_layout);
        CommonArgs.videoView = (VideoView) findViewById(R.id.videoView);
        CommonArgs.audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
    }

    public void initLocalVariable() {
        playBtn = (ImageButton) findViewById(R.id.play_btn);
        pauseBtn = (ImageButton) findViewById(R.id.pause_btn);
        originalBtn = (ImageButton) findViewById(R.id.original_btn);
        fullscreenBtn = (ImageButton) findViewById(R.id.fullscreen_btn);
    }

    public void setInitPlayerView() {
        playBtn.setVisibility(View.GONE);
        pauseBtn.setVisibility(View.VISIBLE);
        originalBtn.setVisibility(View.VISIBLE);
        fullscreenBtn.setVisibility(View.GONE);
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

    public void rewind(View view) {
        int rewind_to = (CommonArgs.videoView.getCurrentPosition() - (int) (0.01 * CommonArgs.duration)); // rewind by 1%
        if (rewind_to < 0) {
            rewind_to = 0;
        }
        CommonArgs.mediaPlayer.seekTo(rewind_to);
    }

    public void forward(View view) {
        int forward_to = (CommonArgs.videoView.getCurrentPosition() + (int) (0.01 * CommonArgs.duration)); // forward by 1%
        CommonArgs.mediaPlayer.seekTo(forward_to);
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

}
