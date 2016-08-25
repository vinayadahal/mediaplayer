package com.mediaplayer.services;


import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.mediaplayer.variables.CommonArgs;

public class PlayerSupport {

    private Runnable runnable, title_control_runnable;
    private Handler handler = new Handler(), title_control_handler = new Handler();
    float startx, starty;
    float endx, endy, sumx, sumy;
    private Boolean isViewOn = false, flag = false;
    int oldNum = 0;

    public void setVideoViewListeners(final Context ctx, final TextView totalTime) {
        CommonArgs.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                CommonArgs.duration = CommonArgs.videoView.getDuration();
                CommonArgs.mediaPlayer = mp; // used to pause video
                totalTime.setText(new MathService().timeFormatter(CommonArgs.duration));
                seekToBar(mp);
            }
        });
        CommonArgs.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                ((Activity) ctx).finish();
            }
        });
    }

    public void playerScreenTouch() {
        CommonArgs.rl_play_file.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startx = event.getX();
                        starty = event.getY();
                        if (!checkTitleControlVisibility()) {
                            CommonArgs.title_control.setVisibility(View.VISIBLE);
                            System.out.println("view on is setting");
                            isViewOn = true;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (checkTitleControlVisibility() && !isViewOn) {
                            updateProgressBar();
                            CommonArgs.title_control.setVisibility(View.VISIBLE);
                        }
                        System.out.println("up event------------------->");
                        hideTitleControl();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        endx = event.getX();
                        endy = event.getY();
                        getSiwpeArea();
                        break;
                }
                return true;
            }
        });
    }

    public void updateProgressBar() {
        runnable = new Runnable() {
            public void run() {
                System.out.println("Seekbar runnable running");
                CommonArgs.seekBar.setProgress(0);
                CommonArgs.seekBar.setMax((int) CommonArgs.duration);
                if (CommonArgs.seekBar.getProgress() <= (int) CommonArgs.duration && !checkTitleControlVisibility()) {
                    final long current = CommonArgs.videoView.getCurrentPosition();
                    int currentTimeInSec = (int) current;
                    CommonArgs.seekBar.setProgress(currentTimeInSec);
                    handler.post(new Runnable() {
                        public void run() {
                            CommonArgs.currentTimeTxt.setText(new MathService().timeFormatter(current));
                        }
                    });
                }
                if (checkTitleControlVisibility()) {
                    handler.removeCallbacks(this);// stops running runnable
                    handler.removeCallbacksAndMessages(null);
                    System.out.println("Seekbar runnable should be stopped");
                    return;
                }
                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(runnable, 10);
    }

    public Boolean checkTitleControlVisibility() {
        if (CommonArgs.title_control.getVisibility() != View.VISIBLE) {
            return true;
        } else {
            return false;
        }
    }

    public void seekToBar(final MediaPlayer mediaPlayer) {
        CommonArgs.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                title_control_handler.removeCallbacks(title_control_runnable);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
                hideTitleControl();
            }
        });
    }

    public void hideTitleControl() {
        title_control_runnable = new Runnable() {
            @Override
            public void run() {
                if (!isViewOn) {
                    CommonArgs.title_control.setVisibility(View.GONE);
                    new MediaControl().removeNotificationText(CommonArgs.notification_txt);
                    System.out.println("runnable is running ");
                    handler.removeCallbacks(runnable); // stops running runnable
                } else {
                    isViewOn = false;
                }
            }
        };
        title_control_handler.postDelayed(title_control_runnable, 3000);
    }

    public void getSiwpeArea() {
        if (startx < endx) {
            sumx = startx - endx;
        }
        if (startx > endx) {
            sumx = startx - endx;
        }
        if (starty < endy) {
            sumy = starty - endy;
        }
        if (starty > endy) {
            sumy = starty - endy;
        }
        if (sumy >= 100 && sumx < sumy) {
            System.out.println("You may have swiped bottom to top");
            System.out.println("distance " + (int) sumy / 8);
            if (!flag && oldNum != (int) sumy / 8) {
                new MediaControl().setVolumeUp();
                oldNum = (int) sumy / 8;
            }
        }
        if (sumx >= 100 && sumy < sumx) {
            System.out.println("You may have swiped right to left");
        }
        if (sumx <= -100 && sumy > sumx) {
            System.out.println("You may have swiped left to right");
        }
        if (sumy <= -100 && sumx > sumy) {
            System.out.println("You may have swiped top to bottom");
            if (!flag && oldNum != (int) sumy / 8) {
                new MediaControl().setVolumeDown();
                oldNum = (int) sumy / 8;
            }
        }
        System.out.println("sumx " + sumx);
        System.out.println("sumy " + sumy);
    }

}
