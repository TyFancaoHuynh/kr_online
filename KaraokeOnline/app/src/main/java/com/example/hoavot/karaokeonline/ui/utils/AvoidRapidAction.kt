package com.example.hoavot.karaokeonline.ui.utils


import android.os.SystemClock

/**
 *
 * @author at-hoavo.
 */
object AvoidRapidAction {
    private var lastClickTime: Long = 0
    private const val DEFAULT_DELAY_TIME = 300 //ms
    internal const val DELAY_TIME = 500 //ms
    internal fun action(action: () -> Unit) {
        action(DEFAULT_DELAY_TIME, action)
    }

    internal fun action(milis: Int, action: () -> Unit) {
        if (Math.abs(SystemClock.elapsedRealtime() - lastClickTime) > milis) {
            action.invoke()
            lastClickTime = SystemClock.elapsedRealtime()
        }
    }
}
