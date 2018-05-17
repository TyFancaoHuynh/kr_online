package com.example.hoavot.karaokeonline.ui.base

import java.text.SimpleDateFormat
import java.util.*

/**
 *
 * @author at-hoavo
 */
object Time {

    fun parseDay(timeAtMiliseconds: Long): String {
        if (timeAtMiliseconds <= 0) {
            return ""
        }
        val result = "Vừa xong."
        val formatter = SimpleDateFormat(" yyyy-MM-dd HH:mm:ss")
        val dataSot = formatter.format(Date())
        val calendar = Calendar.getInstance()
        val dayagolong = timeAtMiliseconds
        calendar.timeInMillis = dayagolong
        val agoformater = formatter.format(calendar.time)
        var CreateDate: Date? = null
        val CurrentDate = formatter.parse(dataSot)
        CreateDate = formatter.parse(agoformater);
        var different = Math.abs(CurrentDate.time - CreateDate.time)
        val secondsInMilli = 1000
        val minutesInMilli = secondsInMilli * 60
        val hoursInMilli = minutesInMilli * 60
        val daysInMilli = hoursInMilli * 24
        val elapsedDays = different / daysInMilli
        different %= daysInMilli
        val elapsedHours = different / hoursInMilli
        different %= hoursInMilli
        val elapsedMinutes = different / minutesInMilli
        different %= minutesInMilli
        val elapsedSeconds = different / secondsInMilli
        different %= secondsInMilli
        if (elapsedDays == 0L) {
            if (elapsedHours == 0L) {
                if (elapsedMinutes == 0L) {
                    if (elapsedSeconds < 0) {
                        return "0" + "s"
                    } else {
                        if (elapsedDays > 0 && elapsedSeconds < 59) {
                            return "Vừa xong."
                        }
                    }
                } else {
                    return elapsedMinutes.toString() + " phút trước."
                }
            } else {
                return elapsedHours.toString() + " giờ trước"
            }
        } else {
            if (elapsedDays <= 29) {
                return elapsedDays.toString() + " ngày trước"
            }
            if (elapsedDays in 30..58) {
                return "1 tháng trước"
            }
            if (elapsedDays in 59..87) {
                return "2 tháng trước"
            }
            if (elapsedDays in 88..116) {
                return "3 tháng trước"
            }
            if (elapsedDays in 117..145) {
                return "4 tháng trước"
            }
            if (elapsedDays in 146..174) {
                return "5 tháng trước"
            }
            if (elapsedDays in 175..203) {
                return "6 tháng trước"
            }
            if (elapsedDays in 204..232) {
                return "7 tháng trước"
            }
            if (elapsedDays in 233..261) {
                return "8 tháng trước"
            }
            if (elapsedDays in 262..290) {
                return "9 tháng trước"
            }
            if (elapsedDays in 291..319) {
                return "10 tháng trước"
            }
            if (elapsedDays in 320..348) {
                return "11 tháng trước"
            }
            if (elapsedDays in 349..360) {
                return "12 tháng trước"
            }
            if (elapsedDays in 361..720) {
                return "1 năm trước"
            }
            if (elapsedDays > 720) {
                val formatterYear = SimpleDateFormat("dd/MM/yyyy")
                val calendarYear = Calendar.getInstance()
                calendarYear.timeInMillis = dayagolong;
                return formatterYear.format(calendarYear.time) + ""
            }
        }
        return result
    }
}
