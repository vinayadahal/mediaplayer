package com.mediaplayer.components;


import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.mediaplayer.R;
import com.mediaplayer.listeners.BtnOnClickListener;
import com.mediaplayer.variables.CommonArgs;

public class PlayFileComponentInit {

    private BtnOnClickListener objBtnOnClickListener = new BtnOnClickListener();

    public void initPlayFileGlobal() {
        CommonArgs.title_control = (RelativeLayout) ((Activity) CommonArgs.playFileCtx).findViewById(R.id.title_control);
        CommonArgs.subArea = (TextView) ((Activity) CommonArgs.playFileCtx).findViewById(R.id.subArea);
        CommonArgs.seekBar = (SeekBar) ((Activity) CommonArgs.playFileCtx).findViewById(R.id.vid_seekbar);
        CommonArgs.currentTimeTxt = (TextView) ((Activity) CommonArgs.playFileCtx).findViewById(R.id.current_time);
        CommonArgs.notification_txt = (TextView) ((Activity) CommonArgs.playFileCtx).findViewById(R.id.notification_txt);
        CommonArgs.rl_play_file = (RelativeLayout) ((Activity) CommonArgs.playFileCtx).findViewById(R.id.play_file_relative_layout);
        CommonArgs.videoView = (VideoView) ((Activity) CommonArgs.playFileCtx).findViewById(R.id.videoView);
        CommonArgs.audioManager = (AudioManager) CommonArgs.playFileCtx.getSystemService(Context.AUDIO_SERVICE);
        setGlobalVariable();
    }

    public void setGlobalVariable() {
        CommonArgs.seekBar.setProgress(0);
        CommonArgs.title_control.setVisibility(View.GONE);
    }

    public void initPlayFileLocal() {
        nextPrevBtnInit();
        volumeBrightBtnInit();
        setVideoViewSize();
        setScreenOrientation();
    }

    public void nextPrevBtnInit() {
        ImageButton next_btn = (ImageButton) ((Activity) CommonArgs.playFileCtx).findViewById(R.id.next_btn);
        next_btn.setOnClickListener(objBtnOnClickListener);
        ImageButton previous_btn = (ImageButton) ((Activity) CommonArgs.playFileCtx).findViewById(R.id.previous_btn);
        previous_btn.setOnClickListener(objBtnOnClickListener);
    }

    public void volumeBrightBtnInit() {
        ImageButton brightness_btn = (ImageButton) ((Activity) CommonArgs.playFileCtx).findViewById(R.id.brightness);
        brightness_btn.setOnClickListener(objBtnOnClickListener);
        ImageButton volume_btn = (ImageButton) ((Activity) CommonArgs.playFileCtx).findViewById(R.id.volume_btn);
        volume_btn.setOnClickListener(objBtnOnClickListener);
    }

    public void setVideoViewSize() {
        ImageButton originalBtn = (ImageButton) ((Activity) CommonArgs.playFileCtx).findViewById(R.id.original_btn);
        objBtnOnClickListener.original_btn = originalBtn;
        originalBtn.setOnClickListener(objBtnOnClickListener);
        originalBtn.setVisibility(View.GONE); //sets init design
        ImageButton fullscreenBtn = (ImageButton) ((Activity) CommonArgs.playFileCtx).findViewById(R.id.fullscreen_btn);
        objBtnOnClickListener.fullscreen_btn = fullscreenBtn;
        fullscreenBtn.setOnClickListener(objBtnOnClickListener);
    }

    public void setScreenOrientation() {
        ImageButton portraitBtn = (ImageButton) ((Activity) CommonArgs.playFileCtx).findViewById(R.id.portrait_btn);
        objBtnOnClickListener.portrait_btn = portraitBtn;
        portraitBtn.setOnClickListener(objBtnOnClickListener);
        ImageButton landscapeBtn = (ImageButton) ((Activity) CommonArgs.playFileCtx).findViewById(R.id.landscape_btn);
        objBtnOnClickListener.landscape_btn = landscapeBtn;
        landscapeBtn.setOnClickListener(objBtnOnClickListener);
        landscapeBtn.setVisibility(View.GONE); //sets init design
    }

}
