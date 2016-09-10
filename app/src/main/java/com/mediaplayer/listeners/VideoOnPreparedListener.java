package com.mediaplayer.listeners;

import android.media.MediaPlayer;
import android.os.Handler;
import android.widget.TextView;

import com.mediaplayer.services.MathService;
import com.mediaplayer.services.SrtParser;
import com.mediaplayer.variables.CommonArgs;

import org.apache.commons.io.FilenameUtils;

import java.io.File;

public class VideoOnPreparedListener implements MediaPlayer.OnPreparedListener {

    public TextView totalTime;
    private Handler handler = new Handler();

    @Override
    public void onPrepared(MediaPlayer mp) {
        CommonArgs.mediaPlayer = mp; // used to pause video
        System.out.println("duration before onPrepared:::: " + CommonArgs.duration);
        CommonArgs.duration = mp.getDuration();
        totalTime.setText(MathService.timeFormatter(CommonArgs.duration));
        CommonArgs.seekBar.setOnSeekBarChangeListener(new VideoOnSeekBarChangeListener());
//        CommonArgs.parsedAryNumber = 0;
        CommonArgs.isPlaying = true;
        Thread th = new Thread() {
            public void run() {
                showSub();
            }
        };
        th.start();
        th.setPriority(Thread.MIN_PRIORITY);
    }


    public void showSub() {
        final String srtFile = new FilenameUtils().removeExtension(CommonArgs.currentVideoPath) + ".srt";
        System.out.println("mdplayer------- " + CommonArgs.mediaPlayer);
        if (!new File(srtFile).exists()) {
            return;
        }
        final SrtParser objSrtParser = new SrtParser();
        final StringBuilder text = objSrtParser.loadSrt(srtFile);
        Runnable runnable = new Runnable() {
            public void run() {
                if (CommonArgs.isPlaying) {
                    Thread th = Thread.currentThread();
                    th.setPriority(Thread.MIN_PRIORITY);
                    String time = objSrtParser.srtTimeFormatter(CommonArgs.mediaPlayer.getCurrentPosition());
                    final String line = objSrtParser.parse(text, time);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (line != null) {
                                CommonArgs.subArea.setText(line);
                            } else if (line == null) {
                                CommonArgs.subArea.setText("");
                            }
                        }
                    });
                    handler.postDelayed(this, 500);
                }
            }
        };
        handler.post(runnable);
    }
}
