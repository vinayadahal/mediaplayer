package com.mediaplayer.services;


import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.mediaplayer.components.Effects;
import com.mediaplayer.variables.CommonArgs;

public class PlayerSupport {

    private Runnable runnable;
    private Handler handler = new Handler();
    float startx, starty;
    float endx, endy;
    float sumx, sumy;
    int countVol = 0;
    Boolean isViewOn = false;

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
                        if (!checkTitleControlVisibility()) {
                            CommonArgs.title_control.setVisibility(View.VISIBLE);
                            System.out.println("view is on is setting");
                            isViewOn = true;
                        }
                        startx = event.getX();
                        starty = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (checkTitleControlVisibility()) {
                            updateProgressBar();
                            CommonArgs.title_control.setVisibility(View.VISIBLE);
                        }
                        CommonArgs.title_control.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (!isViewOn) {
                                    System.out.println("called if view is false");
                                    new Effects().fadeOut(CommonArgs.title_control);
                                    new MediaControl().removeNotificationText(CommonArgs.notification_txt);
                                    System.out.println("ending from ACTION_UP");
                                    handler.removeCallbacks(runnable); // stops running runnable
                                } else {
                                    isViewOn = false;
                                }
                            }
                        }, 3000);
                        System.out.println("up event------------------->");

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
                System.out.println("Hello World");
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
                    if (checkTitleControlVisibility()) {
                        System.out.println("ending the world");
                        handler.removeCallbacks(runnable);// stops running runnable
                        System.out.println("call back should be removed");
                        handler.removeCallbacksAndMessages(null);
                        return;
                    }
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
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
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
            System.out.println("distance " + (int) sumy / 48);
            new MediaControl().setVolumeUp();
            countVol++;
        }
        if (sumx >= 100 && sumy < sumx) {
            System.out.println("You may have swiped right to left");
        }
        if (sumx <= -100 && sumy > sumx) {
            System.out.println("You may have swiped left to right");
        }
        if (sumy <= -100 && sumx > sumy) {
            System.out.println("You may have swiped top to bottom");
            new MediaControl().setVoluemDown();
        }
        System.out.println("sumx " + sumx);
        System.out.println("sumy " + sumy);
    }

}
