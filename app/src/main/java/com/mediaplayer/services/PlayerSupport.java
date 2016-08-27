package com.mediaplayer.services;


import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v4.view.GestureDetectorCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.mediaplayer.variables.CommonArgs;

public class PlayerSupport {

    private Runnable runnable, title_control_runnable;
    private Handler handler = new Handler(), title_control_handler = new Handler();
    private Boolean isViewOn = false;
    private float xAxis, yAxis, oldVal;

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

    public void playerScreenTouch(final Context ctx) {
        CommonArgs.rl_play_file.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
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
                        xAxis = event.getX();
                        yAxis = event.getY();
                        onSwipeAction();
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

    public void onSwipeAction() {
        CommonArgs.volumeSeekBar.setMax(15);
        int currentVolume = CommonArgs.audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        if (((int) yAxis % 12) == 0) {
            if (yAxis > oldVal) {
                new MediaControl().setVolumeDown();
                CommonArgs.volumeSeekBar.setProgress(currentVolume - 1);
            } else if (yAxis < oldVal) {
                new MediaControl().setVolumeUp();
                CommonArgs.volumeSeekBar.setProgress(currentVolume + 1);
            }
        }
        oldVal = yAxis;
    }

//    public void volumeSeekClick() {
//        CommonArgs.volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//                title_control_handler.removeCallbacks(title_control_runnable);
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                int currProg = seekBar.getProgress();
//                int newVolume=
//                hideTitleControl();
//            }
//        });
//    }

}
