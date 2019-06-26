package com.mediaplayer.listeners

import android.view.View
import android.widget.SeekBar

import com.mediaplayer.services.MathService
import com.mediaplayer.variables.CommonArgs


class VideoOnSeekBarChangeListener : SeekBar.OnSeekBarChangeListener {
    private var showTimer: Boolean? = false

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        if (this.showTimer!!) {
            CommonArgs.notification_txt!!.visibility = View.VISIBLE
            CommonArgs.notification_txt!!.text = MathService.timeFormatter(progress.toLong())
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {
        CommonArgs.titleControlHandler.removeCallbacks(CommonArgs.titleControlRunnable)
        showTimer = true
    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
        CommonArgs.mediaPlayer!!.seekTo(seekBar.progress)
        PlayFileTouchListener().hideTitleControl()
        CommonArgs.notification_txt!!.visibility = View.GONE
        showTimer = false
    }
}
