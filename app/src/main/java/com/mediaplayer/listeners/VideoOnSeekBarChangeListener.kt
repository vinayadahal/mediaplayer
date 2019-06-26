package com.mediaplayer.listeners

import android.view.View
import android.widget.SeekBar

import com.mediaplayer.services.MathService
import com.mediaplayer.variables.CommonArgs


class VideoOnSeekBarChangeListener : SeekBar.OnSeekBarChangeListener {
    var showTimer: Boolean? = false

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        if (showTimer!!) {
            CommonArgs.notification_txt!!.visibility = View.VISIBLE
            CommonArgs.notification_txt!!.text = MathService.timeFormatter(progress.toLong())
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {
        CommonArgs.title_control_handler.removeCallbacks(CommonArgs.title_control_runnable)
        showTimer = true
    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
        CommonArgs.mediaPlayer!!.seekTo(seekBar.progress)
        PlayFileTouchListener().hideTitleControl()
        CommonArgs.notification_txt!!.visibility = View.GONE
        showTimer = false
    }
}
