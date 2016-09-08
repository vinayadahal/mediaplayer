package com.mediaplayer.variables;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.mediaplayer.components.Effects;
import com.mediaplayer.listeners.PlayFileTouchListener;
import com.mediaplayer.services.MediaControl;
import com.mediaplayer.services.MobileArrayAdapter;

public class CommonArgs {

    public static RelativeLayout rl_play_file, title_control, rl_volume_seekbar, rl_brightness_seekbar;
    public static TextView notification_txt;
    public static SeekBar seekBar, volumeSeekBar, brightnessSeekBar;
    public static VideoView videoView;
    public static long duration;
    public static TextView currentTimeTxt,subArea;
    public static MediaPlayer mediaPlayer;
    public static AudioManager audioManager;
    public static Context playFileCtx;
    public static Runnable title_control_runnable, runnable;
    public static Handler handler = new Handler(), title_control_handler = new Handler();

    public static void autoFade(final RelativeLayout relativeLayout, final Boolean isViewOn) {
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
