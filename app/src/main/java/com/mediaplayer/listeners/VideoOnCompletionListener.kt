package com.mediaplayer.listeners

import android.app.Activity
import android.content.Context
import android.media.MediaPlayer

import com.mediaplayer.components.PlayBackResume
import com.mediaplayer.services.FileService
import com.mediaplayer.variables.CommonArgs

import java.io.File


class VideoOnCompletionListener : MediaPlayer.OnCompletionListener {

    var ctx: Context? = null

    override fun onCompletion(mp: MediaPlayer) {
        (ctx as Activity).finish()
        CommonArgs.isPlaying = false
    }

}
