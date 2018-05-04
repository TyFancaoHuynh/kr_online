package com.example.hoavot.karaokeonline.ui.feed.comment

import android.graphics.Color
import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.hoavot.karaokeonline.R
import com.example.hoavot.karaokeonline.data.model.other.Comment
import com.example.hoavot.karaokeonline.ui.extensions.enableHighLightWhenClicked
import com.example.hoavot.karaokeonline.ui.extensions.hideKeyboard
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
    internal lateinit var areaComment: RelativeLayout
    internal lateinit var edtComment: EditText
    internal lateinit var recyclerComment: RecyclerView
    internal lateinit var countLike: TextView
    internal lateinit var countComment: TextView
    internal lateinit var like: ImageView
    internal lateinit var unlike: ImageView
    internal lateinit var cmtFirst: ImageView

    override fun createView(ui: AnkoContext<CommentFragment>): View = ui.apply {
        areaComment = relativeLayout {
            isClickable = false
            backgroundColor = Color.WHITE
            lparams(matchParent, matchParent)
            relativeLayout {
                id = R.id.commentFragmentToolBarLike
                lparams(matchParent, dip(40)) {
                    horizontalPadding = dip(20)
                }

                relativeLayout {
                    id = R.id.commentFragmentAreaLike
                    like = imageView(R.drawable.ic_count_like) {
                        enableHighLightWhenClicked()
                        onClick {
                            owner.likeClicked()
                        }
                    }.lparams(dip(20), dip(20)) {
                        centerHorizontally()
                    }
                    unlike = imageView(R.drawable.ic_unlike_feed) {
                        enableHighLightWhenClicked()
                        onClick {
                            owner.unLikeClicked()
                        }
                    }.lparams(dip(20), dip(20)) {
                        centerHorizontally()
                    }
                }.lparams(dip(20), dip(20)) {
                    gravity = Gravity.CENTER
                }

                countLike = textView {
                    id = R.id.commentFragmentTvCountLike
                    textColor = Color.BLACK
                    textSize = px2dip(dimen(R.dimen.feedCommentTextSize))
                }.lparams {
                    leftMargin = dip(10)
                    rightOf(R.id.commentFragmentAreaLike)
                    gravity = Gravity.CENTER_VERTICAL
                }

                countComment = textView {
                    textSize = px2dip(dimen(R.dimen.textSize15))
                    textColor = ContextCompat.getColor(context, R.color.colorGrayBold)
                    typeface = Typeface.DEFAULT_BOLD
                }.lparams {
                    gravity = Gravity.CENTER_VERTICAL
                    leftOf(R.id.commentFragmentTvCountLike)
                    alignParentRight()
                }
            }

            cmtFirst = imageView(R.drawable.cmt) {
                visibility = View.GONE
            }.lparams(dip(150), dip(150)) {
                below(R.id.commentFragmentToolBarLike)
            }

            recyclerComment = recyclerView {
                id = R.id.commentFragmentRecyclerViewComment
                layoutManager = LinearLayoutManager(context)
                adapter = commentsAdapter
            }.lparams(matchParent, wrapContent) {
                below(R.id.commentFragmentToolBarLike)
                topMargin = dip(20)
                bottomMargin = dip(60)
            }

            linearLayout {
                edtComment = editText {
                    id = R.id.commentFragmentEditComment
                    backgroundResource = R.drawable.custom_edittext_comment
                    textSize = px2dip(dimen(R.dimen.textSize15))
                    hint = "Add..."
                    hintTextColor = ContextCompat.getColor(context, R.color.colorGrayLight)
                    horizontalPadding = dip(10)
                    singleLine = true
                    inputType = InputType.TYPE_CLASS_TEXT
                    imeOptions = EditorInfo.IME_ACTION_DONE
                    setOnEditorActionListener({ _, p1, _ ->
                        if (p1 == EditorInfo.IME_ACTION_DONE) {
                            if (text.isNotBlank()) {
                                owner.eventWhenAddCommentClicked(text.toString().trim())
                            }
                            context.hideKeyboard(this)
                            this.clearFocus()
                        }
                        false
                    })
                }.lparams(matchParent, dip(40)) {
                    horizontalMargin = dip(5)
                    verticalMargin = dip(15)
                }
            }.lparams(matchParent, dip(60)) {
                alignParentBottom()
            }
        }
    }.view
}
