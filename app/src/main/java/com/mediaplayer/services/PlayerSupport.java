package com.mediaplayer.services;


import android.media.MediaPlayer;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.mediaplayer.components.Effects;
import com.mediaplayer.variables.ArgsPlayerSupport;

public class PlayerSupport {

    int sleepingForProgress = 0;

    public void playerScreenTouch() {
        ArgsPlayerSupport.rl_play_file.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        updateProgressBar();
                        ArgsPlayerSupport.title_control.setVisibility(View.VISIBLE);
                        break;
                    case MotionEvent.ACTION_UP:
                        new PlayerSupport().removeNotificationText(ArgsPlayerSupport.notification_txt);
                        ArgsPlayerSupport.title_control.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new Effects().fadeOut(ArgsPlayerSupport.title_control);
                                new PlayerSupport().removeNotificationText(ArgsPlayerSupport.notification_txt);
                            }
                        }, 5000);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        ArgsPlayerSupport.title_control.setVisibility(View.VISIBLE);
                        break;
                }
                return true;
            }
        });
    }


    public void updateProgressBar() {
        final Handler handler = new Handler();
        Thread progressUpdate = new Thread() {
            public void run() {
                ArgsPlayerSupport.seekBar.setProgress(0);
                ArgsPlayerSupport.seekBar.setMax(100);
                while (ArgsPlayerSupport.seekBar.getProgress() <= 100) {
                    final long current = ArgsPlayerSupport.videoView.getCurrentPosition();
                    int currentTimeInSec = (int) (current * 100 / ArgsPlayerSupport.duration);
                    ArgsPlayerSupport.seekBar.setProgress(currentTimeInSec);
                    if (!ArgsPlayerSupport.videoView.isPlaying() || sleepingForProgress == 2) {
                        sleepingForProgress = 0;
                        break;
                    }
                    handler.post(new Runnable() {
                        public void run() {
                            ArgsPlayerSupport.currentTimeTxt.setText(timeFormatter(current));
                        }
                    });
                    threadSleep(this);
                }
            }
        };
        progressUpdate.start();
    }

    public void threadSleep(Thread thread) {
        if (ArgsPlayerSupport.title_control.getVisibility() == View.VISIBLE) {
            if (thread.getState() == Thread.State.TIMED_WAITING) {
                thread.notify();
            }
        } else {
            try {
                thread.sleep(1000);
                sleepingForProgress++;
            } catch (InterruptedException ex) {
                System.out.println("InterruptedException ::: " + ex);
            }
        }
    }

    public void setOriginalSize(ImageButton fullBtn, ImageButton oriBtn) {
        RelativeLayout.LayoutParams videoViewLayoutParams = (RelativeLayout.LayoutParams) ArgsPlayerSupport.videoView.getLayoutParams();
        videoViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
        videoViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
        videoViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
        videoViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        ArgsPlayerSupport.videoView.setLayoutParams(videoViewLayoutParams);
        fullBtn.setVisibility(View.VISIBLE);
        oriBtn.setVisibility(View.GONE);
        ArgsPlayerSupport.notification_txt.setText("ORIGINAL");
    }

    public void setFullscreen(ImageButton fullBtn, ImageButton oriBtn) {
        RelativeLayout.LayoutParams videoViewLayoutParams = (RelativeLayout.LayoutParams) ArgsPlayerSupport.videoView.getLayoutParams();
        videoViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1);
        videoViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 1);
        videoViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 1);
        videoViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 1);
        ArgsPlayerSupport.videoView.setLayoutParams(videoViewLayoutParams);
        oriBtn.setVisibility(View.VISIBLE);
        fullBtn.setVisibility(View.GONE);
        ArgsPlayerSupport.notification_txt.setText("FULLSCREEN");
    }

    public void removeNotificationText(TextView notification_txt) {
        if (!notification_txt.getText().equals("")) {
            notification_txt.setText("");
        }
    }

    public String timeFormatter(long duration) {
        long second = (duration / 1000) % 60;
        long minute = (duration / (1000 * 60)) % 60;
        long hour = (duration / (1000 * 60 * 60)) % 24;
        return String.format("%02d:%02d:%02d", hour, minute, second);
    }

    public void seekToBar(SeekBar seekBar, final MediaPlayer mediaPlayer, final long totalTime) {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                long videoTime = (seekBar.getProgress() * totalTime) / 100;
                mediaPlayer.seekTo((int) videoTime);
            }
        });
    }
}
