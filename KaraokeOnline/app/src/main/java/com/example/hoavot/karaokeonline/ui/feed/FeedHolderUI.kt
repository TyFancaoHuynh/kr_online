package com.example.hoavot.karaokeonline.ui.feed

import android.graphics.Color
import android.graphics.Typeface
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.hoavot.karaokeonline.R
import com.example.hoavot.karaokeonline.data.model.other.Comment
import com.example.hoavot.karaokeonline.ui.extensions.fontNomal
import com.example.hoavot.karaokeonline.ui.feed.comment.CommentAdapter
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView

/**
 *
 * @author at-hoavo.
 */
class FeedHolderUI : AnkoComponent<ViewGroup> {
    internal lateinit var avatar: ImageView
    internal lateinit var userName: TextView
    internal lateinit var recordArea: RelativeLayout
    internal lateinit var playRecord: ImageButton
    internal lateinit var pauseRecord: ImageButton
    internal lateinit var progressBar: ProgressBar
    internal lateinit var like: ImageView
    internal lateinit var unlike: ImageView
    internal lateinit var countLike: TextView
    internal lateinit var countComment: TextView
    internal lateinit var captionArea: TextView
    internal lateinit var comment: ImageView
    internal lateinit var share: ImageView
    internal var comments: MutableList<Comment> = mutableListOf()

    override fun createView(ui: AnkoContext<ViewGroup>): View = with(ui) {
        verticalLayout {
            lparams(matchParent, wrapContent) {
                topMargin = dip(10)
            }
            backgroundResource = R.drawable.custom_item_feed
            linearLayout {
                lparams(matchParent, dip(50))
                avatar = imageView {
                }.lparams(dip(40), dip(40)) {
                    leftMargin = dip(3)
                    topMargin = dip(3)
                }

                userName = textView("bbjds") {
                    textSize = px2dip(dimen(R.dimen.feedTitleTextSize))
                    textColor = Color.BLACK
                    fontNomal()
                }.lparams {
                    leftMargin = dip(5)
                }
            }

            captionArea = textView {
                maxLines = 6
                minLines = 1
                textSize = px2dip(dimen(R.dimen.feedCaptionTextSize))
            }.lparams(matchParent, wrapContent) {
                leftMargin = dip(20)
            }

            recordArea = relativeLayout {
                lparams(matchParent, dip(50)) {
                    margin = dip(20)
                }
                playRecord = imageButton { }.lparams(dip(20), dip(20))
                pauseRecord = imageButton { }.lparams(dip(20), dip(20))
            }

            relativeLayout {
                lparams(matchParent, dip(20))
                relativeLayout {
                    lparams(dip(41), matchParent)
                    id = R.id.feedFragmentLikeArea
                    unlike = imageView(R.mipmap.ic_unlike) {
                        id = R.id.feedFragmentImgUnLike
                    }.lparams(matchParent, matchParent) {
                        centerInParent()
                    }

                    like = imageView(R.mipmap.ic_like) {
                        visibility = View.INVISIBLE
                        id = R.id.feedFragmentImgLike
                    }.lparams(matchParent, matchParent) {
                        centerInParent()
                    }
                }

                countLike = textView {
                    textSize = px2dip(dimen(R.dimen.feedCommentTextSize))
                }.lparams {
                    rightOf(R.id.feedFragmentLikeArea)
                }

                linearLayout {
                    relativeLayout {
                        lparams(dip(41), matchParent) {
                            rightMargin = dip(5)
                        }
                        comment = imageView(R.mipmap.ic_comment_feed) {
                        }.lparams(matchParent, matchParent) {
                            centerInParent()
                        }
                    }

                    relativeLayout {
                        lparams(dip(41), matchParent) {
                            rightMargin = dip(15)
                        }
                        share = imageView(R.mipmap.ic_share) {

                        }.lparams(matchParent, matchParent) {
                            centerInParent()
                        }
                    }
                }.lparams {
                    alignParentRight()
                }
            }

            countComment = textView {
                typeface = Typeface.MONOSPACE
                textSize = px2dip(dimen(R.dimen.feedCommentTextSize))
            }.lparams {
                verticalMargin = dip(10)
                leftMargin = dip(5)
            }
        }
    }
}
