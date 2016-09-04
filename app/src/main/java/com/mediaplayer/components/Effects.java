package com.mediaplayer.components;

import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

public class Effects {

    public void fadeIn(final RelativeLayout layout) {
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new AccelerateInterpolator());
        fadeIn.setDuration(200);
        layout.startAnimation(fadeIn);
        layout.setVisibility(View.VISIBLE);
    }

    public void fadeOut(final RelativeLayout layout) {
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(200);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
                layout.setVisibility(View.GONE);
            }
        });
        layout.startAnimation(fadeOut);
    }
}
