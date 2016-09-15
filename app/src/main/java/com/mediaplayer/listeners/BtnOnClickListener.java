package com.mediaplayer.listeners;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.view.View;
import android.widget.ImageButton;

import com.mediaplayer.R;
import com.mediaplayer.activites.PlayFile;
import com.mediaplayer.components.PlayBackResume;
import com.mediaplayer.components.PopUpDialog;
import com.mediaplayer.services.MediaControl;
import com.mediaplayer.variables.CommonArgs;


public class BtnOnClickListener implements View.OnClickListener {

    public ImageButton screen_rotation_btn, original_btn, adjust_btn, fullscreen_btn;

    @Override
    public void onClick(View v) {
        MediaControl objMediaControl = new MediaControl();
        switch (v.getId()) {
            case R.id.screen_rotation:
                CommonArgs.title_control_handler.removeCallbacks(CommonArgs.title_control_runnable); // removing callback to show view during next and prev
                if (((Activity) CommonArgs.playFileCtx).getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                    ((Activity) CommonArgs.playFileCtx).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else {
                    ((Activity) CommonArgs.playFileCtx).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
                new PlayFileTouchListener().hideTitleControl(); // hiding view after 3 sec
                break;
            case R.id.brightness:
                new PopUpDialog().showBrightnessDialog();
                break;
            case R.id.previous_btn:
                CommonArgs.title_control_handler.removeCallbacks(CommonArgs.title_control_runnable); // removing callback to show view during next and prev
                String videoPath = CommonArgs.currentVideoPath;
                int currentPos = CommonArgs.videoView.getCurrentPosition();
                new PlayBackResume().setResumePoint(videoPath, currentPos);
                CommonArgs.isPlaying = false;
                int nxtFile = new MediaControl().previousBtnAction(CommonArgs.allVideoPath, CommonArgs.currentVideoPath);
                ((PlayFile) CommonArgs.playFileCtx).playFile(CommonArgs.allVideoPath.get(nxtFile)); // playing new file
                CommonArgs.currentVideoPath = CommonArgs.allVideoPath.get(nxtFile); // recording current playing file for future use
                CommonArgs.handler.postDelayed(CommonArgs.runnable, 500); // updating progress bar after pressing next/prev button
                new PlayFileTouchListener().hideTitleControl(); // hiding view after 3 sec
                break;
            case R.id.next_btn:
                CommonArgs.title_control_handler.removeCallbacks(CommonArgs.title_control_runnable);// removing callback to show view during next and prev
                String vidPath = CommonArgs.currentVideoPath;
                int currentPosition = CommonArgs.videoView.getCurrentPosition();
                new PlayBackResume().setResumePoint(vidPath, currentPosition);
                CommonArgs.isPlaying = false;
                int nextFile = new MediaControl().nextBtnAction(CommonArgs.allVideoPath, CommonArgs.currentVideoPath); // gets next file array's index
                ((PlayFile) CommonArgs.playFileCtx).playFile(CommonArgs.allVideoPath.get(nextFile)); // playing new file
                CommonArgs.currentVideoPath = CommonArgs.allVideoPath.get(nextFile); // recording current playing file for future use
                CommonArgs.handler.postDelayed(CommonArgs.runnable, 500); // updating progress bar after pressing next/prev button
                new PlayFileTouchListener().hideTitleControl(); // hiding view after 3 sec
                break;
            case R.id.volume_btn:
                new PopUpDialog().showVolumeDialog();
                break;
            case R.id.fullscreen_btn:
                CommonArgs.title_control_handler.removeCallbacks(CommonArgs.title_control_runnable);// removing callback to show view during next and prev
                objMediaControl.setFullscreen(fullscreen_btn, adjust_btn);
                new PlayFileTouchListener().hideTitleControl(); // hiding view after 3 sec
                break;
            case R.id.adjust_btn:
                CommonArgs.title_control_handler.removeCallbacks(CommonArgs.title_control_runnable);// removing callback to show view during next and prev
                objMediaControl.setAdjustscreen(adjust_btn, original_btn);
                new PlayFileTouchListener().hideTitleControl(); // hiding view after 3 sec
                break;
            case R.id.original_btn:
                CommonArgs.title_control_handler.removeCallbacks(CommonArgs.title_control_runnable);// removing callback to show view during next and prev
                objMediaControl.setOriginalSize(fullscreen_btn, original_btn);
                new PlayFileTouchListener().hideTitleControl(); // hiding view after 3 sec
                break;
        }

    }

}
