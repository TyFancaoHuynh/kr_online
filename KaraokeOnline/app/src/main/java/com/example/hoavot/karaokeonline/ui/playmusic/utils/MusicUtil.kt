package com.example.hoavot.karaokeonline.ui.playmusic.utils

/**
 * Copyright © 2017 AsianTech inc.
 * Created by at-hoavo on 01/07/2017.
 */
object MusicUtil {
    fun showTime(milliseconds: Long): String {
        var timer = ""
        val hours = (milliseconds / (1000 * 60 * 60)).toInt()
        val minutes = (milliseconds % (1000 * 60 * 60)).toInt() / (1000 * 60)
        val seconds = (milliseconds % (1000 * 60 * 60) % (1000 * 60) / 1000).toInt()
        if (hours > 0) {
            timer += hours.toString() + ":"
        } else
            timer += "0:"
        if (minutes > 0) {
            if (minutes <= 9) timer += "0"
            timer += minutes.toString() + ":"
        } else
            timer += "00:"
        if (seconds > 0) {
            if (seconds <= 9) timer += "0"
            timer += seconds.toString()
        } else
            timer += "00"

        return timer
    }
}

fun Long.showDate(): String {
    var date = "02-05-1995"

    return date
}