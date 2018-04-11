package com.example.hoavot.karaokeonline.ui.feed

import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import com.example.hoavot.karaokeonline.R
import com.example.hoavot.karaokeonline.data.model.other.Comment
import de.hdodenhof.circleimageview.CircleImageView
import org.jetbrains.anko.*
import org.jetbrains.anko.custom.ankoView

/**
 *
 * @author at-hoavo.
 */
class FeedHolderUI : AnkoComponent<ViewGroup> {
    internal lateinit var avatar: CircleImageView
    internal lateinit var userName: TextView
    internal lateinit var recordArea: LinearLayout
    internal lateinit var playRecord: ImageView
    internal lateinit var pauseRecord: ImageView
    internal lateinit var seekbar: SeekBar
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
            backgroundColor = Color.WHITE
            lparams(matchParent, wrapContent) {
                topMargin = dip(10)
            }
            linearLayout {
                lparams(matchParent, dip(50))
                avatar = circleImageView {
                    borderWidth = dip(0.4f)
                    borderColor = Color.GRAY
                }.lparams(dip(40), dip(40)) {
                    leftMargin = dip(3)
                    topMargin = dip(3)
                }

                userName = textView("bbjds") {
                    typeface = Typeface.DEFAULT_BOLD
                    textSize = px2dip(dimen(R.dimen.feedTitleTextSize))
                    textColor = Color.BLACK
//                    fontNomal()
                }.lparams {
                    topMargin = dip(13)
                    leftMargin = dip(5)
                }
            }

            captionArea = textView {
                maxLines = 6
                minLines = 1
                textColor = Color.BLACK
                textSize = px2dip(dimen(R.dimen.feedCaptionTextSize))
            }.lparams(matchParent, wrapContent) {
                leftMargin = dip(20)
            }

            recordArea = linearLayout {
                orientation = LinearLayout.HORIZONTAL
                lparams(matchParent, dip(50)) {
                    horizontalMargin = dip(20)
                    verticalMargin = dip(10)
                }
                relativeLayout {
                    playRecord = imageView(R.drawable.ic_play_song_feed) {
                    }.lparams(dip(30), dip(30))
                    pauseRecord = imageView(R.drawable.ic_pause_song_feed) {
                        visibility = View.INVISIBLE
                    }.lparams(dip(30), dip(30))
                }

                seekbar = seekBar {

                }.lparams(matchParent, wrapContent) {
                    topMargin = dip(7)
                }

            }

            relativeLayout {
                lparams(matchParent, wrapContent) {
                    horizontalPadding = dip(20)
                }

                imageView(R.drawable.ic_count_like) {
                    id = R.id.imgCountLike
                }.lparams(dip(15), dip(15))

                countLike = textView {
                    id = R.id.tvCountLike
                    textColor = Color.BLACK
                    textSize = px2dip(dimen(R.dimen.feedCommentTextSize))
                }.lparams {
                    leftMargin = dip(5)
                    rightOf(R.id.imgCountLike)
                }

                countComment = textView {
                    id = R.id.tvComment
                    textSize = px2dip(dimen(R.dimen.feedCaptionTextSize))
                }.lparams {
                    leftOf(R.id.tvCountLike)
                    alignParentRight()
                }
            }

            view {
                backgroundColor = R.color.colorLineFeedScreen
            }.lparams(matchParent, dip(0.5f)) {
                horizontalMargin = dip(20)
                topMargin = dip(10)
            }

            linearLayout {
                lparams(matchParent, dip(40))
                id = R.id.feedFragmentLikeArea

                relativeLayout {
                    like = imageView(R.drawable.ic_count_like) {
                        id = R.id.feedFragmentImgUnLike
                    }.lparams(dip(20), dip(20)) {
                        centerHorizontally()
                    }
                    unlike = imageView(R.drawable.ic_unlike_feed) {
                        id = R.id.feedFragmentImgUnLike
                    }.lparams(dip(20), dip(20)) {
                        centerHorizontally()
                    }
                }.lparams(dip(20), dip(20)) {
                    weight = 1f
                    gravity = Gravity.CENTER
                }

                comment = imageView(R.drawable.ic_comment_feed) {
                    id = R.id.feedFragmentImgComment
                }.lparams(dip(20), dip(20)) {
                    weight = 1f
                    gravity = Gravity.CENTER
                }

                share = imageView(R.drawable.ic_share_feed) {

                }.lparams(dip(20), dip(20)) {
                    weight = 1f
                    gravity = Gravity.CENTER
                }

            }

        }

    }

    inline fun ViewManager.circleImageView(init: CircleImageView.() -> Unit): CircleImageView
            = ankoView({ CircleImageView(it) }, 0, init)
}
