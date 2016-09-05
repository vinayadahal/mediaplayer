package com.mediaplayer.listeners;

import android.view.View;
import android.widget.SeekBar;

import com.mediaplayer.components.Effects;
import com.mediaplayer.services.MathService;
import com.mediaplayer.variables.CommonArgs;


public class VideoOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
    public Boolean showTimer = false;

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (showTimer) {
            CommonArgs.notification_txt.setVisibility(View.VISIBLE);
            CommonArgs.notification_txt.setText(new MathService().timeFormatter(progress));
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        CommonArgs.title_control_handler.removeCallbacks(CommonArgs.title_control_runnable);
        showTimer = true;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        CommonArgs.mediaPlayer.seekTo(seekBar.getProgress());
        new PlayFileTouchListener().hideTitleControl();
        CommonArgs.notification_txt.setVisibility(View.GONE);
        showTimer = false;
    }
}
