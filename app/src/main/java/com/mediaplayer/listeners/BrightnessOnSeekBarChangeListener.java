package com.mediaplayer.listeners;


import android.view.WindowManager;
import android.widget.SeekBar;

import com.mediaplayer.activites.PlayFile;
import com.mediaplayer.variables.CommonArgs;

public class BrightnessOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

    private PlayFile playFile = (PlayFile) CommonArgs.playFileCtx;
    private WindowManager.LayoutParams lp = playFile.getWindow().getAttributes();

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        float Progress = (float) progress / 10;
        lp.screenBrightness = Progress;
        if (Progress > 0) {
            playFile.getWindow().setAttributes(lp);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        playFile.getWindow().setAttributes(lp);
    }

}
