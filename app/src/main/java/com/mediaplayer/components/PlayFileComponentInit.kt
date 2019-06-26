package com.mediaplayer.components


import android.app.Activity
import android.content.Context
import android.media.AudioManager
import android.view.View
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.TextView
import android.widget.VideoView

import com.mediaplayer.R
import com.mediaplayer.listeners.BtnOnClickListener
import com.mediaplayer.variables.CommonArgs

class PlayFileComponentInit {

    private val objBtnOnClickListener = BtnOnClickListener()

    fun initPlayFileGlobal() {
        CommonArgs.titleControl = (CommonArgs.playFileCtx as Activity).findViewById<View>(R.id.title_control) as RelativeLayout
        CommonArgs.subArea = (CommonArgs.playFileCtx as Activity).findViewById<View>(R.id.subArea) as TextView
        CommonArgs.seekBar = (CommonArgs.playFileCtx as Activity).findViewById<View>(R.id.vid_seekbar) as SeekBar
        CommonArgs.currentTimeTxt = (CommonArgs.playFileCtx as Activity).findViewById<View>(R.id.current_time) as TextView
        CommonArgs.notification_txt = (CommonArgs.playFileCtx as Activity).findViewById<View>(R.id.notification_txt) as TextView
        CommonArgs.rlPlayFile = (CommonArgs.playFileCtx as Activity).findViewById<View>(R.id.play_file_relative_layout) as RelativeLayout
        CommonArgs.videoView = (CommonArgs.playFileCtx as Activity).findViewById<View>(R.id.videoView) as VideoView
        CommonArgs.audioManager = CommonArgs.playFileCtx!!.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        setGlobalVariable()
    }

    private fun setGlobalVariable() {
        CommonArgs.seekBar!!.progress = 0
        CommonArgs.titleControl!!.visibility = View.GONE
    }

    fun initPlayFileLocal() {
        nextPrevBtnInit()
        volumeBrightBtnInit()
        setVideoViewSize()
        setScreenOrientation()
    }

    private fun nextPrevBtnInit() {
        val nextBtn = (CommonArgs.playFileCtx as Activity).findViewById<View>(R.id.next_btn) as ImageButton
        nextBtn.setOnClickListener(objBtnOnClickListener)
        val previousBtn = (CommonArgs.playFileCtx as Activity).findViewById<View>(R.id.previous_btn) as ImageButton
        previousBtn.setOnClickListener(objBtnOnClickListener)
    }

    private fun volumeBrightBtnInit() {
        val brightnessBtn = (CommonArgs.playFileCtx as Activity).findViewById<View>(R.id.brightness) as ImageButton
        brightnessBtn.setOnClickListener(objBtnOnClickListener)
        val volumeBtn = (CommonArgs.playFileCtx as Activity).findViewById<View>(R.id.volume_btn) as ImageButton
        volumeBtn.setOnClickListener(objBtnOnClickListener)
    }

    private fun setVideoViewSize() {
        val originalBtn = (CommonArgs.playFileCtx as Activity).findViewById<View>(R.id.original_btn) as ImageButton
        objBtnOnClickListener.originalBtn = originalBtn
        originalBtn.setOnClickListener(objBtnOnClickListener)
        originalBtn.visibility = View.GONE //sets init design
        val adjustBtn = (CommonArgs.playFileCtx as Activity).findViewById<View>(R.id.adjust_btn) as ImageButton
        objBtnOnClickListener.adjustBtn = adjustBtn
        adjustBtn.setOnClickListener(objBtnOnClickListener)
        adjustBtn.visibility = View.GONE //sets init design
        val fullscreenBtn = (CommonArgs.playFileCtx as Activity).findViewById<View>(R.id.fullscreen_btn) as ImageButton
        objBtnOnClickListener.fullscreenBtn = fullscreenBtn
        fullscreenBtn.setOnClickListener(objBtnOnClickListener)
    }

    private fun setScreenOrientation() {
        val screenRotationBtn = (CommonArgs.playFileCtx as Activity).findViewById<View>(R.id.screen_rotation) as ImageButton
        objBtnOnClickListener.screenRotationBtn = screenRotationBtn
        screenRotationBtn.setOnClickListener(objBtnOnClickListener)
    }

}
