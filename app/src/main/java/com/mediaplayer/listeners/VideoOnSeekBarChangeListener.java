package com.mediaplayer.listeners;

import android.widget.SeekBar;

import com.mediaplayer.variables.CommonArgs;


public class VideoOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        CommonArgs.title_control_handler.removeCallbacks(CommonArgs.title_control_runnable);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        CommonArgs.mediaPlayer.seekTo(seekBar.getProgress());
        new PlayFileTouchListener().hideTitleControl();
    }
}
