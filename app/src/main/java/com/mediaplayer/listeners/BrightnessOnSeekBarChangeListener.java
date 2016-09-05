package com.mediaplayer.listeners;


import android.os.Handler;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.mediaplayer.activites.PlayFile;
import com.mediaplayer.components.Effects;
import com.mediaplayer.variables.CommonArgs;

public class BrightnessOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

    private PlayFile playFile = (PlayFile) CommonArgs.playFileCtx;
    private WindowManager.LayoutParams lp = playFile.getWindow().getAttributes();
    Boolean isViewOn = false;

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        autoFade(CommonArgs.rl_volume_seekbar);
        float Progress = (float) progress / 10;
        lp.screenBrightness = Progress;
        if (Progress > 0) {
            playFile.getWindow().setAttributes(lp);
        }
        isViewOn = false;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        playFile.getWindow().setAttributes(lp);
        autoFade(CommonArgs.rl_brightness_seekbar);
        isViewOn = true;
    }

    public void autoFade(final RelativeLayout relativeLayout) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (isViewOn) {
                    new Effects().fadeOut(relativeLayout);
                }
            }
        };
        new Handler().postDelayed(runnable, 3000);
    }

}
