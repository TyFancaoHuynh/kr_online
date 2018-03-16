package com.example.hoavot.karaokeonline.ui.extensions

import java.util.regex.Pattern

/**
 *  Copyright © 2017 AsianTech inc.
 *  Created by at-hoavo on 15/12/2017.
 */

/**
 * fun to convert time video to format time string
 */
internal fun String.toDurationTimeVideo(): String {
    val pattern = Pattern.compile("[A-Z]+")
    val list: Array<String> = pattern.split(this.trim())
    when (list.size) {
        4 -> return list[1] + ":" + if (list[2].length > 1) list[2] else {
            "0" + list[2]
        } + ":" + if (list[3].length > 1) list[3] else {
            "0" + list[3]
        }
        3 -> return list[1] + ":" + if (list[2].length > 1) list[2] else {
            "0" + list[2]
        }
        2 -> return "0:" + list[1]
        else -> return ""
    }
}

/**
 * fun to format display view count video
 */
internal fun String.toViewCountVideo(): String {
    var vC = this
    if (this.length.compareTo(3) == 1) {
        vC = ""
        var view = this.length.rem(3)
        when (view) {
            1 -> vC = this[0] + ""
            2 -> vC = this.substring(0, 1) + ""
            0 -> vC = this.substring(0, 2) + ""
        }
        if (view == 0) {
            view = 3
        }
        for (i in view..this.length - 3 step 3) {
            vC += "." + this.substring(i, i + 3)
        }
    }
    return vC.plus(" lượt xem")
}

/**
 * fun to format display day publish video
 */
fun String.getDayPublish(): String {
    return this.substring(0, 10)
}