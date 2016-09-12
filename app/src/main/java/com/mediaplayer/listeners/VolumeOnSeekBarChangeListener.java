package com.mediaplayer.listeners;

import android.widget.SeekBar;

import com.mediaplayer.services.MediaControl;
import com.mediaplayer.variables.CommonArgs;

public class VolumeOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

    Boolean isViewOn = false;
    int Progress;

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        Progress = progress;
        new MediaControl().setVolume(Progress);
        isViewOn = false;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        new MediaControl().setVolume(Progress);
        isViewOn = true;
//        CommonArgs.autoFade(CommonArgs.rl_volume_seekbar, isViewOn);
    }

}
