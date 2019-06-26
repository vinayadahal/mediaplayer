package com.mediaplayer.services

class MathService {

//    private var sumx: Float = 0.toFloat()
//    private var sumy: Float = 0.toFloat()

//    fun getSwipeArea(startx: Float, starty: Float, endx: Float, endy: Float): String? {
//        println("start x: $startx end x: $endx")
//        println("start y: $starty end y: $endy")
//
//        var actionToDo: String? = null
//        when {
//            startx < endx -> sumx = startx - endx
//            startx > endx -> sumx = startx - endx
//            starty < endy -> {
//                println("endy is big-----")
//                sumy = starty - endy
//            }
//            starty > endy -> {
//                println("starty is big---")
//                sumy = starty - endy
//            }
//        }
//        if (sumy >= 100 && sumx < sumy) {
//            println("You may have swiped bottom to top")
//            println("distance " + sumy.toInt() / 48)
//            actionToDo = "volumeUP"
//        }
//        if (sumx >= 100 && sumy < sumx) {
//            println("You may have swiped right to left")
//        }
//        if (sumx <= -100 && sumy > sumx) {
//            println("You may have swiped left to right")
//        }
//        if (sumy <= -100 && sumx > sumy) {
//            println("You may have swiped top to bottom")
//            actionToDo = "volumeDown"
//        }
//        println("sumx $sumx")
//        println("sumy $sumy")
//        return actionToDo
//    }

    companion object {

        fun convertFileSize(bytes: Long): String {
            val kilobytes = Math.round((bytes / 1024 * 100).toFloat()).toDouble() / 100
            val megabytes = Math.round(kilobytes / 1024 * 100).toDouble() / 100
            val gigabytes = Math.round(megabytes / 1024 * 100).toDouble() / 100
            if (kilobytes < 1024) {
                return "$kilobytes KB"
            }
            if (megabytes < 1024) {
                return "$megabytes MB"
            }
            return if (gigabytes < 1024) {
                "$gigabytes GB"
            } else "$bytes Bytes"
        }

        fun timeFormatter(duration: Long): String {
            val second = duration / 1000 % 60
            val minute = duration / (1000 * 60) % 60
            val hour = duration / (1000 * 60 * 60) % 24
            return if (hour == 0L) {
                String.format("%02d:%02d", minute, second)
            } else String.format("%02d:%02d:%02d", hour, minute, second)
        }
    }
}
