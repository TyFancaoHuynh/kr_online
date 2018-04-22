package com.example.hoavot.karaokeonline.ui.extensions

import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.EditText
import com.example.hoavot.karaokeonline.R
import com.example.hoavot.karaokeonline.ui.utils.AvoidRapidAction
import com.github.siyamed.shapeimageview.CircularImageView
import net.cachapa.expandablelayout.ExpandableLayout
import org.jetbrains.anko.custom.ankoView
import org.jetbrains.anko.sdk25.coroutines.onClick

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

internal fun View.onClickWithAvoidRapidAction(delayTime: Int, click: () -> Unit) {
    onClick {
        AvoidRapidAction.action(delayTime, click)
    }
}

fun EditText.onTextChangeListener(beforeTextChanged: (CharSequence?) -> Unit = {},
                                  onTextChanged: (CharSequence?) -> Unit = {},
                                  afterTextChanged: (Editable?) -> Unit = {}) {
    addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            beforeTextChanged.invoke(p0)
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            onTextChanged.invoke(p0)
        }

        override fun afterTextChanged(p0: Editable?) {
            afterTextChanged.invoke(p0)
        }
    })

}
