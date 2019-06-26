package com.mediaplayer.listeners


import android.widget.SeekBar
import com.mediaplayer.activites.PlayFile
import com.mediaplayer.variables.CommonArgs

class BrightnessOnSeekBarChangeListener : SeekBar.OnSeekBarChangeListener {

    private val playFile = CommonArgs.playFileCtx as PlayFile
    private val lp = playFile.window.attributes

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        val progress = progress.toFloat() / 10
        lp.screenBrightness = progress
        if (progress > 0) {
            playFile.window.attributes = lp
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {}

    override fun onStopTrackingTouch(seekBar: SeekBar) {
        if (lp.screenBrightness < 0) {
            playFile.window.attributes = lp
        }
    }

}
