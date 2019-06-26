package com.mediaplayer.listeners

import android.app.Activity
import android.content.pm.ActivityInfo
import android.view.View
import android.widget.ImageButton

import com.mediaplayer.R
import com.mediaplayer.activites.PlayFile
import com.mediaplayer.components.PlayBackResume
import com.mediaplayer.components.PopUpDialog
import com.mediaplayer.services.MediaControl
import com.mediaplayer.variables.CommonArgs


class BtnOnClickListener : View.OnClickListener {

    var screen_rotation_btn: ImageButton? = null
    var original_btn: ImageButton? = null
    var adjust_btn: ImageButton? = null
    var fullscreen_btn: ImageButton? = null

    override fun onClick(v: View) {
        val objMediaControl = MediaControl()
        when (v.id) {
            R.id.screen_rotation -> {
                CommonArgs.title_control_handler.removeCallbacks(CommonArgs.title_control_runnable) // removing callback to show view during next and prev
                if ((CommonArgs.playFileCtx as Activity).requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                    (CommonArgs.playFileCtx as Activity).requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                } else {
                    (CommonArgs.playFileCtx as Activity).requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                }
                PlayFileTouchListener().hideTitleControl() // hiding view after 3 sec
            }
            R.id.brightness -> PopUpDialog().showBrightnessDialog()
            R.id.previous_btn -> {
                CommonArgs.title_control_handler.removeCallbacks(CommonArgs.title_control_runnable) // removing callback to show view during next and prev
                val videoPath = CommonArgs.currentVideoPath
                val currentPos = CommonArgs.videoView!!.currentPosition
                PlayBackResume().setResumePoint(videoPath, currentPos)
                CommonArgs.isPlaying = false
                val nxtFile = MediaControl().previousBtnAction(CommonArgs.allVideoPath, CommonArgs.currentVideoPath)
                (CommonArgs.playFileCtx as PlayFile).playFile(CommonArgs.allVideoPath!![nxtFile]) // playing new file
                CommonArgs.currentVideoPath = CommonArgs.allVideoPath!![nxtFile] // recording current playing file for future use
                CommonArgs.handler.postDelayed(CommonArgs.runnable, 500) // updating progress bar after pressing next/prev button
                PlayFileTouchListener().hideTitleControl() // hiding view after 3 sec
            }
            R.id.next_btn -> {
                CommonArgs.title_control_handler.removeCallbacks(CommonArgs.title_control_runnable)// removing callback to show view during next and prev
                val vidPath = CommonArgs.currentVideoPath
                val currentPosition = CommonArgs.videoView!!.currentPosition
                PlayBackResume().setResumePoint(vidPath, currentPosition)
                CommonArgs.isPlaying = false
                val nextFile = MediaControl().nextBtnAction(CommonArgs.allVideoPath, CommonArgs.currentVideoPath) // gets next file array's index
                (CommonArgs.playFileCtx as PlayFile).playFile(CommonArgs.allVideoPath!![nextFile]) // playing new file
                CommonArgs.currentVideoPath = CommonArgs.allVideoPath!![nextFile] // recording current playing file for future use
                CommonArgs.handler.postDelayed(CommonArgs.runnable, 500) // updating progress bar after pressing next/prev button
                PlayFileTouchListener().hideTitleControl() // hiding view after 3 sec
            }
            R.id.volume_btn -> PopUpDialog().showVolumeDialog()
            R.id.fullscreen_btn -> {
                CommonArgs.title_control_handler.removeCallbacks(CommonArgs.title_control_runnable)// removing callback to show view during next and prev
                objMediaControl.setFullscreen(fullscreen_btn, adjust_btn)
                PlayFileTouchListener().hideTitleControl() // hiding view after 3 sec
            }
            R.id.adjust_btn -> {
                CommonArgs.title_control_handler.removeCallbacks(CommonArgs.title_control_runnable)// removing callback to show view during next and prev
                objMediaControl.setAdjustscreen(adjust_btn, original_btn)
                PlayFileTouchListener().hideTitleControl() // hiding view after 3 sec
            }
            R.id.original_btn -> {
                CommonArgs.title_control_handler.removeCallbacks(CommonArgs.title_control_runnable)// removing callback to show view during next and prev
                objMediaControl.setOriginalSize(fullscreen_btn, original_btn)
                PlayFileTouchListener().hideTitleControl() // hiding view after 3 sec
            }
        }

    }

}
