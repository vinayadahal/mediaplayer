package com.mediaplayer.listeners;

import android.app.Activity;
import android.media.MediaPlayer;
import android.widget.TextView;

import com.mediaplayer.components.SubtitleDisplay;
import com.mediaplayer.services.MathService;
import com.mediaplayer.services.MediaControl;
import com.mediaplayer.variables.CommonArgs;

public class VideoOnPreparedListener implements MediaPlayer.OnPreparedListener {

    public TextView totalTime;

    @Override
    public void onPrepared(MediaPlayer mp) {
        CommonArgs.mediaPlayer = mp; // used to pause video
        CommonArgs.duration = mp.getDuration();
        totalTime.setText(MathService.timeFormatter(CommonArgs.duration));
        CommonArgs.seekBar.setOnSeekBarChangeListener(new VideoOnSeekBarChangeListener());
        CommonArgs.videoView.start();
        CommonArgs.isPlaying = true;
        Thread th = new Thread() {
            public void run() {
                new SubtitleDisplay().showSub();
            }
        };
        th.start();
        th.setPriority(Thread.MIN_PRIORITY);
    }
}
