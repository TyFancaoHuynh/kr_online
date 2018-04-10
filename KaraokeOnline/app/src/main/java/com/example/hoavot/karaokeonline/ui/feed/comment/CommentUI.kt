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
            lparams(matchParent, dip(150)) {
                horizontalMargin = dip(20)
            }
            avatar = imageView {

            }.lparams(dip(20), dip(20))

            comment = textView {

            }
        }
    }
}
