package com.mediaplayer.listeners;


import android.view.MotionEvent;
import android.view.View;

import com.mediaplayer.components.Effects;
import com.mediaplayer.services.MathService;
import com.mediaplayer.services.MediaControl;
import com.mediaplayer.variables.CommonArgs;

public class PlayFileTouchListener implements View.OnTouchListener {

    public Boolean isViewOn = false, skipTimer = false;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!checkTitleControlVisibility()) {
                    CommonArgs.title_control.setVisibility(View.VISIBLE);
                    isViewOn = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (checkTitleControlVisibility() && !isViewOn) {
                    new Effects().fadeIn(CommonArgs.title_control);
                    updateProgressBar();
                    skipTimer = false;
                } else if (!checkTitleControlVisibility()) {
                    hideTitleControlNormal();
                    skipTimer = true;
                }
                if (!skipTimer) {
                    hideTitleControl();
                }
                break;
        }
        return true;
    }

    public void updateProgressBar() {
        CommonArgs.runnable = new Runnable() {
            public void run() {
                Thread thread= Thread.currentThread();
                thread.setPriority(Thread.MIN_PRIORITY);
                System.out.println("Seekbar runnable running");
                CommonArgs.seekBar.setMax((int) CommonArgs.duration);
                if (CommonArgs.seekBar.getProgress() <= (int) CommonArgs.duration && !checkTitleControlVisibility()) {
                    if (!CommonArgs.videoView.isPlaying()) {
                        return;
                    }
                    final long current = CommonArgs.videoView.getCurrentPosition();
                    CommonArgs.seekBar.setProgress((int) current);
                    CommonArgs.handler.post(new Runnable() {
                        public void run() {
                            CommonArgs.currentTimeTxt.setText(MathService.timeFormatter(current));
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

    public void hideTitleControlNormal() {
        new Effects().fadeOut(CommonArgs.title_control);
        new MediaControl().removeNotificationText(CommonArgs.notification_txt);
        CommonArgs.handler.removeCallbacks(CommonArgs.runnable); // stops running runnable
        CommonArgs.title_control_handler.removeCallbacks(CommonArgs.title_control_runnable); // prevents currently running hideTitleControl
        isViewOn = false;
    }

    public void hideTitleControl() {
        CommonArgs.title_control_runnable = new Runnable() {
            @Override
            public void run() {
                if (!isViewOn) {
                    new Effects().fadeOut(CommonArgs.title_control);
                    new MediaControl().removeNotificationText(CommonArgs.notification_txt);
                    CommonArgs.handler.removeCallbacks(CommonArgs.runnable); // stops running runnable
                } else {
                    isViewOn = false;
                }
                skipTimer = false;
            }
        };
        CommonArgs.title_control_handler.postDelayed(CommonArgs.title_control_runnable, 3000);
    }

}
