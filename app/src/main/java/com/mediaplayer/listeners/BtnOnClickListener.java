package com.mediaplayer.listeners;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;

import com.mediaplayer.R;
import com.mediaplayer.activites.PlayFile;
import com.mediaplayer.components.PlayBackResume;
import com.mediaplayer.components.SeekBarVisibility;
import com.mediaplayer.services.MediaControl;
import com.mediaplayer.variables.CommonArgs;


public class BtnOnClickListener implements View.OnClickListener {

    public ImageButton fullscreen_btn, portrait_btn, original_btn, landscape_btn;

    @Override
    public void onClick(View v) {
        MediaControl objMediaControl = new MediaControl();
        switch (v.getId()) {
            case R.id.portrait_btn:
                portrait_btn.setVisibility(View.GONE);
                landscape_btn.setVisibility(View.VISIBLE);
                ((Activity) CommonArgs.playFileCtx).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;
            case R.id.landscape_btn:
                landscape_btn.setVisibility(View.GONE);
                portrait_btn.setVisibility(View.VISIBLE);
                ((Activity) CommonArgs.playFileCtx).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;
            case R.id.brightness:
                SeekBarVisibility.showBrightnessSeekBar();
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
                new PlayFileTouchListener().hideTitleControl(); // hiding view after 3 sec
                break;
            case R.id.volume_btn:
                SeekBarVisibility.showVolumeSeekbar();
                break;
            case R.id.fullscreen_btn:
                objMediaControl.setFullscreen(fullscreen_btn, original_btn);
                break;
            case R.id.original_btn:
                objMediaControl.setOriginalSize(fullscreen_btn, original_btn);
                break;
        }

    }

}
