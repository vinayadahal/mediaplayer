package com.mediaplayer.services


import android.content.Context
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.provider.MediaStore

import com.mediaplayer.components.MessageAlert
import com.mediaplayer.variables.Config

import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class IconService {

    var ctx: Context? = null

    fun checkThumb(videoFile: String): String? {
        val fileFolder = File(videoFile)
        val thumbDir = Config.baseThumbPath + "/.dthumb"
        val imageFile = fileFolder.name + ".bmp"
        val folder = File(thumbDir)
        if (!folder.exists()) {
            if (!folder.mkdir()) {
                MessageAlert().showToast("Failed to create thumbnail directory!", ctx!!)
            }
            return null
        }
        if (!File("$thumbDir/$imageFile").exists()) {
            println("Creating thumbnail ----->")
            val th = object : Thread() {
                override fun run() {
                    makeThumb(videoFile, "$thumbDir/$imageFile")
                }
            }
            th.start()
        }
        return "$thumbDir/$imageFile"
    }

    fun makeThumb(videoFile: String, thumbnailFile: String): String? {
        println("videoFile::::: $videoFile")
        val bmpThumb = ThumbnailUtils.createVideoThumbnail(videoFile, MediaStore.Images.Thumbnails.MICRO_KIND)
                ?: return null
        if (!File(thumbnailFile).exists()) {
            val thumbLocation = File(thumbnailFile)
            val thumbFile = File(thumbLocation.parent, thumbLocation.name)
            writeImgFile(thumbFile, bmpThumb)
        }
        return thumbnailFile
    }

    private fun writeImgFile(imgFile: File, bmp_thumb: Bitmap) {

        try {
            imgFile.createNewFile()
            val bos = ByteArrayOutputStream()
            bmp_thumb.compress(Bitmap.CompressFormat.PNG, 100, bos)
            val bmpByte = bos.toByteArray()
            val fos = FileOutputStream(imgFile)
            fos.write(bmpByte)
            fos.flush()
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }


}
