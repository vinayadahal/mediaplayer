package com.mediaplayer.listeners

import android.widget.SeekBar

import com.mediaplayer.services.MediaControl
import com.mediaplayer.variables.CommonArgs

class VolumeOnSeekBarChangeListener : SeekBar.OnSeekBarChangeListener {

    private var isViewOn: Boolean? = false
    private var progress: Int = 0

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        this.progress = progress
        MediaControl().setVolume(progress)
        isViewOn = false
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {

    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
        MediaControl().setVolume(this.progress)
        isViewOn = true
        //        CommonArgs.autoFade(CommonArgs.rl_volume_seekbar, isViewOn);
    }

}
