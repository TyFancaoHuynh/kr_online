package com.example.hoavot.karaokeonline.ui.feed

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import com.example.hoavot.karaokeonline.R
import com.example.hoavot.karaokeonline.data.model.other.Comment
import com.example.hoavot.karaokeonline.ui.extensions.circleImageView
import com.example.hoavot.karaokeonline.ui.extensions.enableHighLightWhenClicked
import com.example.hoavot.karaokeonline.ui.extensions.fontNomal
import de.hdodenhof.circleimageview.CircleImageView
import org.jetbrains.anko.*

/**
 *
 * @author at-hoavo.
 */
class FeedHolderUI : AnkoComponent<ViewGroup> {
    internal lateinit var avatar: CircleImageView
    internal lateinit var userName: TextView
    internal lateinit var recordArea: LinearLayout
    internal lateinit var yourMusic: TextView
    internal lateinit var fileMusic: TextView
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

                userName = textView {
                    typeface = Typeface.DEFAULT_BOLD
                    textSize = px2dip(dimen(R.dimen.textSize15))
                    textColor = Color.BLACK
                    fontNomal()
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
                horizontalMargin = dip(20)
            }

            fileMusic = textView {
                backgroundColor = ContextCompat.getColor(context, R.color.colorHightLight)
                textSize = px2dip(dimen(R.dimen.textSize15))
                visibility = View.GONE
                paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
                typeface = Typeface.DEFAULT_BOLD
                gravity = Gravity.CENTER
                enableHighLightWhenClicked()
            }.lparams(matchParent, dip(40)) {
                verticalMargin = dip(15)
                horizontalMargin = dip(30)
            }

            relativeLayout {
                lparams(matchParent, wrapContent) {
                    horizontalPadding = dip(20)
                    topMargin = dip(10)
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
                        enableHighLightWhenClicked()
                    }.lparams(dip(20), dip(20)) {
                        centerHorizontally()
                    }
                    unlike = imageView(R.drawable.ic_unlike_feed) {
                        id = R.id.feedFragmentImgUnLike
                        enableHighLightWhenClicked()
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
                    id = R.id.feedFragmentImgShare
                }.lparams(dip(20), dip(20)) {
                    weight = 1f
                    gravity = Gravity.CENTER
                }

            }

        }
    }
}
