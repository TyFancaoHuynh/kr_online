package com.example.hoavot.karaokeonline.ui.feed.comment

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import com.example.hoavot.karaokeonline.R
import com.example.hoavot.karaokeonline.data.model.other.Comment
import com.example.hoavot.karaokeonline.ui.feed.FeedFragment
import com.example.hoavot.karaokeonline.ui.utils.AvoidRapidAction
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 *
 * @author at-hoavo.
 */
class CommentLayoutUI(private var comments: MutableList<Comment>) : AnkoComponent<CommentFragment> {
    internal var commentsAdapter = CommentAdapter(comments)
    internal var position = -1
    internal lateinit var areaComment: LinearLayout
    internal lateinit var edtComment: EditText
    internal lateinit var btnComment: ImageView

    override fun createView(ui: AnkoContext<CommentFragment>): View = with(ui) {
        relativeLayout {
            lparams(matchParent, dip(300))
            recyclerView {
                layoutManager = LinearLayoutManager(context)
                adapter = commentsAdapter
            }
            areaComment = linearLayout {
                edtComment = editText {

                }.lparams(dip(0), matchParent) {
                    weight = 6f
                }

                btnComment = imageView(R.drawable.ic_plus) {
                    onClick {
                        AvoidRapidAction.action(400) {
                            owner.eventWhenAddCommentClicked(edtComment.text.toString())
                        }
                    }

                }.lparams(dip(0), matchParent) {
                    weight = 1f
                }
            }.lparams(matchParent, dip(50)) {
                alignParentBottom()
            }
        }
    }
}
