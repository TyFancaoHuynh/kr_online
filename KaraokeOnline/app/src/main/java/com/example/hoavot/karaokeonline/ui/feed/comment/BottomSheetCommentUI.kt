package com.example.hoavot.karaokeonline.ui.feed.comment

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import com.example.hoavot.karaokeonline.R
import com.example.hoavot.karaokeonline.data.model.other.Comment
import com.example.hoavot.karaokeonline.ui.feed.FeedFragment
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView

/**
 *
 * @author at-hoavo.
 */
class BottomSheetCommentUI : AnkoComponent<FeedFragment> {
    internal var comments = mutableListOf<Comment>()
    internal var commentsAdapter = CommentAdapter(comments)
    internal lateinit var areaComment: LinearLayout
    internal lateinit var edtComment: EditText
    internal lateinit var btnComment: ImageView

    override fun createView(ui: AnkoContext<FeedFragment>): View = with(ui) {
        relativeLayout {
            recyclerView {
                layoutManager = LinearLayoutManager(context)
                adapter = commentsAdapter
            }
            areaComment = linearLayout {
                edtComment = editText {

                }

                btnComment = imageView(R.drawable.ic_plus) { }
            }.lparams(matchParent, wrapContent) {
                alignParentBottom()
            }
        }
    }
}
