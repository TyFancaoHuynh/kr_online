package com.example.hoavot.karaokeonline.ui.feed

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewManager
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.hoavot.karaokeonline.R
import com.example.hoavot.karaokeonline.data.model.other.Feed
import com.example.hoavot.karaokeonline.data.model.other.User
import com.example.hoavot.karaokeonline.ui.extensions.enableHighLightWhenClicked
import de.hdodenhof.circleimageview.CircleImageView
import org.jetbrains.anko.*
import org.jetbrains.anko.custom.ankoView
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.sdk25.coroutines.onTouch

/**
 *
 * @author at-hoavo.
 */
class FeedFragmentUI(private val feeds: MutableList<Feed>, user: User) : AnkoComponent<FeedFragment> {
    internal val feedsAdapter = FeedAdapter(feeds, true)
    internal lateinit var circleImgAvatarStatus: CircleImageView
    internal lateinit var areaPlay: RelativeLayout
    internal lateinit var avatarPlay: CircleImageView
    internal lateinit var usernamePlay: TextView
    internal lateinit var filePlay: TextView
    internal lateinit var mImgBtnNext: ImageButton
    internal lateinit var mImgBtnPrevious: ImageButton
    internal lateinit var mImgBtnPlay: ImageButton
    internal lateinit var mImgBtnPause: ImageButton
    internal lateinit var recyclerView: RecyclerView

    override fun createView(ui: AnkoContext<FeedFragment>): View {
        return with(ui) {
            relativeLayout {
                //                backgroundColor = ContextCompat.getColor(context,R.color.colorStatus)
                backgroundColor = Color.WHITE
                lparams(matchParent, matchParent)
//                backgroundColor = ContextCompat.getColor(context, R.color.colorItemFeed)
                linearLayout {
                    verticalPadding = dip(10)
                    orientation = LinearLayout.HORIZONTAL
                    id = R.id.feedFragmentAreaCaption
                    lparams(matchParent, dip(60))
                    circleImgAvatarStatus = circleImageView {
                        borderWidth = dip(0.4f)
                        borderColor = Color.GRAY
                    }.lparams(dip(35), dip(35)) {
                        gravity = Gravity.CENTER
                        weight = 1f
                    }
                    editText {
                        id = R.id.feedFragmentAreaEdtCaption
                        backgroundResource = R.drawable.custom_item_feed
                        hint = "Bạn đang nghĩ gì?..."
                        textSize = px2dip(dimen(R.dimen.feedTitleTextSize))
                        leftPadding = dip(10)
                        hintTextColor = ContextCompat.getColor(context, R.color.colorGrayLight)
                        maxLines = 1
                        isEnabled = true
                        isFocusable = false
                        singleLine = true
                        onTouch { v, event ->
                            if (event.action == MotionEvent.ACTION_DOWN) {
                                owner.eventOnAddCaptionClicked()
                            }
                        }
                    }.lparams(dip(230), dip(40)) {
                        verticalMargin = dip(5)
                        rightMargin = dip(5)
                        gravity = Gravity.CENTER
                        weight = 4f
                    }
                    verticalLayout {
                        onClick { owner.eventOnAddCaptionClicked() }
                        imageView(R.drawable.ic_add_song_feed) {
                        }
                        textView {
                            text = "Music"
                            textColor = Color.BLACK
                            textSize = px2dip(dip(10))
                        }
                    }.lparams {
                        gravity = Gravity.CENTER_VERTICAL
                        rightMargin = dip(10)
                    }
                }.lparams(matchParent, dip(60))

                recyclerView = recyclerView {
                    backgroundColor = ContextCompat.getColor(context, R.color.colorItemFeed)
                    layoutManager = LinearLayoutManager(context)
                    adapter = feedsAdapter
                }.lparams(matchParent, matchParent) {
                    below(R.id.feedFragmentAreaCaption)
                }

                areaPlay = relativeLayout {
                    backgroundColor = ContextCompat.getColor(context, R.color.colorPlayFeed)
                    visibility = View.GONE
                    avatarPlay = circleImageView {
                        id = R.id.feedFragmentAvatarPlay
                    }.lparams(dip(60), dip(60)) {
                        alignParentLeft()
                        centerVertically()
                        leftMargin = dip(5)
                    }

                    usernamePlay = textView {
                        id = R.id.feedFragmentUsernamePlay
                        textColor = Color.WHITE
                        textSize = px2dip(dimen(R.dimen.textSize16))
                    }.lparams {
                        topMargin = dip(12)
                        rightOf(R.id.feedFragmentAvatarPlay)
                        leftMargin = dip(8)
                    }

                    filePlay = textView {
                        id = R.id.feedFragmentFilePlay
                        textColor = ContextCompat.getColor(context, R.color.colorFilePlay)
                        textSize = px2dip(dimen(R.dimen.textSize12))
                        maxLines = 2
                        ellipsize = TextUtils.TruncateAt.END
                    }.lparams {
                        sameLeft(R.id.feedFragmentUsernamePlay)
                        leftOf(R.id.feedFragmentImgPrevious)
                        below(R.id.feedFragmentUsernamePlay)
                        rightMargin = dip(10)
                    }

                    mImgBtnNext = imageButton(R.drawable.next_feed) {
                        id = R.id.feedFragmentNextPlay
                        enableHighLightWhenClicked()
                        onClick {
                            owner.eventOnButtonClicked(mImgBtnNext)
                        }
                    }.lparams {
                        below(R.id.feedFragmentColsePlay)
                        alignParentRight()
                        rightMargin = dip(5)
                        topMargin = dip(5)
                    }

                    mImgBtnPause = imageButton(R.drawable.pause_feed) {
                        visibility = View.INVISIBLE
                        id = R.id.feedFragmentPreviousPlay
                        enableHighLightWhenClicked()
                        onClick {
                            owner.eventOnButtonClicked(mImgBtnPause)
                        }
                    }.lparams {
                        leftOf(R.id.feedFragmentNextPlay)
                        rightMargin = dip(5)
                        sameTop(R.id.feedFragmentNextPlay)
                    }

                    mImgBtnPlay = imageButton(R.drawable.play_feed) {
                        enableHighLightWhenClicked()
                        onClick {
                            owner.eventOnButtonClicked(mImgBtnPlay)
                        }
                    }.lparams {
                        leftOf(R.id.feedFragmentNextPlay)
                        rightMargin = dip(5)
                        sameTop(R.id.feedFragmentNextPlay)
                    }

                    mImgBtnPrevious = imageButton(R.drawable.previous_feed) {
                        id = R.id.feedFragmentImgPrevious
                        enableHighLightWhenClicked()
                        onClick {
                            owner.eventOnButtonClicked(mImgBtnPrevious)
                        }
                    }.lparams {
                        leftOf(R.id.feedFragmentPreviousPlay)
                        rightMargin = dip(5)
                        sameTop(R.id.feedFragmentNextPlay)
                    }

                    imageView(R.drawable.ic_close_white_36dp) {
                        id = R.id.feedFragmentColsePlay
                        padding = dip(5)
                        onClick {
                            owner.eventClosePlayFeedClicked()
                        }
                    }.lparams(dip(35), dip(35)) {
                        alignParentRight()
                        alignParentTop()
                    }
                }.lparams(matchParent, dip(80)) {
                    alignParentBottom()
                }

            }
        }
    }

    inline fun ViewManager.circleImageView(init: CircleImageView.() -> Unit): CircleImageView = ankoView({ CircleImageView(it) }, 0, init)
}
