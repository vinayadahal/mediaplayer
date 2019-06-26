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
    private var pw_resume: PopupWindow? = null
    private var pw_volume: PopupWindow? = null
    private var pw_brightness: PopupWindow? = null
    private val objPopUpOnTouchListener = PopUpOnTouchListener()
    private val playFile = CommonArgs.playFileCtx as PlayFile

    fun initCustomPopUp(popup: View, pw: PopupWindow, text: String) {
        val popup_resume = popup.findViewById<View>(R.id.popup_resume) as RelativeLayout
        popup_resume.setOnTouchListener(objPopUpOnTouchListener)
        val resumeText = popup.findViewById<View>(R.id.resume_text) as TextView
        resumeText.text = "Do you want to resume playback from " + MathService.timeFormatter(java.lang.Long.parseLong(text.toString().trim { it <= ' ' })) + "?"
        val resumeBtn = popup.findViewById<View>(R.id.popup_call_btn) as Button
        resumeBtn.transformationMethod = null
        resumeBtn.setOnClickListener {
            CommonArgs.mediaPlayer!!.seekTo(Integer.parseInt(text.trim { it <= ' ' }.toString()))
            pw.dismiss()
        }
        val cancelBtn = popup.findViewById<View>(R.id.popup_sms_btn) as Button
        cancelBtn.transformationMethod = null
        cancelBtn.setOnClickListener { pw.dismiss() }
    }

    fun showResumeDialog(text: String) {
        val popup = inflater.inflate(R.layout.pop_up_resume, null, true)
        pw_resume = PopupWindow(popup, llp.width, llp.height, true)
        objPopUpOnTouchListener.pw = pw_resume
        initCustomPopUp(popup, pw_resume!!, text) // initialize popup components
        popupShowAt(pw_resume!!)
    }

    fun showVolumeDialog() {
        val popup = inflater.inflate(R.layout.pop_up_volume, null, true)
        val volumeSeekBar = popup.findViewById<View>(R.id.volume_seekbar) as SeekBar
        volumeSeekBar.max = 15
        volumeSeekBar.progress = CommonArgs.audioManager!!.getStreamVolume(AudioManager.STREAM_MUSIC)//sets current volume to seekbar
        volumeSeekBar.setOnSeekBarChangeListener(VolumeOnSeekBarChangeListener())
        val popup_volume = popup.findViewById<View>(R.id.popup_volume) as RelativeLayout
        pw_volume = PopupWindow(popup, llp.width, llp.height, true)
        objPopUpOnTouchListener.pw = pw_volume
        popup_volume.setOnTouchListener(objPopUpOnTouchListener)
        popupShowAt(pw_volume!!)
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
        val popup_brightness = popup.findViewById<View>(R.id.popup_brightness) as RelativeLayout
        pw_brightness = PopupWindow(popup, llp.width, llp.height, true)
        objPopUpOnTouchListener.pw = pw_brightness
        popup_brightness.setOnTouchListener(objPopUpOnTouchListener)
        popupShowAt(pw_brightness!!)
    }

    fun popupShowAt(pw: PopupWindow) {
        pw.setBackgroundDrawable(ColorDrawable()) //helped me to hide popup
        pw.isOutsideTouchable = true
        pw.isTouchable = true
        pw.showAtLocation(CommonArgs.rl_play_file, Gravity.CENTER, 0, 0)
    }

}
