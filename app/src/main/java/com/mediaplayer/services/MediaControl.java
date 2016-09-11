package com.mediaplayer.services;

import android.media.AudioManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mediaplayer.variables.CommonArgs;

import java.util.List;

public class MediaControl {

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

    public void setVolume(int volume) {
        CommonArgs.audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
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
        System.out.println("Setting Full Screen:::::: ");
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
}
