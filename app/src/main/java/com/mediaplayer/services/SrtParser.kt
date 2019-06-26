package com.mediaplayer.services


import java.io.File
import java.io.FileInputStream
import java.util.*

class SrtParser {

    private var startDialog: MutableMap<String, String> = HashMap()
    private var endDialog: MutableMap<String, String> = HashMap()

    fun loadSrt(FileLocation: String): java.lang.StringBuilder {
        val file = File(FileLocation)
        val text = StringBuilder()
        if (file.exists()) {
            val br = FileInputStream(file).bufferedReader()
            val iterator = br.readLines().iterator()
            while (iterator.hasNext()) {
                val line = iterator.next()
                text.append(line.trim())
                text.append('\n')
            }
            br.close()
        }
        return text
    }

    fun parse(text: StringBuilder) {
        val subtitleArray = text.toString().trim().split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (i in subtitleArray.indices) {
            if (subtitleArray[i].contains("-->")) {
                val splitTime = subtitleArray[i].split("-->".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val startTime = splitTime[0].substring(0, splitTime[0].indexOf(",")).trim()
                val stopTime = splitTime[1].substring(0, splitTime[1].indexOf(",")).trim()
                var j = 1
                val sb = StringBuilder()
                while (!subtitleArray[i + j].contains("-->")) {
                    if (subtitleArray[i + j] == "") {
                        break
                    } else {
                        if (subtitleArray[i + j - 1].contains("\n") && !subtitleArray[i + j - 1].contains("-->")) {
                            sb.append(subtitleArray[i + j])
                        } else {
                            sb.append("<br/>")
                            sb.append(subtitleArray[i + j])
                        }
                    }
                    if (subtitleArray.size - 2 == i) {
                        break
                    } else {
                        j++
                    }
                    if (i + j >= subtitleArray.size) {
                        break
                    }
                }
                startDialog[startTime] = sb.toString().trim()
                endDialog[stopTime] = ""
            }
        }
    }

    fun srtTimeFormatter(duration: Long): String {
        val second = duration / 1000 % 60
        val minute = duration / (1000 * 60) % 60
        val hour = duration / (1000 * 60 * 60) % 24
        return String.format("%02d:%02d:%02d", hour, minute, second)
    }

    fun showMap(videoTime: String): String {
        return if (startDialog[videoTime] != null) {
            startDialog[videoTime].toString()
        } else {
            endDialog[videoTime].toString()
        }
    }
}
