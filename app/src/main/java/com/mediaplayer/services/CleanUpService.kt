package com.mediaplayer.services


import android.content.Context

import com.mediaplayer.variables.Config

import java.io.File
import java.util.ArrayList

object CleanUpService {

    fun deleteTempPlayBack(videoPath: List<String>, ctx: Context) {
        val filesToDelete = ArrayList<String>()
        val internalFile = ctx.applicationContext.filesDir.listFiles()
        for (i in internalFile.indices) {
            val internalFileName = internalFile[i].name
            var fileFound: Boolean? = false
            for (j in videoPath.indices) {
                val videoFile = File(videoPath[j]).name + ".txt"
                if (internalFileName == videoFile) {
                    fileFound = true
                    break
                }
            }
            if ((!fileFound!!)) {
                filesToDelete.add(internalFileName)
            }
        }
        for (k in filesToDelete.indices) {
            val file = File(ctx.applicationContext.filesDir, filesToDelete[k])
            val deleted = file.delete()
            if (!deleted) {
                println("Couldn't delete:::: " + filesToDelete[k])
            } else {
                println("deleted File:::: " + filesToDelete[k])
            }
        }
    }

    fun deleteThumbnail(videoPath: List<String>) {
        val filesToDelete = ArrayList<String>()
        val imageFile = File(Config.baseThumbPath + "/.dthumb").listFiles()
        if (imageFile == null) {
            println("Found no list of items...")
            return
        }
        for (i in imageFile.indices) {
            val thumbFileName = imageFile[i].name
            var fileFound: Boolean? = false
            for (j in videoPath.indices) {
                val videoFile = File(videoPath[j]).name + ".bmp"
                if (thumbFileName == videoFile) {
                    fileFound = true
                    break
                }
            }
            if ((!fileFound!!)) {
                filesToDelete.add(thumbFileName)
            }
        }
        for (k in filesToDelete.indices) {
            val file = File(Config.baseThumbPath + "/.dthumb", filesToDelete[k])
            val deleted = file.delete()
            if (!deleted) {
                println("Couldn't delete:::: " + filesToDelete[k])
            } else {
                println("deleted File:::: " + filesToDelete[k])
            }
        }
    }

}
