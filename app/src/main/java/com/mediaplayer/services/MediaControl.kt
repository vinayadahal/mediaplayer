package com.mediaplayer.services

import android.app.Activity
import android.media.AudioManager
import android.view.View
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView

import com.mediaplayer.variables.CommonArgs

class MediaControl {

    private val videoViewLayoutParams = CommonArgs.videoView!!.layoutParams as RelativeLayout.LayoutParams

    fun nextBtnAction(allVideoPath: List<String>?, filePath: String?): Int {
        var nextFile = allVideoPath!!.indexOf(filePath) + 1 //current file + 1
        if (nextFile > allVideoPath.size - 1) {
            nextFile = 0 // setting 0 as next file so that player support forward loop
        }
        CommonArgs.mediaPlayer!!.stop() // stopping before next video play
        return nextFile
    }

    fun previousBtnAction(allVideoPath: List<String>?, filePath: String?): Int {
        var nextFile = allVideoPath!!.indexOf(filePath) - 1 //current file - 1
        if (nextFile < 0) {
            nextFile = allVideoPath.size - 1 // setting final value as next file so that player support  backward loop
        }
        CommonArgs.mediaPlayer!!.stop() // stopping before next video play
        return nextFile
    }

    fun setVolume(volume: Int) {
        CommonArgs.audioManager!!.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0)
    }

    fun resetView() {
        videoViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0)
        videoViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0)
        videoViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0)
        videoViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
        CommonArgs.videoView!!.layoutParams = videoViewLayoutParams
    }

    fun setOriginalSize(fullBtn: ImageButton?, oriBtn: ImageButton?) {
        resetView()
        val videoWidth = CommonArgs.mediaPlayer!!.videoWidth
        val videoHeight = CommonArgs.mediaPlayer!!.videoHeight
        videoViewLayoutParams.height = videoHeight
        videoViewLayoutParams.width = videoWidth
        CommonArgs.videoView!!.layoutParams = videoViewLayoutParams
        fullBtn!!.visibility = View.VISIBLE
        oriBtn!!.visibility = View.GONE
        CommonArgs.notification_txt!!.text = "ORIGINAL"
    }

    fun setFullscreen(fullBtn: ImageButton?, fitToScreen: ImageButton?) {
        videoViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1)
        videoViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 1)
        videoViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 1)
        videoViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 1)
        CommonArgs.videoView!!.layoutParams = videoViewLayoutParams
        fitToScreen!!.visibility = View.VISIBLE
        fullBtn!!.visibility = View.GONE
        CommonArgs.notification_txt!!.text = "FULLSCREEN"
    }


    fun setAdjustscreen(adjustBtn: ImageButton?, oriBtn: ImageButton?) {
        resetView()
        val videoWidth = CommonArgs.mediaPlayer!!.videoWidth
        val videoHeight = CommonArgs.mediaPlayer!!.videoHeight
        val videoProportion = videoWidth.toFloat() / videoHeight.toFloat()
        val screenWidth = (CommonArgs.playFileCtx as Activity).windowManager.defaultDisplay.width
        val screenHeight = (CommonArgs.playFileCtx as Activity).windowManager.defaultDisplay.height
        val screenProportion = screenWidth.toFloat() / screenHeight.toFloat()
        if (videoProportion > screenProportion) {
            videoViewLayoutParams.width = screenWidth
            videoViewLayoutParams.height = (screenWidth.toFloat() / videoProportion).toInt()
        } else {
            videoViewLayoutParams.width = (videoProportion * screenHeight.toFloat()).toInt()
            videoViewLayoutParams.height = screenHeight
        }
        CommonArgs.videoView!!.layoutParams = videoViewLayoutParams
        oriBtn!!.visibility = View.VISIBLE
        adjustBtn!!.visibility = View.GONE
        CommonArgs.notification_txt!!.text = "ADJUST TO SCREEN"
    }

    fun removeNotificationText(notification_txt: TextView?) {
        if (notification_txt!!.text != "") {
            notification_txt!!.text = ""
        }
    }
}
