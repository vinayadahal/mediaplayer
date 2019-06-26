package com.mediaplayer.listeners

import android.media.MediaPlayer
import android.widget.TextView

import com.mediaplayer.components.PlayBackResume
import com.mediaplayer.components.SubtitleDisplay
import com.mediaplayer.services.MathService
import com.mediaplayer.variables.CommonArgs

class VideoOnPreparedListener : MediaPlayer.OnPreparedListener {

    var totalTime: TextView? = null

    override fun onPrepared(mp: MediaPlayer) {
        CommonArgs.mediaPlayer = mp // used to pause video
        println("media player Set complete :::::: ")
        CommonArgs.duration = mp.duration.toLong()
        totalTime!!.text = MathService.timeFormatter(CommonArgs.duration)
        CommonArgs.seekBar!!.setOnSeekBarChangeListener(VideoOnSeekBarChangeListener())
        CommonArgs.videoView!!.start()
        CommonArgs.isPlaying = true
        val th = object : Thread() {
            override fun run() {
                SubtitleDisplay().showSub()
            }
        }
        th.start()
        th.priority = Thread.MIN_PRIORITY
        showResumeImage(mp)
    }

    fun showResumeImage(mp: MediaPlayer) {
        if (CommonArgs.setBackgroundOnResume!!) {
            PlayBackResume().resumePlayBackAuto(CommonArgs.currentVideoPath)
            CommonArgs.setBackgroundOnResume = false
        }
    }
}
