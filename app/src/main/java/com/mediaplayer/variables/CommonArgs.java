package com.mediaplayer.variables;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.mediaplayer.components.Effects;
import com.mediaplayer.listeners.PlayFileTouchListener;
import com.mediaplayer.services.MediaControl;
import com.mediaplayer.services.MobileArrayAdapter;

import java.util.List;

public class CommonArgs {

    public static RelativeLayout rl_play_file, title_control;
    public static TextView notification_txt;
    public static SeekBar seekBar;
    public static VideoView videoView;
    public static long duration;
    public static TextView currentTimeTxt, subArea;
    public static MediaPlayer mediaPlayer;
    public static AudioManager audioManager;
    public static Context playFileCtx;
    public static Runnable title_control_runnable, runnable, subtitle_runnable;
    public static Handler handler = new Handler(), title_control_handler = new Handler();
    public static String currentVideoPath;
    public static Boolean isPlaying;
    public static List<String> allVideoPath = null;

}
