package com.mediaplayer.components


import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.media.AudioManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.TextView

import com.mediaplayer.R
import com.mediaplayer.activites.PlayFile
import com.mediaplayer.listeners.BrightnessOnSeekBarChangeListener
import com.mediaplayer.listeners.PopUpOnTouchListener
import com.mediaplayer.listeners.VolumeOnSeekBarChangeListener
import com.mediaplayer.services.MathService
import com.mediaplayer.variables.CommonArgs

class PopUpDialog {

    private val llp = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    private val inflater = CommonArgs.playFileCtx!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private var pwResume: PopupWindow? = null
    private var pwVolume: PopupWindow? = null
    private var pwBrightness: PopupWindow? = null
    private val objPopUpOnTouchListener = PopUpOnTouchListener()
    private val playFile = CommonArgs.playFileCtx as PlayFile

    private fun initCustomPopUp(popup: View, pw: PopupWindow, text: String) {
        val popupResume = popup.findViewById<View>(R.id.popup_resume) as RelativeLayout
        popupResume.setOnTouchListener(objPopUpOnTouchListener)
        val resumeText = popup.findViewById<View>(R.id.resume_text) as TextView
        resumeText.text = "Do you want to resume playback from " + MathService.timeFormatter(java.lang.Long.parseLong(text.toString().trim { it <= ' ' })) + "?"
        val resumeBtn = popup.findViewById<View>(R.id.popup_call_btn) as Button
        resumeBtn.transformationMethod = null
        resumeBtn.setOnClickListener {
            CommonArgs.mediaPlayer!!.seekTo(Integer.parseInt(text.trim()))
            pw.dismiss()
        }
        val cancelBtn = popup.findViewById<View>(R.id.popup_sms_btn) as Button
        cancelBtn.transformationMethod = null
        cancelBtn.setOnClickListener { pw.dismiss() }
    }

    fun showResumeDialog(text: String) {
        val popup = inflater.inflate(R.layout.pop_up_resume, null, true)
        pwResume = PopupWindow(popup, llp.width, llp.height, true)
        objPopUpOnTouchListener.pw = pwResume
        initCustomPopUp(popup, pwResume!!, text) // initialize popup components
        popupShowAt(pwResume!!)
    }

    fun showVolumeDialog() {
        val popup = inflater.inflate(R.layout.pop_up_volume, null, true)
        val volumeSeekBar = popup.findViewById<View>(R.id.volume_seekbar) as SeekBar
        volumeSeekBar.max = 15
        volumeSeekBar.progress = CommonArgs.audioManager!!.getStreamVolume(AudioManager.STREAM_MUSIC)//sets current volume to seekbar
        volumeSeekBar.setOnSeekBarChangeListener(VolumeOnSeekBarChangeListener())
        val popupVolume = popup.findViewById<View>(R.id.popup_volume) as RelativeLayout
        pwVolume = PopupWindow(popup, llp.width, llp.height, true)
        objPopUpOnTouchListener.pw = pwVolume
        popupVolume.setOnTouchListener(objPopUpOnTouchListener)
        popupShowAt(pwVolume!!)
    }

    fun showBrightnessDialog() {
        val popup = inflater.inflate(R.layout.pop_up_brightness, null, true)
        val brightnessSeekBar = popup.findViewById<View>(R.id.brightness_seekbar) as SeekBar
        brightnessSeekBar.max = 10
        val lp = playFile.window.attributes // brightness issue.
        if (lp.screenBrightness > 0) {
            brightnessSeekBar.progress = (lp.screenBrightness * 10).toInt()
        }
        brightnessSeekBar.setOnSeekBarChangeListener(BrightnessOnSeekBarChangeListener())
        val popupBrightness = popup.findViewById<View>(R.id.popup_brightness) as RelativeLayout
        pwBrightness = PopupWindow(popup, llp.width, llp.height, true)
        objPopUpOnTouchListener.pw = pwBrightness
        popupBrightness.setOnTouchListener(objPopUpOnTouchListener)
        popupShowAt(pwBrightness!!)
    }

    private fun popupShowAt(pw: PopupWindow) {
        pw.setBackgroundDrawable(ColorDrawable()) //helped me to hide popup
        pw.isOutsideTouchable = true
        pw.isTouchable = true
        pw.showAtLocation(CommonArgs.rlPlayFile, Gravity.CENTER, 0, 0)
    }

}
