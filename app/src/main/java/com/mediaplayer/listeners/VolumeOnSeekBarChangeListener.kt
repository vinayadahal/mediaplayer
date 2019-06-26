package com.mediaplayer.listeners

import android.widget.SeekBar

import com.mediaplayer.services.MediaControl
import com.mediaplayer.variables.CommonArgs

class VolumeOnSeekBarChangeListener : SeekBar.OnSeekBarChangeListener {

    internal var isViewOn: Boolean? = false
    internal var Progress: Int = 0

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        Progress = progress
        MediaControl().setVolume(Progress)
        isViewOn = false
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {

    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
        MediaControl().setVolume(Progress)
        isViewOn = true
        //        CommonArgs.autoFade(CommonArgs.rl_volume_seekbar, isViewOn);
    }

}
