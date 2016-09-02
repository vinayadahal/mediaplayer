package com.mediaplayer.variables;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

public class CommonArgs {

    public static RelativeLayout rl_play_file, title_control, rl_volume_seekbar,rl_brightness_seekbar;
    public static TextView notification_txt;
    public static SeekBar seekBar, volumeSeekBar,brightnessSeekBar;
    public static VideoView videoView;
    public static long duration;
    public static TextView currentTimeTxt;
    public static MediaPlayer mediaPlayer;
    public static AudioManager audioManager;
    public static Context playFileCtx;
}
