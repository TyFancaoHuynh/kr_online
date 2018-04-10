package com.example.hoavot.karaokeonline.ui.feed.comment

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import com.example.hoavot.karaokeonline.data.model.other.Comment
import com.example.hoavot.karaokeonline.ui.feed.FeedFragment
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView

/**
 *
 * @author at-hoavo.
 */
class BottomSheetCommentUI(comments: MutableList<Comment>) : AnkoComponent<FeedFragment> {
    internal var commentsAdapter = CommentAdapter(comments)
    internal lateinit var areaComment: LinearLayout
    internal lateinit var edtComment: EditText
    internal lateinit var btnComment: ImageView

    override fun createView(ui: AnkoContext<FeedFragment>): View = with(ui) {
        verticalLayout {

            recyclerView {
                layoutManager = LinearLayoutManager(context)
                adapter = commentsAdapter
            }

            areaComment = linearLayout {
                visibility = View.GONE
                edtComment = editText {

                }

                btnComment = imageView { }
            }
        }
    }
}