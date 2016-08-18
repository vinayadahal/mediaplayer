package com.mediaplayer.activites;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.mediaplayer.R;
import com.mediaplayer.services.PlayerSupport;

import java.util.ArrayList;
import java.util.List;

public class PlayFile extends AppCompatActivity {

    private RelativeLayout title_control;
    private long duration;
    private MediaPlayer mediaPlayer;
    private VideoView videoView;
    private ImageButton playBtn;
    private ImageButton pauseBtn;
    private ImageButton originalBtn;
    private ImageButton fullscreenBtn;
    private TextView notification_txt;
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
        notification_txt = (TextView) findViewById(R.id.notification_txt);
        layoutParams = (RelativeLayout.LayoutParams) videoView.getLayoutParams();
        playBtn.setVisibility(View.GONE);
        pauseBtn.setVisibility(View.VISIBLE);
        originalBtn.setVisibility(View.VISIBLE);
        fullscreenBtn.setVisibility(View.GONE);
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
//                timeUpdate();
//                new PlayerSupport().timeUpdate(duration, (TextView) findViewById(R.id.current_time), videoView, title_control);
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
        new PlayerSupport().playerScreenTouch((RelativeLayout) findViewById(R.id.play_file_relative_layout),
                title_control, notification_txt, (ProgressBar) findViewById(R.id.vid_progressbar), videoView,
                duration, (TextView) findViewById(R.id.current_time));
        int minutes = (int) (duration / 1000) / 60;
        int seconds = (int) (duration / 1000) % 60;
        TextView totalTime = (TextView) findViewById(R.id.total_time);
        totalTime.setText(minutes + ":" + seconds);
    }

//    public void timeUpdate() {
//        final TextView currentTimeTxt = (TextView) findViewById(R.id.current_time);
//        final Handler handler = new Handler();
//        Runnable runnable = new Runnable() {
//            public void run() {
//                System.out.println("inside thread");
//                long current = 0;
//                int sleepingFor = 0;
//                while (current != duration) {
//                    current = videoView.getCurrentPosition();
//                    if (sleepingFor == 6 || current == duration || title_control.getVisibility() != View.VISIBLE) {
//                        break;
//                    }
//                    final int minutes = (int) (current / 1000) / 60;
//                    final int seconds = (int) (current / 1000) % 60;
//                    System.out.println("TIME::: " + minutes + ":" + seconds);
//                    handler.post(new Runnable() {
//                        public void run() {
//                            currentTimeTxt.setText(minutes + ":" + seconds);
//                        }
//                    });
//                    System.out.println("Sleeping for ---------> " + sleepingFor);
//                }
//            }
//        };
//        new Thread(runnable).start();
//    }

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
        int rewind_to = (videoView.getCurrentPosition() - 10000);
        if (rewind_to < 0) {
            rewind_to = 0;
        }
        mediaPlayer.seekTo(rewind_to);
    }

    public void forward(View view) {
        int forward_to = (videoView.getCurrentPosition() + 10000);
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
        new PlayerSupport().setOriginalSize(layoutParams, videoView, fullscreenBtn, originalBtn, notification_txt);
    }

    public void fullScreenSize(View view) {
        new PlayerSupport().setFullscreen(layoutParams, videoView, fullscreenBtn, originalBtn, notification_txt);
    }
}
