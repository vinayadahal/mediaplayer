package com.mediaplayer.components


import android.os.Handler
import android.provider.MediaStore

import com.mediaplayer.services.FileService
import com.mediaplayer.variables.CommonArgs

import java.io.File

class PlayBackResume {

    fun getVideoDuration(filename: String?): String? {
        val columns = arrayOf(MediaStore.Video.VideoColumns.DURATION)
        val whereVal = arrayOf(filename)
        val cursor = CommonArgs.playFileCtx!!.contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, columns, MediaStore.MediaColumns.DATA + "=?" + "", whereVal, null)
        var strDuration: String? = null
        if (cursor!!.moveToFirst()) {
            strDuration = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DURATION))
        }
        cursor.close()
        return strDuration
    }

    fun resumeFrom(filePath: String?) {
        val objFileService = FileService()
        val filename = File(filePath).name + ".txt"
        val text = objFileService.readFile(filename)
        if (text == null) {
            Handler().postDelayed({
                objFileService.writeFile("0", filename) // content will be like time(in msec)
            }, 2000)
            return
        }
        if (text.toString() != "0") {
            Handler().postDelayed({
                if (getVideoDuration(filePath) != text.trim { it <= ' ' } && getVideoDuration(filePath) != null)
                    PopUpDialog().showResumeDialog(text)
            }, 500)
        }
    }

    fun setResumePoint(filePath: String?, timeInMs: Int) {
        Handler().post {
            val objFileService = FileService()
            val filename = File(filePath).name + ".txt"
            objFileService.writeFile(Integer.toString(timeInMs), filename) // content will be like time(in msec)
        }
    }

    fun resumePlayBackAuto(filePath: String?) {
        val objFileService = FileService()
        val filename = File(filePath).name + ".txt"
        val text = objFileService.readFile(filename) ?: return
        if (CommonArgs.mediaPlayer != null)
            CommonArgs.mediaPlayer!!.seekTo(Integer.parseInt(text.toString().trim { it <= ' ' }))
    }

}
