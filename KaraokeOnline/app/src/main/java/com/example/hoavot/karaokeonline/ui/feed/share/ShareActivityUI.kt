package com.example.hoavot.karaokeonline.ui.feed.share

import android.graphics.Color
import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.util.Log.d
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.example.hoavot.karaokeonline.R
import com.example.hoavot.karaokeonline.data.model.other.User
import com.example.hoavot.karaokeonline.ui.extensions.circleImageView
import com.example.hoavot.karaokeonline.ui.extensions.enableHighLightWhenClicked
import de.hdodenhof.circleimageview.CircleImageView
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 *
 * @author at-hoavo
 */
class ShareActivityUI(private val fileMusic: String, private val user: User) : AnkoComponent<ShareActivity> {
    internal lateinit var avatar: CircleImageView
    internal lateinit var tvUsername: TextView
    internal lateinit var tvSong: TextView
    internal lateinit var edtCaption: EditText

    override fun createView(ui: AnkoContext<ShareActivity>): View {
        return with(ui) {
            relativeLayout {
                lparams(matchParent, matchParent)
//                backgroundColor = ContextCompat.getColor(context, R.color.colorShareBG)
                backgroundResource = R.drawable.bg_share
                relativeLayout {
                    lparams(matchParent, matchParent) {
                        verticalMargin = dip(50)
                        gravity = Gravity.CENTER_HORIZONTAL
                        horizontalMargin = dip(20)
                    }
                    backgroundColor = Color.WHITE
                    avatar = circleImageView {
                        id = R.id.captionFragmentAvartar
                        borderWidth = dip(0.4f)
                        borderColor = Color.GRAY
                    }.lparams(dip(50), dip(50)) {
                        leftMargin = dip(15)
                        verticalMargin = dip(15)
                    }

                    tvUsername = textView(user.username) {
                        id = R.id.captionFragmenttvUsername
                        textSize = px2dip(dimen(R.dimen.textSize15))
                        typeface = Typeface.DEFAULT_BOLD
                        textColor = Color.BLACK
                    }.lparams {
                        rightOf(R.id.captionFragmentAvartar)
                        sameTop(R.id.captionFragmentAvartar)
                        leftMargin = dip(5)
                    }

                    textView("Bài hát của tôi") {
                        id = R.id.shareActivityTextMusic
                        textSize = px2dip(dimen(R.dimen.textSize14))
                        typeface = Typeface.DEFAULT_BOLD
                        textColor = ContextCompat.getColor(context, R.color.colorBlackBold)
                    }.lparams {
                        below(R.id.captionFragmentAvartar)
                        leftMargin = dip(10)
                    }

                    tvSong = textView(fileMusic) {
                        id = R.id.shareActivityTextSong
                        textSize = px2dip(dimen(R.dimen.textSize13))
                        typeface = Typeface.DEFAULT_BOLD
                        textColor = ContextCompat.getColor(context, R.color.colorGrayLight)
                    }.lparams {
                        sameLeft(R.id.shareActivityTextMusic)
                        below(R.id.shareActivityTextMusic)
                    }

                    edtCaption = editText {
                        backgroundColor = Color.TRANSPARENT
                        backgroundResource = R.drawable.custom_edittext_search_video
                        topPadding = dip(20)
                        hint = resources.getString(R.string.uploadReviewFragmentEdtCaptionHint)
                        gravity = Gravity.TOP or Gravity.START
                        id = R.id.shareActivityCaption
                        hintTextColor = ContextCompat.getColor(ctx, R.color.colorGrayLight)
                        textColor = ContextCompat.getColor(ctx, android.R.color.black)
                        textSize = px2dip(dimen(R.dimen.captionFragmentEdtCaptionTextSize))
                        horizontalPadding = dip(10)
                    }.lparams(matchParent, dip(200)) {
                        horizontalMargin = dip(10)
                        below(R.id.shareActivityTextSong)
                        topMargin = dip(10)
                    }

                    button("Share") {
                        enableHighLightWhenClicked()
                        textColor = Color.WHITE
                        backgroundColor = ContextCompat.getColor(context, R.color.colorPlayFeed)
                        onClick {
                            d("TAGGG", "on share touch")
                            owner.eventWhenShareClicked()
                        }
                    }.lparams(dip(150), dip(40)) {
                        below(R.id.shareActivityCaption)
                        topMargin = dip(50)
                        centerHorizontally()
                    }
                }
            }
        }

    }
}
