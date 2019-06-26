package com.mediaplayer.listeners


import android.view.MotionEvent
import android.view.View

import com.mediaplayer.components.Effects
import com.mediaplayer.services.MathService
import com.mediaplayer.services.MediaControl
import com.mediaplayer.variables.CommonArgs

class PlayFileTouchListener : View.OnTouchListener {

    var isViewOn: Boolean? = false
    var skipTimer: Boolean? = false

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> if ((!checkTitleControlVisibility()!!)!!) {
                CommonArgs.title_control!!.visibility = View.VISIBLE
                isViewOn = true
            }
            MotionEvent.ACTION_UP -> {
                if (checkTitleControlVisibility()!! && (!isViewOn!!)!!) {
                    Effects().fadeIn(CommonArgs.title_control)
                    updateProgressBar()
                    skipTimer = false
                    if (!CommonArgs.mediaPlayer!!.isPlaying) {
                        skipTimer = true // setting true to make view visible during pause.
                    }
                } else if ((!checkTitleControlVisibility()!!)!!) {
                    hideTitleControlNormal()
                    skipTimer = true
                }
                if ((!skipTimer!!)!!) {
                    hideTitleControl()
                }
            }
        }
        return true
    }

    fun updateProgressBar() {
        CommonArgs.runnable = object : Runnable {
            override fun run() {
                val thread = Thread.currentThread()
                thread.priority = Thread.MIN_PRIORITY
                println("Seekbar runnable running")
                CommonArgs.seekBar!!.max = CommonArgs.duration.toInt()
                if (CommonArgs.seekBar!!.progress <= CommonArgs.duration.toInt() && (!checkTitleControlVisibility()!!)!!) {
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
        return (CommonArgs.title_control!!.visibility != View.VISIBLE)
    }

    fun hideTitleControlNormal() {
        Effects().fadeOut(CommonArgs.title_control)
        MediaControl().removeNotificationText(CommonArgs.notification_txt)
        CommonArgs.handler.removeCallbacks(CommonArgs.runnable) // stops running runnable
        CommonArgs.title_control_handler.removeCallbacks(CommonArgs.title_control_runnable) // prevents currently running hideTitleControl
        isViewOn = false
    }

    fun hideTitleControl() {
        CommonArgs.title_control_runnable = Runnable {
            if ((!isViewOn!!)!!) {
                Effects().fadeOut(CommonArgs.title_control)
                MediaControl().removeNotificationText(CommonArgs.notification_txt)
                CommonArgs.handler.removeCallbacks(CommonArgs.runnable) // stops running runnable
            } else {
                isViewOn = false
            }
            skipTimer = false
        }
        CommonArgs.title_control_handler.postDelayed(CommonArgs.title_control_runnable, 3000)
    }

}
