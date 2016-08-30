package com.mediaplayer.services;

import android.content.Context;
import android.graphics.Point;
import android.media.AudioManager;
import android.os.Build;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mediaplayer.variables.CommonArgs;

import java.util.List;

public class MediaControl {

    static int measuredWidth = 0;
    static int measuredHeight = 0;

    public int nextBtnAction(List<String> allVideoPath, String filePath) {
        int nextFile = allVideoPath.indexOf(filePath) + 1; //current file + 1
        if (nextFile > (allVideoPath.size() - 1)) {
            nextFile = 0; // setting 0 as next file so that player support forward loop
        }
        CommonArgs.mediaPlayer.stop(); // stopping before next video play
        return nextFile;
    }

    public int previousBtnAction(List<String> allVideoPath, String filePath) {
        int nextFile = allVideoPath.indexOf(filePath) - 1; //current file - 1
        if (nextFile < 0) {
            nextFile = allVideoPath.size() - 1; // setting final value as next file so that player support  backward loop
        }
        CommonArgs.mediaPlayer.stop(); // stopping before next video play
        return nextFile;
    }

    public void setVolumeDown() {
        CommonArgs.audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
    }

    public void setVolumeUp(int volume) {
        System.out.println("max volume::::: " + CommonArgs.audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        System.out.println("screen height: " + measuredHeight);
        int currentVolume = CommonArgs.audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        if (volume == currentVolume + 1 || volume == currentVolume - 1) {
            CommonArgs.audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_SHOW_UI);
        }
    }

    public void setOriginalSize(ImageButton fitBtn, ImageButton oriBtn) {
        RelativeLayout.LayoutParams videoViewLayoutParams = (RelativeLayout.LayoutParams) CommonArgs.videoView.getLayoutParams();
        videoViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
        videoViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
        videoViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
        videoViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        CommonArgs.videoView.setLayoutParams(videoViewLayoutParams);
        fitBtn.setVisibility(View.VISIBLE);
        oriBtn.setVisibility(View.GONE);
        CommonArgs.notification_txt.setText("ORIGINAL");
    }

    public void setFullscreen(ImageButton fullBtn, ImageButton oriBtn) {
        RelativeLayout.LayoutParams videoViewLayoutParams = (RelativeLayout.LayoutParams) CommonArgs.videoView.getLayoutParams();
        videoViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1);
        videoViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 1);
        videoViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 1);
        videoViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 1);
        CommonArgs.videoView.setLayoutParams(videoViewLayoutParams);
        oriBtn.setVisibility(View.VISIBLE);
        fullBtn.setVisibility(View.GONE);
        CommonArgs.notification_txt.setText("FULLSCREEN");
    }

    public void removeNotificationText(TextView notification_txt) {
        if (!notification_txt.getText().equals("")) {
            notification_txt.setText("");
        }
    }

    public void setScreenSize(Context ctx) {
        WindowManager w = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point size = new Point();
            w.getDefaultDisplay().getSize(size);
            measuredWidth = size.x;
            measuredHeight = size.y;
        } else {
            Display d = w.getDefaultDisplay();
            measuredWidth = d.getWidth();
            measuredHeight = d.getHeight();
        }
    }

}
