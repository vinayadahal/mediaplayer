package com.mediaplayer.services;


import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.mediaplayer.components.Effects;

public class PlayerSupport {

    public void playerScreenTouch(RelativeLayout play_file_rl, final RelativeLayout title_control,
                                  final TextView notification_txt, final ProgressBar progressBar,
                                  final VideoView videoView, final long duration, final TextView currentTimeTxt) {
        play_file_rl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        updateProgressBar(progressBar, videoView, duration, title_control);
                        timeUpdate(duration, currentTimeTxt, videoView, title_control);
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

    public void updateProgressBar(final ProgressBar progressBar, final VideoView videoView, final long duration, final RelativeLayout title_control) {
        Thread progressUpdate = new Thread() {
            public void run() {
                progressBar.setProgress(0);
                progressBar.setMax(100);
                int sleepingFor = 0;
                while (progressBar.getProgress() <= 100) {
                    long current = videoView.getCurrentPosition();
                    int currentTimeInSec = (int) (current * 100 / duration);
                    progressBar.setProgress(currentTimeInSec);
                    if (current == 0 || sleepingFor == 6 || progressBar.getProgress() >= 100) {
                        break;
                    }
                    if (title_control.getVisibility() == View.VISIBLE) {
                        if (this.getState() == State.TIMED_WAITING) {
                            notify();
                        }
                    } else {
                        try {
                            sleep(1000);
                            sleepingFor++;
                        } catch (InterruptedException ex) {
                            System.out.println("InterruptedException ::: " + ex);
                        }
                    }
                }
            }
        };
        progressUpdate.start();
    }


    public void timeUpdate(final long duration, final TextView currentTimeTxt, final VideoView videoView, final RelativeLayout title_control) {
        final Handler handler = new Handler();
        Thread th = new Thread() {
            @Override
            public void run() {
                System.out.println("inside thread");
                long current = 0;
                int sleepingFor = 0;
                while (current != duration) {
                    current = videoView.getCurrentPosition();
                    if (sleepingFor == 6 || current == duration) {
                        break;
                    }
                    if (title_control.getVisibility() == View.VISIBLE) {
                        if (this.getState() == State.TIMED_WAITING) {
                            notify();
                        }
                    } else {
                        try {
                            sleep(1000);
                            sleepingFor++;
                        } catch (InterruptedException ex) {
                            System.out.println("InterruptedException ::: " + ex);
                        }
                    }
                    final int minutes = (int) (current / 1000) / 60;
                    final int seconds = (int) (current / 1000) % 60;
                    System.out.println("TIME::: " + minutes + ":" + seconds);
                    handler.post(new Runnable() {
                        public void run() {
                            currentTimeTxt.setText(minutes + ":" + seconds);
                        }
                    });
                    System.out.println("Sleeping for ---------> " + sleepingFor);
                }
            }
        };
        th.start();


//        final Handler handler = new Handler();
//        Runnable runnable = new Runnable() {
//            public void run() {
//                System.out.println("inside thread");
//                long current = 0;
//                int sleepingFor = 0;
//                while (current != duration) {
//                    current = videoView.getCurrentPosition();
//                    if (sleepingFor == 6 || current == duration) {
//                        break;
//                    }
//                    final int minutes = (int) (current / 1000) / 60;
//                    final int seconds = (int) (current / 1000) % 60;
//                    System.out.println("TIME::: " + minutes + ":" + seconds);
//                    handler.post(new Runnable() {
//                        public void run() {
//                            currentTimeTxt.setText(minutes + ":" + seconds);
//                        }
//                    });
//                    System.out.println("Sleeping for ---------> " + sleepingFor++);
//                }
//            }
//        };
//        new Thread(runnable).start();
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
