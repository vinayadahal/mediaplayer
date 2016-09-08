package com.mediaplayer.listeners;

import android.media.MediaPlayer;
import android.os.Handler;
import android.widget.TextView;

import com.mediaplayer.services.MathService;
import com.mediaplayer.services.SrtParser;
import com.mediaplayer.variables.CommonArgs;

public class VideoOnPreparedListener implements MediaPlayer.OnPreparedListener {

    public TextView totalTime;
    private Handler handler = new Handler();

    @Override
    public void onPrepared(MediaPlayer mp) {
        CommonArgs.mediaPlayer = mp; // used to pause video
        System.out.println("duration before onPrepared:::: " + CommonArgs.duration);
        CommonArgs.duration = mp.getDuration();
        totalTime.setText(new MathService().timeFormatter(CommonArgs.duration));
        CommonArgs.seekBar.setOnSeekBarChangeListener(new VideoOnSeekBarChangeListener());
        Thread th = new Thread() {
            public void run() {
                showSub();
            }
        };
        th.start();
    }


    public void showSub() {
        SrtParser objSrtParser = new SrtParser();
        while (CommonArgs.mediaPlayer.getCurrentPosition() != CommonArgs.duration && CommonArgs.mediaPlayer.isPlaying()) {
            String time = objSrtParser.srtTimeFormatter(CommonArgs.mediaPlayer.getCurrentPosition());
            final String line = objSrtParser.parse(objSrtParser.loadSrt("/storage/sdcard0/Videos/---PSY - GENTLEMAN M-_V - YouTube.srt"), time);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (line != null) {
                        CommonArgs.subArea.setText(line);
                    } else {
                        CommonArgs.subArea.setText("");
                    }
                }
            });
        }
    }
}
