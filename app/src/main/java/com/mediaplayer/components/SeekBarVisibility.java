package com.mediaplayer.components;


import android.media.AudioManager;
import android.view.View;

import com.mediaplayer.listeners.BrightnessOnSeekBarChangeListener;
import com.mediaplayer.listeners.VolumeOnSeekBarChangeListener;
import com.mediaplayer.variables.CommonArgs;

public class SeekBarVisibility {

    static Boolean  isViewOn = false, normalFade = false;

    public static void showVolumeSeekbar() {
        if (CommonArgs.rl_volume_seekbar.getVisibility() == View.GONE) {
            if (CommonArgs.rl_brightness_seekbar.getVisibility() == View.VISIBLE)
                new Effects().fadeOut(CommonArgs.rl_brightness_seekbar);
            CommonArgs.rl_volume_seekbar.setVisibility(View.VISIBLE);
            CommonArgs.volumeSeekBar.setProgress(CommonArgs.audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));//sets current volume to seekbar
            CommonArgs.volumeSeekBar.setOnSeekBarChangeListener(new VolumeOnSeekBarChangeListener());
            normalFade = true;
        } else if (CommonArgs.rl_volume_seekbar.getVisibility() == View.VISIBLE) {
            new Effects().fadeOut(CommonArgs.rl_volume_seekbar);
            normalFade = false;
        }
        if (normalFade)
            CommonArgs.autoFade(CommonArgs.rl_volume_seekbar, isViewOn);
    }

    public static void showBrightnessSeekBar() {
        if (CommonArgs.rl_brightness_seekbar.getVisibility() == View.GONE) {
            if (CommonArgs.rl_volume_seekbar.getVisibility() == View.VISIBLE)
                CommonArgs.autoFade(CommonArgs.rl_volume_seekbar, true);
            CommonArgs.rl_brightness_seekbar.setVisibility(View.VISIBLE);
            CommonArgs.brightnessSeekBar.setOnSeekBarChangeListener(new BrightnessOnSeekBarChangeListener());
            normalFade = true;
        } else if (CommonArgs.rl_brightness_seekbar.getVisibility() == View.VISIBLE) {
            new Effects().fadeOut(CommonArgs.rl_brightness_seekbar);
            normalFade = false;
        }
        if (normalFade)
            CommonArgs.autoFade(CommonArgs.rl_brightness_seekbar, true);
    }

}
