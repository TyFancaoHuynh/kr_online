package com.example.hoavot.karaokeonline.ui.base.feed

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.hoavot.karaokeonline.R
import com.example.hoavot.karaokeonline.data.model.other.Feed
import com.example.hoavot.karaokeonline.data.model.other.User
import com.example.hoavot.karaokeonline.ui.extensions.circleImageView
import com.example.hoavot.karaokeonline.ui.extensions.enableHighLightWhenClicked
import com.example.hoavot.karaokeonline.ui.feed.FeedAdapter
import de.hdodenhof.circleimageview.CircleImageView
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 *
 * @author at-hoavo
 */
//class BaseFeedFragmentUI(private val feeds: MutableList<Feed>, user: User) : AnkoComponent<BaseFeedFragment> {
//    internal val feedsAdapter = FeedAdapter(feeds, user,)
//    internal lateinit var recyclerView: RecyclerView
//    internal lateinit var areaPlay: RelativeLayout
//    internal lateinit var avatarPlay: CircleImageView
//    internal lateinit var usernamePlay: TextView
//    internal lateinit var filePlay: TextView
//    internal lateinit var mImgBtnNext: ImageButton
//    internal lateinit var mImgBtnPrevious: ImageButton
//    internal lateinit var mImgBtnPlay: ImageButton
//    internal lateinit var mImgBtnPause: ImageButton
//    override fun createView(ui: AnkoContext<BaseFeedFragment>): View {
//        return with(ui) {
//            relativeLayout {
//                lparams(matchParent, matchParent)
//                recyclerView = recyclerView {
//                    backgroundColor = ContextCompat.getColor(context, R.color.colorItemFeed)
//                    layoutManager = LinearLayoutManager(context)
//                    adapter = feedsAdapter
//                }.lparams(matchParent, matchParent) {
//                    below(R.id.feedFragmentAreaCaption)
//                    topMargin = dip(10)
//                }
//
//                areaPlay = relativeLayout {
//                    backgroundColor = ContextCompat.getColor(context, R.color.colorPlayFeed)
//                    visibility = View.GONE
//                    avatarPlay = circleImageView {
//                        id = R.id.feedFragmentAvatarPlay
//                    }.lparams(dip(60), dip(60)) {
//                        alignParentLeft()
//                        centerVertically()
//                        leftMargin = dip(5)
//                    }
//
//                    usernamePlay = textView {
//                        id = R.id.feedFragmentUsernamePlay
//                        textColor = Color.WHITE
//                        textSize = px2dip(dimen(R.dimen.textSize14))
//                    }.lparams {
//                        topMargin = dip(12)
//                        rightOf(R.id.feedFragmentAvatarPlay)
//                        leftMargin = dip(8)
//                    }
//
//                    filePlay = textView {
//                        id = R.id.feedFragmentFilePlay
//                        textColor = Color.WHITE
//                        textSize = px2dip(dimen(R.dimen.textSize12))
//                        maxLines = 2
//                        ellipsize = TextUtils.TruncateAt.END
//                    }.lparams(wrapContent, dip(200)) {
//                        rightMargin = getWidth() / 2
//                        sameLeft(R.id.feedFragmentUsernamePlay)
//                        below(R.id.feedFragmentUsernamePlay)
//                    }
//
//                    mImgBtnNext = imageButton(R.drawable.next_feed) {
//                        id = R.id.feedFragmentNextPlay
//                        enableHighLightWhenClicked()
//                        onClick {
//                            owner.eventOnButtonClicked(mImgBtnNext)
//                        }
//                    }.lparams {
//                        below(R.id.feedFragmentColsePlay)
//                        alignParentRight()
//                        rightMargin = dip(5)
//                        topMargin = dip(5)
//                    }
//
//                    mImgBtnPause = imageButton(R.drawable.pause_feed) {
//                        visibility = View.INVISIBLE
//                        id = R.id.feedFragmentPreviousPlay
//                        enableHighLightWhenClicked()
//                        onClick {
//                            owner.eventOnButtonClicked(mImgBtnPause)
//                        }
//                    }.lparams {
//                        leftOf(R.id.feedFragmentNextPlay)
//                        rightMargin = dip(5)
//                        sameTop(R.id.feedFragmentNextPlay)
//                    }
//
//                    mImgBtnPlay = imageButton(R.drawable.play_feed) {
//                        enableHighLightWhenClicked()
//                        onClick {
//                            owner.eventOnButtonClicked(mImgBtnPlay)
//                        }
//                    }.lparams {
//                        leftOf(R.id.feedFragmentNextPlay)
//                        rightMargin = dip(5)
//                        sameTop(R.id.feedFragmentNextPlay)
//                    }
//
//                    mImgBtnPrevious = imageButton(R.drawable.previous_feed) {
//                        enableHighLightWhenClicked()
//                        onClick {
//                            owner.eventOnButtonClicked(mImgBtnPrevious)
//                        }
//                    }.lparams {
//                        leftOf(R.id.feedFragmentPreviousPlay)
//                        rightMargin = dip(5)
//                        sameTop(R.id.feedFragmentNextPlay)
//                    }
//
//                    imageView(R.drawable.ic_close_white_36dp) {
//                        id = R.id.feedFragmentColsePlay
//                        padding = dip(5)
//                        onClick {
//                            owner.eventClosePlayFeedClicked()
//                        }
//                    }.lparams(dip(35), dip(35)) {
//                        alignParentRight()
//                        alignParentTop()
//                    }
//                }.lparams(matchParent, dip(80)) {
//                    alignParentBottom()
//                }
//
//            }
//        }
//    }

//}