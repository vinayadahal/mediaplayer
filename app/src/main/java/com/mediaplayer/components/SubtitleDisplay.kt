package com.mediaplayer.components


import android.os.Build
import android.support.annotation.RequiresApi
import android.text.Html
import android.widget.TextView

import com.mediaplayer.services.SrtParser
import com.mediaplayer.variables.CommonArgs

import java.io.File

class SubtitleDisplay {

    fun showSub() {
        val srtFile = CommonArgs.currentVideoPath!!.substring(0, CommonArgs.currentVideoPath!!.lastIndexOf(".")) + ".srt"
        if (!File(srtFile).exists()) {
            CommonArgs.handler.post {
                if (CommonArgs.subArea!!.text !== "") {
                    CommonArgs.subArea!!.text = ""
                }
            }
            return
        }
        val objSrtParser = SrtParser()
        val text = objSrtParser.loadSrt(srtFile)
        objSrtParser.parse(text)
        CommonArgs.subtitle_runnable = object : Runnable {
            @RequiresApi(Build.VERSION_CODES.N)
            override fun run() {
                if (CommonArgs.isPlaying!!) {
                    val th = Thread.currentThread()
                    th.priority = Thread.MIN_PRIORITY
                    val time = objSrtParser.srtTimeFormatter(CommonArgs.mediaPlayer!!.currentPosition.toLong())
                    val line = objSrtParser.showMap(time)
                    CommonArgs.handler.post {
                        if (line != "null" && line !== "") {
                            CommonArgs.subArea!!.setText(Html.fromHtml(line,Html.FROM_HTML_MODE_LEGACY), TextView.BufferType.NORMAL)
                        } else if (line === "") {
                            CommonArgs.subArea!!.text = ""
                        }
                    }
                    CommonArgs.handler.postDelayed(this, 500)
                }
            }
        }
        CommonArgs.handler.post(CommonArgs.subtitle_runnable)
    }
}
