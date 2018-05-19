package com.example.hoavot.karaokeonline.ui.feed.comment

import android.graphics.Color
import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.hoavot.karaokeonline.R
import com.example.hoavot.karaokeonline.ui.extensions.circleImageView
import com.example.hoavot.karaokeonline.ui.extensions.fontNomal
import de.hdodenhof.circleimageview.CircleImageView
import org.jetbrains.anko.*

/**
 *
 * @author at-hoavo.
 */
class CommentUI : AnkoComponent<ViewGroup> {
    internal lateinit var avatar: CircleImageView
    internal lateinit var username: TextView
    internal lateinit var comment: TextView

    override fun createView(ui: AnkoContext<ViewGroup>): View = with(ui) {
        relativeLayout {
            lparams(matchParent, wrapContent) {
                horizontalMargin = dip(20)
                verticalMargin = dip(10)
            }
            avatar = circleImageView {
                id = R.id.commentFragmentAvatar
                borderWidth = dip(0.4f)
                borderColor = Color.GRAY
            }.lparams(dip(35), dip(35))

            relativeLayout {
                backgroundResource = R.drawable.ic_comment_area
                lparams(matchParent, wrapContent) {
                    rightOf(R.id.commentFragmentAvatar)
                    leftMargin = dip(5)
                }
                username = textView {
                    id = R.id.commentFragmentUsername
                    textColor = Color.BLACK
                    textSize = px2dip(dimen(R.dimen.textSize15))
                    typeface = Typeface.DEFAULT_BOLD

                }.lparams {
                    leftMargin = dip(5)
                }

                comment = textView {
                    textColor = Color.BLACK
                    textSize = px2dip(dimen(R.dimen.textSize14))
                    fontNomal()
                }.lparams {
                    below(R.id.commentFragmentUsername)
                    sameLeft(R.id.commentFragmentUsername)
                    topMargin = dip(5)
                }
            }
        }
    }
}
