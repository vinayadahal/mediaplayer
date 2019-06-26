package com.mediaplayer.variables


import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Handler
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.TextView
import android.widget.VideoView

object CommonArgs {

    var rlPlayFile: RelativeLayout? = null
    var titleControl: RelativeLayout? = null
    var notification_txt: TextView? = null
    var seekBar: SeekBar? = null
    var videoView: VideoView? = null
    var duration: Long = 0
    var currentTimeTxt: TextView? = null
    var subArea: TextView? = null
    var mediaPlayer: MediaPlayer? = null
    var audioManager: AudioManager? = null
    var playFileCtx: Context? = null
    var titleControlRunnable: Runnable? = null
    var runnable: Runnable? = null
    var subtitleRunnable: Runnable? = null
    var handler = Handler()
    var titleControlHandler = Handler()
    var currentVideoPath: String? = null
    var isPlaying: Boolean? = null
    var allVideoPath: List<String>? = null
    var setBackgroundOnResume: Boolean? = false

}
