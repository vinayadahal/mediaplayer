package com.mediaplayer.listeners;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;


public class VideoOnCompletionListener implements MediaPlayer.OnCompletionListener {

    public Context ctx;

    @Override
    public void onCompletion(MediaPlayer mp) {
        ((Activity) ctx).finish();
    }

}
