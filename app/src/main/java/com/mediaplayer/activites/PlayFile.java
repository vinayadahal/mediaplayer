package com.mediaplayer.activites;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.mediaplayer.R;
import com.mediaplayer.services.PlayerSupport;
import com.mediaplayer.variables.ArgsPlayerSupport;

import java.util.ArrayList;
import java.util.List;

public class PlayFile extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private ImageButton playBtn;
    private ImageButton pauseBtn;
    private ImageButton originalBtn;
    private ImageButton fullscreenBtn;
    private RelativeLayout.LayoutParams layoutParams;
    //player stuff
    List<String> allVideoPath = null;
    List<String> allFileList = null;
    String filePath = null; // or other values
    String videoFileName = null;
    ArrayList<String> allFilesPath = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //hides notification bar
        setContentView(R.layout.activity_play_file);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            filePath = bundle.getString("fileName");
            videoFileName = bundle.getString("videoFileName");
            allVideoPath = bundle.getStringArrayList("allVideoPath");
            allFileList = bundle.getStringArrayList("allFileList");
        }
        playFile(filePath, videoFileName);
        for (int i = 0; i < allVideoPath.size(); i++) { // foe
            allFilesPath.add(allVideoPath.get(i) + allFileList.get(i));
        }
        playBtn = (ImageButton) findViewById(R.id.play_btn);
        pauseBtn = (ImageButton) findViewById(R.id.pause_btn);
        originalBtn = (ImageButton) findViewById(R.id.original_btn);
        fullscreenBtn = (ImageButton) findViewById(R.id.fullscreen_btn);
        ArgsPlayerSupport.notification_txt = (TextView) findViewById(R.id.notification_txt);
        layoutParams = (RelativeLayout.LayoutParams) ArgsPlayerSupport.videoView.getLayoutParams();
        playBtn.setVisibility(View.GONE);
        pauseBtn.setVisibility(View.VISIBLE);
        originalBtn.setVisibility(View.VISIBLE);
        fullscreenBtn.setVisibility(View.GONE);
    }

    public void playFile(String filename, String videoFileName) {
        ArgsPlayerSupport.title_control = (RelativeLayout) findViewById(R.id.title_control);
        ArgsPlayerSupport.title_control.setVisibility(View.INVISIBLE);
        TextView vidTitle = (TextView) findViewById(R.id.vid_title);
        vidTitle.setText("Now Playing: " + videoFileName);
        ArgsPlayerSupport.videoView = (VideoView) findViewById(R.id.videoView);
        ArgsPlayerSupport.videoView.setVideoURI(Uri.parse(filename));
        ArgsPlayerSupport.videoView.start();
        ArgsPlayerSupport.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                ArgsPlayerSupport.duration = ArgsPlayerSupport.videoView.getDuration();
                mediaPlayer = mp; // used to pause video
                progressBarUpdate();
                new PlayerSupport().seekToBar((SeekBar) findViewById(R.id.vid_seekbar), mediaPlayer, ArgsPlayerSupport.duration);
            }
        });
        ArgsPlayerSupport.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                finish();
            }
        });
    }

    public void progressBarUpdate() {
        ArgsPlayerSupport.rl_play_file = (RelativeLayout) findViewById(R.id.play_file_relative_layout);
        ArgsPlayerSupport.seekBar = (SeekBar) findViewById(R.id.vid_seekbar);
        ArgsPlayerSupport.currentTimeTxt = (TextView) findViewById(R.id.current_time);
        new PlayerSupport().playerScreenTouch();
        TextView totalTime = (TextView) findViewById(R.id.total_time);
        totalTime.setText(new PlayerSupport().timeFormatter(ArgsPlayerSupport.duration));
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

    public void rewind(View view) {
        int rewind_to = (ArgsPlayerSupport.videoView.getCurrentPosition() - 10000);
        if (rewind_to < 0) {
            rewind_to = 0;
        }
        mediaPlayer.seekTo(rewind_to);
    }

    public void forward(View view) {
        int forward_to = (ArgsPlayerSupport.videoView.getCurrentPosition() + 10000);
        mediaPlayer.seekTo(forward_to);
    }

    public void previous(View view) {
        int nextFile = allFilesPath.indexOf(filePath) - 1; //current file - 1
        if (nextFile < 0) {
            nextFile = allFileList.size() - 1; // setting final value as next file so that player support  backward loop
        }
        playFile(allFilesPath.get(nextFile), allFileList.get(nextFile)); // playing new file
        filePath = allFilesPath.get(nextFile); // recording current playing file for future use
    }


    public void next(View view) {
        int nextFile = allFilesPath.indexOf(filePath) + 1; //current file + 1
        if (nextFile > (allFilesPath.size() - 1)) {
            nextFile = 0; // setting 0 as next file so that player support forward loop
        }
        playFile(allFilesPath.get(nextFile), allFileList.get(nextFile)); // playing new file
        filePath = allFilesPath.get(nextFile); // recording current playing file for future use
    }

    public void originalSize(View view) {
        new PlayerSupport().setOriginalSize(fullscreenBtn, originalBtn);
    }

    public void fullScreenSize(View view) {
        new PlayerSupport().setFullscreen(fullscreenBtn, originalBtn);
    }

}
