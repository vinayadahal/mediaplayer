package com.mediaplayer.services;


import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.mediaplayer.activites.PlayFile;
import com.mediaplayer.components.Effects;
import com.mediaplayer.variables.CommonArgs;

public class BrightnessVolumeService {

    private PlayFile playFile;
    private WindowManager.LayoutParams lp;
    public Boolean isViewOn = false;
    private Handler handler = new Handler();
    private Runnable runnable;


    public void volumeSeekBarChange() {
        CommonArgs.volumeSeekBar.setProgress(CommonArgs.audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));//sets current volume to seekbar
        CommonArgs.volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
//                new Effects().fadeOut(CommonArgs.rl_volume_seekbar);
                isViewOn = true;
                autoFade(CommonArgs.rl_volume_seekbar);
            }
        });
    }

    public void setBrightness(Context ctx) {
        playFile = (PlayFile) ctx;
        lp = playFile.getWindow().getAttributes();
//        System.out.println("brightness::: " + (int) playFile.getWindow().getAttributes().screenBrightness);
//        CommonArgs.brightnessSeekBar.setProgress((int) playFile.getWindow().getAttributes().screenBrightness * 10);
        CommonArgs.brightnessSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float Progress = (float) progress / 10;
                lp.screenBrightness = Progress;
                playFile.getWindow().setAttributes(lp);
                isViewOn = false;
//                runnable = new Runnable() {
//                    public void run() {
//                        imgView.setBackground(ctx.getDrawable(R.drawable.brightness_high_btn));
//                    }
//                };
//                handler.post(runnable);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isViewOn = true;
                autoFade(CommonArgs.rl_brightness_seekbar);
//                new Effects().fadeOut(CommonArgs.rl_brightness_seekbar);
            }
        });
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
