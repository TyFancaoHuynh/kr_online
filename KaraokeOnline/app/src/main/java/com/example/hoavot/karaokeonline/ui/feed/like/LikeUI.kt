package com.example.hoavot.karaokeonline.ui.feed.like

import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.hoavot.karaokeonline.R
import com.example.hoavot.karaokeonline.ui.extensions.circleImageView
import de.hdodenhof.circleimageview.CircleImageView
import org.jetbrains.anko.*

/**
 *
 * @author at-hoavo
 */
class LikeUI : AnkoComponent<ViewGroup> {
    internal lateinit var avatar: CircleImageView
    internal lateinit var username: TextView

    override fun createView(ui: AnkoContext<ViewGroup>): View {
        return ui.apply {
            linearLayout {
                lparams(matchParent, dip(50)) {
                    topMargin = dip(5)
                    leftMargin = dip(5)
                }

                avatar = circleImageView {
                    borderWidth = dip(0.4f)
                    borderColor = Color.GRAY
                }.lparams(dip(35), dip(35)) {
                    gravity = Gravity.CENTER_VERTICAL
                    leftMargin = dip(10)
                }

                username = textView {
                    textColor = Color.BLACK
                    textSize = px2dip(dimen(R.dimen.textSize18))
                    typeface = Typeface.DEFAULT_BOLD
                    gravity = Gravity.CENTER
                }.lparams {
                    leftMargin = dip(7)
                    topMargin = dip(15)
                }

            }
        }.view
    }
}