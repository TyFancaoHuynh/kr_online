package com.example.hoavot.karaokeonline.ui.feed

import android.graphics.Color
import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
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
    internal lateinit var optionArea: LinearLayout
    internal lateinit var more: ImageView
    internal lateinit var fileMusic: TextView
    internal lateinit var like: ImageView
    internal lateinit var unlike: ImageView
    internal lateinit var countLike: TextView
    internal lateinit var countComment: TextView
    internal lateinit var captionArea: TextView
    internal lateinit var comment: ImageView
    internal lateinit var share: ImageView
    internal lateinit var updateFeed: TextView
    internal lateinit var deleteFeed: TextView
    internal lateinit var time: TextView
    internal lateinit var imgBGMusic: ImageView
    internal lateinit var rlParent: RelativeLayout
    internal lateinit var likeSmall: ImageView
    internal var comments: MutableList<Comment> = mutableListOf()

    override fun createView(ui: AnkoContext<ViewGroup>): View = ui.apply {
        rlParent = relativeLayout {
            backgroundColor = Color.WHITE
            lparams(matchParent, wrapContent) {
                bottomMargin = dip(10)
            }
            linearLayout {
                id = R.id.profileFragmenAreaImage
                avatar = circleImageView {
                    borderWidth = dip(0.4f)
                    borderColor = Color.GRAY
                }.lparams(dip(40), dip(40)) {
                    leftMargin = dip(3)
                    topMargin = dip(3)
                }

                verticalLayout {
                    userName = textView {
                        typeface = Typeface.DEFAULT_BOLD
                        textSize = px2dip(dimen(R.dimen.textSize16))
                        textColor = Color.BLACK
                    }
                    time = textView {
                        textSize = px2dip(dimen(R.dimen.textSize13))
                        textColor = ContextCompat.getColor(context, R.color.colorGrayLight)
                        fontNomal()
                    }.lparams {
                        topMargin = dip(5)
                        leftMargin = dip(5)
                    }
                }.lparams {
                    topMargin = dip(13)
                    leftMargin = dip(5)
                }

            }.lparams(matchParent, dip(65)) {
                alignParentTop()
            }

            captionArea = textView {
                id = R.id.profileFragmenCaption
                maxLines = 6
                minLines = 1
                textColor = Color.BLACK
                textSize = px2dip(dimen(R.dimen.feedCaptionTextSize))
            }.lparams(matchParent, wrapContent) {
                horizontalMargin = dip(20)
                below(R.id.profileFragmenAreaImage)
            }

            imgBGMusic = imageView(R.drawable.bg_play) {
                scaleType = ImageView.ScaleType.CENTER_CROP
                id = R.id.profileFragmenImgMusic
            }.lparams(matchParent, dip(150)) {
                topMargin = dip(10)
                below(R.id.profileFragmenCaption)
            }

            verticalLayout {
                id = R.id.profileFragmenFileMusic
                backgroundColor = ContextCompat.getColor(context, R.color.colorBGFileName)
                textView("MP3 File") {
                    textColor = ContextCompat.getColor(context, R.color.colorTextMp3)
                    textSize = px2dip(dimen(R.dimen.textSize13))
                }.lparams {
                    topMargin = dip(5)
                    leftMargin=dip(3)
                }

                fileMusic = textView {
                    textColor = ContextCompat.getColor(context, R.color.colorTextFileMusic)
                    textSize = px2dip(dimen(R.dimen.textSize15))
                    typeface = Typeface.DEFAULT_BOLD
                }.lparams {
                    topMargin = dip(2)
                    leftMargin=dip(3)
                }
            }.lparams(matchParent, dip(60)) {
                below(R.id.profileFragmenImgMusic)
            }

            relativeLayout {
                id = R.id.profileFragmenAreaMusic
                lparams(matchParent, wrapContent) {
                    below(R.id.profileFragmenFileMusic)
                    horizontalPadding = dip(20)
                    topMargin = dip(20)
                }

                likeSmall = imageView(R.drawable.ic_heart_small) {
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

            linearLayout {
                id = R.id.feedFragmentLikeArea

                relativeLayout {
                    like = imageView(R.drawable.ic_heart_origin) {
                        id = R.id.feedFragmentImgUnLike
                        enableHighLightWhenClicked()
                    }.lparams(dip(20), dip(20)) {
                        centerHorizontally()
                    }
                    unlike = imageView(R.drawable.ic_heart_96) {
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

            }.lparams(matchParent, dip(40)) {
                below(R.id.profileFragmenAreaMusic)
            }

            more = imageView(R.drawable.ic_more_vert_grey_500_24dp) {
                id = R.id.feedFragmentMore
                visibility = View.INVISIBLE
            }.lparams {
                alignParentRight()
                alignParentTop()
                topMargin = dip(15)
                rightMargin = dip(15)
            }

            optionArea = verticalLayout {
                visibility = View.INVISIBLE
                updateFeed = textView("Update") {
                    textSize = px2dip(dimen(R.dimen.textSize15))
                    visibility=View.GONE
                    typeface = Typeface.DEFAULT_BOLD
                    textColor = ContextCompat.getColor(context, R.color.colorButton)
                    padding = dip(5)
                    backgroundColor = ContextCompat.getColor(context, R.color.colorLineFeedScreen)
                    gravity = Gravity.CENTER
                    enableHighLightWhenClicked()
                }.lparams(matchParent, wrapContent)

                view {
                    id = R.id.profileFragmenLine
                    backgroundColor = R.color.colorPurpose
                }.lparams(matchParent, dip(0.5f)) {
                    topMargin = dip(2)
                }

                deleteFeed = textView("Delete") {
                    textSize = px2dip(dimen(R.dimen.textSize15))
                    typeface = Typeface.DEFAULT_BOLD
                    textColor = ContextCompat.getColor(context, R.color.colorButton)
                    padding = dip(5)
                    backgroundColor = ContextCompat.getColor(context, R.color.colorLineFeedScreen)
                    gravity = Gravity.CENTER
                    enableHighLightWhenClicked()
                }.lparams(matchParent, wrapContent)
            }.lparams(dip(150), dip(80)) {
                alignParentRight()
                below(R.id.feedFragmentMore)
            }
        }
    }.view
}
