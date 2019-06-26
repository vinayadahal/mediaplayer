package com.mediaplayer.components

import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.RelativeLayout

class Effects {

    fun fadeIn(layout: RelativeLayout?) {
        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.interpolator = AccelerateInterpolator()
        fadeIn.duration = 200
        layout!!.startAnimation(fadeIn)
        layout!!.visibility = View.VISIBLE
    }

    fun fadeOut(layout: RelativeLayout?) {
        val fadeOut = AlphaAnimation(1f, 0f)
        fadeOut.interpolator = AccelerateInterpolator()
        fadeOut.duration = 200
        fadeOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationEnd(animation: Animation) {}

            override fun onAnimationRepeat(animation: Animation) {}

            override fun onAnimationStart(animation: Animation) {
                layout!!.visibility = View.GONE
            }
        })
        layout!!.startAnimation(fadeOut)
    }
}
