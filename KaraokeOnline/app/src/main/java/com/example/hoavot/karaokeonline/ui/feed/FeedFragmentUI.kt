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
import android.widget.*
import com.example.hoavot.karaokeonline.R
import com.example.hoavot.karaokeonline.data.model.other.Feed
import com.example.hoavot.karaokeonline.ui.extensions.getWidthScreen
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
class FeedFragmentUI(private val feeds: MutableList<Feed>) : AnkoComponent<FeedFragment> {
    internal val feedsAdapter = FeedAdapter(feeds)
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
                lparams(matchParent, matchParent)
//                backgroundColor = ContextCompat.getColor(context, R.color.colorItemFeed)
                linearLayout {
                    backgroundColor = Color.WHITE
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
                        hint = "What do you think?..."
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
                }.lparams(matchParent, dip(40)) {
                    topMargin = dip(10)
                }

                recyclerView = recyclerView {
                    backgroundColor = ContextCompat.getColor(context, R.color.colorItemFeed)
                    layoutManager = LinearLayoutManager(context)
                    adapter = feedsAdapter
                }.lparams(matchParent, matchParent) {
                    below(R.id.feedFragmentAreaCaption)
                    topMargin = dip(10)
                }

                areaPlay = relativeLayout {
                    backgroundColor = ContextCompat.getColor(context, R.color.colorPlayFeed)
                    visibility = View.GONE
                    avatarPlay = circleImageView {
                        id = R.id.feedFragmentAvatarPlay
                    }.lparams(dip(25), dip(25)) {
                        alignParentLeft()
                        leftMargin = dip(5)
                    }

                    usernamePlay = textView {
                        id = R.id.feedFragmentUsernamePlay
                        textColor = Color.WHITE
                        textSize = px2dip(dimen(R.dimen.textSize14))
                    }.lparams {
                        alignParentTop()
                        topMargin = dip(7)
                        rightOf(R.id.feedFragmentAvatarPlay)
                    }

                    filePlay = textView {
                        textColor = Color.WHITE
                        textSize = px2dip(dimen(R.dimen.textSize12))
                        ellipsize = TextUtils.TruncateAt.END
                    }.lparams(wrapContent, dip(context.getWidthScreen() / 2)) {
                        below(R.id.feedFragmentUsernamePlay)
                        sameLeft(R.id.feedFragmentUsernamePlay)
                        topMargin = dip(5)
                    }

                    mImgBtnNext = imageButton(R.drawable.next_feed) {
                        id = R.id.feedFragmentNextPlay
                        onClick {
                            owner.eventOnButtonClicked(mImgBtnNext)
                        }
                    }.lparams {
                        alignParentRight()
                        rightMargin = dip(5)
                        centerVertically()
                    }

                    mImgBtnPause = imageButton(R.drawable.pause_feed) {
                        visibility = View.INVISIBLE
                        id = R.id.feedFragmentPreviousPlay
                        onClick {
                            owner.eventOnButtonClicked(mImgBtnPlay)
                        }
                    }.lparams {
                        leftOf(R.id.feedFragmentNextPlay)
                        rightMargin = dip(5)
                        centerVertically()
                    }

                    mImgBtnPlay = imageButton(R.drawable.play_feed) {
                        onClick {
                            owner.eventOnButtonClicked(mImgBtnPause)
                        }
                    }.lparams {
                        leftOf(R.id.feedFragmentNextPlay)
                        rightMargin = dip(5)
                        centerVertically()
                    }

                    mImgBtnPrevious = imageButton(R.drawable.previous_feed) {
                        onClick {
                            owner.eventOnButtonClicked(mImgBtnPrevious)
                        }
                    }.lparams {
                        leftOf(R.id.feedFragmentPreviousPlay)
                        rightMargin = dip(5)
                        centerVertically()
                    }

                }.lparams(matchParent, dip(60)) {
                    alignParentBottom()
                }

            }
        }
    }

    inline fun ViewManager.circleImageView(init: CircleImageView.() -> Unit): CircleImageView
            = ankoView({ CircleImageView(it) }, 0, init)
}
