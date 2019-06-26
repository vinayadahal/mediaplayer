package com.mediaplayer.listeners


import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View

import com.mediaplayer.components.Effects
import com.mediaplayer.services.MathService
import com.mediaplayer.services.MediaControl
import com.mediaplayer.variables.CommonArgs

class PlayFileTouchListener : View.OnTouchListener {

    private var isViewOn: Boolean? = false
    private var skipTimer: Boolean? = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> if ((!checkTitleControlVisibility()!!)) {
                CommonArgs.titleControl!!.visibility = View.VISIBLE
                isViewOn = true
            }
            MotionEvent.ACTION_UP -> {
                if (checkTitleControlVisibility()!! && (!isViewOn!!)) {
                    Effects().fadeIn(CommonArgs.titleControl)
                    updateProgressBar()
                    skipTimer = false
                    if (!CommonArgs.mediaPlayer!!.isPlaying) {
                        skipTimer = true // setting true to make view visible during pause.
                    }
                } else if ((!checkTitleControlVisibility()!!)) {
                    hideTitleControlNormal()
                    skipTimer = true
                }
                if ((!skipTimer!!)) {
                    hideTitleControl()
                }
            }
        }
        return true
    }

    private fun updateProgressBar() {
        CommonArgs.runnable = object : Runnable {
            override fun run() {
                val thread = Thread.currentThread()
                thread.priority = Thread.MIN_PRIORITY
                println("Seekbar runnable running")
                CommonArgs.seekBar!!.max = CommonArgs.duration.toInt()
                if (CommonArgs.seekBar!!.progress <= CommonArgs.duration.toInt() && (!checkTitleControlVisibility()!!)) {
                    if (!CommonArgs.videoView!!.isPlaying) {
                        return
                    }
                    val current = CommonArgs.videoView!!.currentPosition.toLong()
                    CommonArgs.seekBar!!.progress = current.toInt()
                    CommonArgs.handler.post { CommonArgs.currentTimeTxt!!.text = MathService.timeFormatter(current) }
                }
                if (checkTitleControlVisibility()!!) {
                    CommonArgs.handler.removeCallbacks(this)// stops running runnable
                    CommonArgs.handler.removeCallbacksAndMessages(null)
                    println("Seekbar runnable should be stopped")
                    return
                }
                CommonArgs.handler.postDelayed(this, 1000)
            }
        }
        CommonArgs.handler.postDelayed(CommonArgs.runnable, 10)
    }

    fun checkTitleControlVisibility(): Boolean? {
        return (CommonArgs.titleControl!!.visibility != View.VISIBLE)
    }

    private fun hideTitleControlNormal() {
        Effects().fadeOut(CommonArgs.titleControl)
        MediaControl().removeNotificationText(CommonArgs.notification_txt)
        CommonArgs.handler.removeCallbacks(CommonArgs.runnable) // stops running runnable
        CommonArgs.titleControlHandler.removeCallbacks(CommonArgs.titleControlRunnable) // prevents currently running hideTitleControl
        isViewOn = false
    }

    fun hideTitleControl() {
        CommonArgs.titleControlRunnable = Runnable {
            if ((!isViewOn!!)) {
                Effects().fadeOut(CommonArgs.titleControl)
                MediaControl().removeNotificationText(CommonArgs.notification_txt)
                CommonArgs.handler.removeCallbacks(CommonArgs.runnable) // stops running runnable
            } else {
                isViewOn = false
            }
            skipTimer = false
        }
        CommonArgs.titleControlHandler.postDelayed(CommonArgs.titleControlRunnable, 3000)
    }

}
