package com.mediaplayer.variables


import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Handler
import android.view.View
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.TextView
import android.widget.VideoView

import com.mediaplayer.components.Effects
import com.mediaplayer.listeners.PlayFileTouchListener
import com.mediaplayer.services.MediaControl
import com.mediaplayer.services.MobileArrayAdapter

object CommonArgs {

    var rl_play_file: RelativeLayout? = null
    var title_control: RelativeLayout? = null
    var notification_txt: TextView? = null
    var seekBar: SeekBar? = null
    var videoView: VideoView? = null
    var duration: Long = 0
    var currentTimeTxt: TextView? = null
    var subArea: TextView? = null
    var mediaPlayer: MediaPlayer? = null
    var audioManager: AudioManager? = null
    var playFileCtx: Context? = null
    var title_control_runnable: Runnable? = null
    var runnable: Runnable? = null
    var subtitle_runnable: Runnable? = null
    var handler = Handler()
    var title_control_handler = Handler()
    var currentVideoPath: String? = null
    var isPlaying: Boolean? = null
    var allVideoPath: List<String>? = null
    var setBackgroundOnResume: Boolean? = false

}
