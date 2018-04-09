package com.example.hoavot.karaokeonline.ui.extensions

import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.example.hoavot.karaokeonline.R
import com.github.siyamed.shapeimageview.CircularImageView
import net.cachapa.expandablelayout.ExpandableLayout
import org.jetbrains.anko.custom.ankoView

/**
 *
 * @author at-hoavo.
 */
internal fun ViewGroup.roundImage(init: CircularImageView.() -> Unit): CircularImageView {
    return ankoView({ CircularImageView(it) }, init = init, theme = 0)
}

internal fun ViewGroup.expandleLayout(init: ExpandableLayout.() -> Unit): ExpandableLayout {
    return ankoView({ ExpandableLayout(it) }, init = init, theme = 0)
}

/**
 * Enable High Light When Clicked
 */
internal fun View.enableHighLightWhenClicked() {
//    val originAlphaAnimation = AnimationUtils.loadAnimation(context, R.anim.high_light_fade_in)
    val lessAlphaAnimation = AnimationUtils.loadAnimation(context, R.anim.high_light_fade_out)

    setOnTouchListener { v, event ->
        if (event.action == MotionEvent.ACTION_DOWN) {
            v.startAnimation(lessAlphaAnimation)
        }
        false
    }
}