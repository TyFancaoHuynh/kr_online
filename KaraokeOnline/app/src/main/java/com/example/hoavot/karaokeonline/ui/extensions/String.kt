package com.example.hoavot.karaokeonline.ui.extensions

import android.graphics.Color
import android.support.annotation.ColorInt
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View

/**
 *
 * @author at-hoavo.
 */
internal fun String.underlineText(onClick: (String) -> Unit): SpannableString {
    val spannableString = SpannableString(this)
    spannableString.setSpan(UnderlineStringSpan(this, Color.BLACK, onClick), 0, this.length, 0)
    return spannableString
}

internal class UnderlineStringSpan(private val value: String, @ColorInt private val textColor: Int, private val onClicked: (String) -> Unit) : ClickableSpan() {
    override fun onClick(widget: View?) {
        onClicked(value)
    }

    override fun updateDrawState(ds: TextPaint?) {
        super.updateDrawState(ds)
        ds?.isUnderlineText = true
        ds?.color = textColor
    }
}

internal fun Int.toTimer(seconds: Int): String {
    var minutes = ""
    var second = ""
    when (this) {
        0 -> minutes = "00:"
        in 1..9 -> minutes = "0" + this + ":"
        else -> minutes += this
    }
    when (seconds) {
        0 -> second = "00"
        in 1..9 -> second = "0" + seconds
        else -> second += seconds
    }
    return minutes + second
}