package com.mediaplayer.services;

import android.app.Activity;
import android.media.AudioManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mediaplayer.variables.CommonArgs;

import java.util.List;

public class MediaControl {

    private RelativeLayout.LayoutParams videoViewLayoutParams = (RelativeLayout.LayoutParams) CommonArgs.videoView.getLayoutParams();

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

    public void resetView() {
        videoViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
        videoViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
        videoViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
        videoViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        CommonArgs.videoView.setLayoutParams(videoViewLayoutParams);
    }

    public void setOriginalSize(ImageButton fullBtn, ImageButton oriBtn) {
        resetView();
        int videoWidth = CommonArgs.mediaPlayer.getVideoWidth();
        int videoHeight = CommonArgs.mediaPlayer.getVideoHeight();
        videoViewLayoutParams.height = videoHeight;
        videoViewLayoutParams.width = videoWidth;
        CommonArgs.videoView.setLayoutParams(videoViewLayoutParams);
        fullBtn.setVisibility(View.VISIBLE);
        oriBtn.setVisibility(View.GONE);
        CommonArgs.notification_txt.setText("ORIGINAL");
    }

    public void setFullscreen(ImageButton fullBtn, ImageButton fitToScreen) {
        videoViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1);
        videoViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 1);
        videoViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 1);
        videoViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 1);
        CommonArgs.videoView.setLayoutParams(videoViewLayoutParams);
        fitToScreen.setVisibility(View.VISIBLE);
        fullBtn.setVisibility(View.GONE);
        CommonArgs.notification_txt.setText("FULLSCREEN");
    }


    public void setAdjustscreen(ImageButton adjustBtn, ImageButton oriBtn) {
        resetView();
        int videoWidth = CommonArgs.mediaPlayer.getVideoWidth();
        int videoHeight = CommonArgs.mediaPlayer.getVideoHeight();
        float videoProportion = (float) videoWidth / (float) videoHeight;
        int screenWidth = ((Activity) CommonArgs.playFileCtx).getWindowManager().getDefaultDisplay().getWidth();
        int screenHeight = ((Activity) CommonArgs.playFileCtx).getWindowManager().getDefaultDisplay().getHeight();
        float screenProportion = (float) screenWidth / (float) screenHeight;
        if (videoProportion > screenProportion) {
            videoViewLayoutParams.width = screenWidth;
            videoViewLayoutParams.height = (int) ((float) screenWidth / videoProportion);
        } else {
            videoViewLayoutParams.width = (int) (videoProportion * (float) screenHeight);
            videoViewLayoutParams.height = screenHeight;
        }
        CommonArgs.videoView.setLayoutParams(videoViewLayoutParams);
        oriBtn.setVisibility(View.VISIBLE);
        adjustBtn.setVisibility(View.GONE);
        CommonArgs.notification_txt.setText("ADJUST TO SCREEN");
    }

    public void removeNotificationText(TextView notification_txt) {
        if (!notification_txt.getText().equals("")) {
            notification_txt.setText("");
        }
    }
}
