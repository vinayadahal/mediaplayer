package com.mediaplayer.listeners;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;

import com.mediaplayer.components.PlayBackResume;
import com.mediaplayer.services.FileService;
import com.mediaplayer.variables.CommonArgs;

import java.io.File;


public class VideoOnCompletionListener implements MediaPlayer.OnCompletionListener {

    public Context ctx;
    public String filePath;

    @Override
    public void onCompletion(MediaPlayer mp) {
        ((Activity) ctx).finish();
    }

}
