package com.mediaplayer.listeners;

import android.media.MediaPlayer;
import android.widget.TextView;

import com.mediaplayer.services.MathService;
import com.mediaplayer.variables.CommonArgs;

public class VideoOnPreparedListener implements MediaPlayer.OnPreparedListener {

    public TextView totalTime;

    @Override
    public void onPrepared(MediaPlayer mp) {
        CommonArgs.duration = mp.getDuration();
        CommonArgs.mediaPlayer = mp; // used to pause video
        totalTime.setText(new MathService().timeFormatter(CommonArgs.duration));
        CommonArgs.seekBar.setOnSeekBarChangeListener(new VideoOnSeekBarChangeListener());
    }
}
