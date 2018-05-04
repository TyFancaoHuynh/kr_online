package com.example.hoavot.karaokeonline.ui.extensions

import android.graphics.Rect
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.EditText
import com.example.hoavot.karaokeonline.R
import com.example.hoavot.karaokeonline.ui.utils.AvoidRapidAction
import com.github.siyamed.shapeimageview.CircularImageView
import de.hdodenhof.circleimageview.CircleImageView
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


internal fun View.setOnVisibilityChangeListener(onVisibilityChangeListener: OnVisibilityChangeListener) {
    this.viewTreeObserver.addOnGlobalLayoutListener(ViewTreeObserver.OnGlobalLayoutListener {
        val rectContainer = Rect()
        // R will be populated with the coordinates of your view that area still visible.
        this.getWindowVisibleDisplayFrame(rectContainer)
        val heightDiff = this.rootView.height - (rectContainer.bottom - rectContainer.top)
        val res = this.resources
        // The status bar is 25dp, use 50dp for assurance
        var maxDiff = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50f, res.displayMetrics)
        // Lollipop includes button bar in the root. Add height of button bar (48dp) to maxDiff
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val heightButtonBar = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48f, res.displayMetrics)
            maxDiff += heightButtonBar
        }
        // If more than 100 pixels, its probably a keyboard.
        if (heightDiff > maxDiff) {
            onVisibilityChangeListener.onShowKeyboard(heightDiff)
        } else {
            onVisibilityChangeListener.onHideKeyboard()
        }
    })
}

inline fun ViewManager.circleImageView(init: CircleImageView.() -> Unit): CircleImageView
        = ankoView({ CircleImageView(it) }, 0, init)

/**
 *
 * Interface on visibility keyboard listener
 */
interface OnVisibilityChangeListener {
    /**
     * On show keyboard
     */
    fun onShowKeyboard(keyboardHeight: Int)

    /**
     * On hide keyboard
     */
    fun onHideKeyboard()
}


