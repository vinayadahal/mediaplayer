package com.mediaplayer.listeners;


import android.view.MotionEvent;
import android.view.View;

import com.mediaplayer.services.MathService;
import com.mediaplayer.services.MediaControl;
import com.mediaplayer.variables.CommonArgs;

public class PlayFileTouchListener implements View.OnTouchListener {

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!checkTitleControlVisibility()) {
                    CommonArgs.title_control.setVisibility(View.VISIBLE);
                    CommonArgs.isViewOn = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (checkTitleControlVisibility()) {
                    CommonArgs.title_control.setVisibility(View.VISIBLE);
                    updateProgressBar();
                }
                if (!checkTitleControlVisibility() && CommonArgs.isViewOn) {
                    CommonArgs.title_control.setVisibility(View.GONE);
                    new MediaControl().removeNotificationText(CommonArgs.notification_txt);
                    CommonArgs.handler.removeCallbacks(CommonArgs.runnable); // stops running runnable
                    CommonArgs.title_control_handler.removeCallbacks(CommonArgs.title_control_runnable);
                    CommonArgs.isViewOn = true;
                }
                if (!CommonArgs.isViewOn) {
                    CommonArgs.hideTitleControl();
                    CommonArgs.isViewOn = false;
                }
                break;
        }
        return true;
    }

    public void updateProgressBar() {
        CommonArgs.runnable = new Runnable() {
            public void run() {
                System.out.println("Seekbar runnable running");
                CommonArgs.seekBar.setProgress(0);
                CommonArgs.seekBar.setMax((int) CommonArgs.duration);
                if (CommonArgs.seekBar.getProgress() <= (int) CommonArgs.duration && !checkTitleControlVisibility()) {
                    if (!CommonArgs.videoView.isPlaying()) {
                        return;
                    }
                    final long current = CommonArgs.videoView.getCurrentPosition();
                    int currentTimeInSec = (int) current;
                    CommonArgs.seekBar.setProgress(currentTimeInSec);
                    CommonArgs.handler.post(new Runnable() {
                        public void run() {
                            CommonArgs.currentTimeTxt.setText(new MathService().timeFormatter(current));
                        }
                    });
                }
                if (checkTitleControlVisibility()) {
                    CommonArgs.handler.removeCallbacks(this);// stops running runnable
                    CommonArgs.handler.removeCallbacksAndMessages(null);
                    System.out.println("Seekbar runnable should be stopped");
                    return;
                }
                CommonArgs.handler.postDelayed(this, 1000);
            }
        };
        CommonArgs.handler.postDelayed(CommonArgs.runnable, 10);
    }

    public Boolean checkTitleControlVisibility() {
        if (CommonArgs.title_control.getVisibility() != View.VISIBLE) {
            return true;
        } else {
            return false;
        }
    }
}
