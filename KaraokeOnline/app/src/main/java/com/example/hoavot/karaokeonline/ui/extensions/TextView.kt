package com.example.hoavot.karaokeonline.ui.extensions

import android.support.v4.content.res.ResourcesCompat
import android.widget.TextView
import com.example.hoavot.karaokeonline.R

/**
 *
 * @author at-hoavo.
 */
internal fun TextView.fontNomal() {
    typeface = ResourcesCompat.getFont(context, R.font.nomal)
}