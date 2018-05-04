package com.example.hoavot.karaokeonline.ui.feed

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewManager
import android.widget.ImageView
import android.widget.LinearLayout
import com.example.hoavot.karaokeonline.R
import com.example.hoavot.karaokeonline.data.model.other.Feed
import de.hdodenhof.circleimageview.CircleImageView
import org.jetbrains.anko.*
import org.jetbrains.anko.custom.ankoView
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.sdk25.coroutines.onTouch

/**
 *
 * @author at-hoavo.
 */
class FeedFragmentUI(private val feeds: MutableList<Feed>) : AnkoComponent<FeedFragment> {
    internal val feedsAdapter = FeedAdapter(feeds)
    internal lateinit var circleImgAvatarStatus: CircleImageView
    internal lateinit var addCaption: ImageView
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

                recyclerView {
                    backgroundColor = ContextCompat.getColor(context, R.color.colorItemFeed)
                    layoutManager = LinearLayoutManager(context)
                    adapter = feedsAdapter
                }.lparams(matchParent, matchParent) {
                    below(R.id.feedFragmentAreaCaption)
                    topMargin = dip(10)
                }
            }
        }
    }

    inline fun ViewManager.circleImageView(init: CircleImageView.() -> Unit): CircleImageView
            = ankoView({ CircleImageView(it) }, 0, init)
}
