package com.mediaplayer.activites;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.mediaplayer.R;

import org.w3c.dom.Text;

public class PlayFile extends AppCompatActivity {

    //    private LinearLayout vid_header;
//    private LinearLayout playback_control;
    private RelativeLayout title_control;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //hides notification bar
        setContentView(R.layout.activity_play_file);
        Bundle b = getIntent().getExtras();
        String filePath = null; // or other values
        String videoFileName = null;
        if (b != null) {
            filePath = b.getString("fileName");
            videoFileName = b.getString("videoFileName");
        }
        playFile(filePath, videoFileName);
    }

    public void playFile(String filename, String videoFileName) {
        title_control = (RelativeLayout) findViewById(R.id.title_control);
        title_control.setVisibility(View.INVISIBLE);
        TextView vidTitle = (TextView) findViewById(R.id.vid_title);
        vidTitle.setText("Now Playing: " + videoFileName);
        VideoView vv = (VideoView) findViewById(R.id.videoView1);
        vv.setVideoURI(Uri.parse(filename));
        vv.start();
        vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                finish();
            }
        });
    }

    public void screenTouch(View view) {
//        fadeIn(title_control);
        title_control.setVisibility(View.VISIBLE);
        title_control.postDelayed(new Runnable() {
            @Override
            public void run() {
                fadeOut(title_control);
            }
        }, 3000);
    }


    private void fadeOut(final RelativeLayout layout) {
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(1000);

        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                layout.setVisibility(View.GONE);
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }
        });

        layout.startAnimation(fadeOut);
    }

}
