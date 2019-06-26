package com.mediaplayer.services

import com.mediaplayer.variables.CommonArgs
import java.io.File
import java.io.FileInputStream
import java.io.PrintWriter

class FileService {

    internal var text = StringBuilder()

    fun readFile(filename: String): String? {
        val file = File(CommonArgs.playFileCtx!!.applicationContext.filesDir.toString() + "/" + filename)
       return if (file.exists()) {
            FileInputStream(file).bufferedReader().use { it.readText() }
        }else{
            null
        }
    }

    fun writeFile(data: String, filename: String): Boolean {
        val dest = File(CommonArgs.playFileCtx!!.applicationContext.filesDir.toString() + "/" +filename)
        try {
            PrintWriter(dest).use { out -> out.println(data) }
            return true
        } catch (e: Exception) {
            println("Exception writing playback file::::::")
            println(e)
            return false
        }
    }

}
