package com.mediaplayer.listeners;

import android.os.Handler;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.mediaplayer.components.Effects;
import com.mediaplayer.services.MediaControl;
import com.mediaplayer.variables.CommonArgs;

public class VolumeOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

    Boolean isViewOn = false;

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        new MediaControl().setVolume(progress);
        isViewOn = false;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        isViewOn = true;
        autoFade(CommonArgs.rl_volume_seekbar);
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
