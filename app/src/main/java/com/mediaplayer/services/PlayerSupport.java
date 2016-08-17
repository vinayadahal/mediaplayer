package com.mediaplayer.services;


import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.mediaplayer.components.Effects;

public class PlayerSupport {

    public void playerScreenTouch(RelativeLayout relativeLayout, final RelativeLayout title_control, final TextView notification_txt) {
        relativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        title_control.setVisibility(View.VISIBLE);
                        break;
                    case MotionEvent.ACTION_UP:
                        new PlayerSupport().removeNotificationText(notification_txt);
                        title_control.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new Effects().fadeOut(title_control);
                                new PlayerSupport().removeNotificationText(notification_txt);
                            }
                        }, 3000);
                        break;
                }
                return true;
            }
        });
    }

    public void updateProgressBar(final ProgressBar progressBar, final VideoView videoView, final long duration) {
        Thread progressUpdate = new Thread() {
            public void run() {
                progressBar.setProgress(0);
                progressBar.setMax(100);
                int i = 0;
                while (progressBar.getProgress() <= 100) {
                    long current = videoView.getCurrentPosition();
                    int currentTimeInSec = (int) (current * 100 / duration);
                    progressBar.setProgress(currentTimeInSec);
                    if (current == 0) {
                        if (i >= 15000) {
                            i = 0;
                            break;
                        }
                    }
                    if (progressBar.getProgress() >= 100) {
                        break;
                    }

                }
            }
        };
        progressUpdate.start();
    }

    public void setOriginalSize(RelativeLayout.LayoutParams layoutParams, VideoView vv, ImageButton fullBtn, ImageButton oriBtn, TextView txtView) {
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        vv.setLayoutParams(layoutParams);
        fullBtn.setVisibility(View.VISIBLE);
        oriBtn.setVisibility(View.GONE);
        txtView.setText("ORIGINAL");
    }

    public void setFullscreen(RelativeLayout.LayoutParams layoutParams, VideoView vv, ImageButton fullBtn, ImageButton oriBtn, TextView txtView) {
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 1);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 1);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 1);
        vv.setLayoutParams(layoutParams);
        oriBtn.setVisibility(View.VISIBLE);
        fullBtn.setVisibility(View.GONE);
        txtView.setText("FULLSCREEN");
    }

    public void removeNotificationText(TextView notification_txt) {
        if (!notification_txt.getText().equals("")) {
            notification_txt.setText("");
        }
    }

}
