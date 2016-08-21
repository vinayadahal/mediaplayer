package com.mediaplayer.services;


import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mediaplayer.components.Effects;
import com.mediaplayer.variables.CommonArgs;

public class PlayerSupport {

    private Runnable runnable;
    private Handler handler = new Handler();

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
                        if (checkTitleControlVisibilty()) {
                            updateProgressBar();
                            CommonArgs.title_control.setVisibility(View.VISIBLE);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        CommonArgs.title_control.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new Effects().fadeOut(CommonArgs.title_control);
                                new MediaControl().removeNotificationText(CommonArgs.notification_txt);
                                System.out.println("ending from ACTION_UP");
                                handler.removeCallbacks(runnable); // stops running runnable
                            }
                        }, 3000);
                        break;
                    case MotionEvent.ACTION_MOVE:
//                        CommonArgs.title_control.setVisibility(View.VISIBLE);
                        System.out.println("some thing is moving");
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
                if (CommonArgs.seekBar.getProgress() <= (int) CommonArgs.duration) {
                    final long current = CommonArgs.videoView.getCurrentPosition();
                    int currentTimeInSec = (int) current;
                    CommonArgs.seekBar.setProgress(currentTimeInSec);
                    handler.post(new Runnable() {
                        public void run() {
                            CommonArgs.currentTimeTxt.setText(new MathService().timeFormatter(current));
                        }
                    });

                    if (checkTitleControlVisibilty()) {
                        System.out.println("ending the world");
                        handler.removeCallbacks(runnable);// stops running runnable
                    }

                }
                handler.postDelayed(this, 1000);

            }
        };
        handler.postDelayed(runnable, 10);
    }

    public Boolean checkTitleControlVisibilty() {
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

    public void screenSwipe() {

    }
}
