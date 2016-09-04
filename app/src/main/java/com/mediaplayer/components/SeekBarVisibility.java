package com.mediaplayer.components;


import android.media.AudioManager;
import android.view.View;

import com.mediaplayer.listeners.BrightnessOnSeekBarChangeListener;
import com.mediaplayer.listeners.VolumeOnSeekBarChangeListener;
import com.mediaplayer.variables.CommonArgs;

public class SeekBarVisibility {

    public void showVolumeSeekbar() {
        if (CommonArgs.rl_volume_seekbar.getVisibility() == View.GONE) {
            if (CommonArgs.rl_brightness_seekbar.getVisibility() == View.VISIBLE)
                new Effects().fadeOut(CommonArgs.rl_brightness_seekbar);
            CommonArgs.rl_volume_seekbar.setVisibility(View.VISIBLE);
            CommonArgs.volumeSeekBar.setProgress(CommonArgs.audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));//sets current volume to seekbar
            CommonArgs.volumeSeekBar.setOnSeekBarChangeListener(new VolumeOnSeekBarChangeListener());
        } else if (CommonArgs.rl_volume_seekbar.getVisibility() == View.VISIBLE) {
            new Effects().fadeOut(CommonArgs.rl_volume_seekbar);
        }
    }

    public void showBrightnessSeekBar() {
        if (CommonArgs.rl_brightness_seekbar.getVisibility() == View.GONE) {
            if (CommonArgs.rl_volume_seekbar.getVisibility() == View.VISIBLE)
                new Effects().fadeOut(CommonArgs.rl_volume_seekbar);
            CommonArgs.rl_brightness_seekbar.setVisibility(View.VISIBLE);
            CommonArgs.brightnessSeekBar.setOnSeekBarChangeListener(new BrightnessOnSeekBarChangeListener());
        } else if (CommonArgs.rl_brightness_seekbar.getVisibility() == View.VISIBLE) {
            new Effects().fadeOut(CommonArgs.rl_brightness_seekbar);
        }
    }

}
