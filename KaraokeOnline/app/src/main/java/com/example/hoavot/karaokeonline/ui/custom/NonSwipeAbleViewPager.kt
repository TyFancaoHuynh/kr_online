package com.example.hoavot.karaokeonline.ui.custom

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewManager
import org.jetbrains.anko.custom.ankoView

/**
 *
 * @author at-hoavo.
 */
class NonSwipeAbleViewPager : ViewPager {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onInterceptTouchEvent(arg0: MotionEvent) = false

    override fun canScrollHorizontally(direction: Int) = false

    override fun onTouchEvent(ev: MotionEvent?) = false
}

/**
 * NonSwipeAbleViewPager
 */
inline fun ViewManager.nonSwipeAbleViewPager(init: NonSwipeAbleViewPager.() -> Unit): NonSwipeAbleViewPager
        = ankoView({ NonSwipeAbleViewPager(it) }, 0, init)