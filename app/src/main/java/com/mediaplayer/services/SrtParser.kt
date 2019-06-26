package com.mediaplayer.services


import java.io.*
import java.sql.Time
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class SrtParser {

    var startDialog: MutableMap<String, String> = HashMap()
    var endDialog: MutableMap<String, String> = HashMap()

    fun loadSrt(FileLocation: String): java.lang.StringBuilder {
        val file = File(FileLocation)
        val text = StringBuilder()
//        val br: BufferedReader
//        try {
//            br = BufferedReader(FileReader(file))
//            var line = br.readLine()
//            while (line != null) {
//                text.append(line)
//                text.append('\n')
//            }
//            br.close()
//        } catch (ex: FileNotFoundException) {
//            println("Exception::::: $ex")
//        } catch (ex: IOException) {
//            println("Exception::::: $ex")
//        }
        if (file.exists()) {
//            FileInputStream(file).bufferedReader().use { it.readText() }
            var br = FileInputStream(file).bufferedReader()

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
        println("Source Text:::::: " + text.toString())
        println("Subtitle Array:::::" + Arrays.toString(subtitleArray))
        for (i in subtitleArray.indices) {
            if (subtitleArray[i].contains("-->")) {
                val splittedTime = subtitleArray[i].split("-->".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val startTime = splittedTime[0].substring(0, splittedTime[0].indexOf(",")).trim()
                val stopTime = splittedTime[1].substring(0, splittedTime[1].indexOf(",")).trim()
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
