package com.example.hoavot.karaokeonline.ui.feed.comment

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import org.jetbrains.anko.*

/**
 *
 * @author at-hoavo.
 */
class CommentUI : AnkoComponent<ViewGroup> {
    internal lateinit var avatar: ImageView
    internal lateinit var comment: TextView

    override fun createView(ui: AnkoContext<ViewGroup>): View = with(ui) {
        linearLayout {
            backgroundColor = Color.CYAN
            lparams(matchParent, dip(30)) {
                horizontalMargin = dip(20)
                topMargin = dip(10)
            }
            avatar = imageView {

            }.lparams(dip(25), dip(25))

            comment = textView {

            }.lparams(matchParent, wrapContent) {
                leftMargin = dip(10)
            }
        }
    }
}
