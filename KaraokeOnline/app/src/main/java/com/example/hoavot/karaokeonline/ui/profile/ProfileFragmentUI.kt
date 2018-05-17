package com.example.hoavot.karaokeonline.ui.profile

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
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
 * @author at-hoavo.
 */
class ProfileFragmentUI(private val feeds: MutableList<Feed>,user: User) : AnkoComponent<ProfileFragment> {
    internal lateinit var avatar: ImageView
    internal lateinit var pickImage: ImageView
    internal lateinit var username: TextView
    internal lateinit var countFeed: TextView
    internal lateinit var editProfile: TextView
    internal lateinit var age: TextView
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
    internal var feedsAdapter = FeedAdapter(feeds,user)

    override fun createView(ui: AnkoContext<ProfileFragment>): View {
        return with(ui) {
            relativeLayout {
                lparams(matchParent, matchParent)
                backgroundColor=ContextCompat.getColor(context,R.color.colorSetting)
                imageView(R.drawable.ic_more_vert_black_36dp) {
                    id = R.id.profileFragmenMore
                    onClick {
                        owner.onMoreClick()
                    }
                }.lparams {
                    alignParentRight()
                    alignParentTop()
                    topMargin = dip(15)
                    rightMargin = dip(15)
                }

                relativeLayout {
                    id = R.id.profileFragmenAvatar
                    lparams(dip(150), dip(150)) {
                        topMargin = dip(20)
                        centerHorizontally()
                    }

                    avatar = imageView {
                        backgroundColor = Color.DKGRAY
//                        backgroundResource = R.drawable.user
                    }.lparams(matchParent, matchParent)

                    relativeLayout {
                        backgroundColor = ContextCompat.getColor(context, R.color.colorProfileAvatar)
                        pickImage = imageView(R.drawable.ic_camera_alt_red_300_24dp) {
                        }.lparams {
                            centerInParent()
                        }
                        enableHighLightWhenClicked()
                        onClick {
                            owner.eventOnCameraClick()
                        }
                    }.lparams(dip(150), dip(40)) {
                        alignParentBottom()
                        alignParentRight()
                    }
                }

                username = textView {
                    id = R.id.profileFragmentTvUsername
                    textSize = px2dip(dimen(R.dimen.textSize18))
                    typeface = Typeface.DEFAULT_BOLD
                    textColor = Color.BLACK
                }.lparams {
                    below(R.id.profileFragmenAvatar)
                    topMargin = dip(10)
                    centerHorizontally()
                }

                age = textView {
                    textSize = px2dip(dimen(R.dimen.textSize15))
                    textColor = Color.WHITE
                    visibility=View.INVISIBLE
                }.lparams {
                    rightOf(R.id.profileFragmentTvUsername)
                    sameTop(R.id.profileFragmentTvUsername)
                    leftMargin = dip(15)
                    centerHorizontally()
                }

                countFeed = textView {
                    id = R.id.profileFragmentTvCountFeed
                    textSize = px2dip(dimen(R.dimen.textSize14))
                    textColor = ContextCompat.getColor(context,R.color.colorBlackBold)
                    paintFlags = Paint.UNDERLINE_TEXT_FLAG
                }.lparams {
                    sameTop(R.id.profileFragmentTvUsername)
                    rightOf(R.id.profileFragmentTvUsername)
                    leftMargin=dip(10)
                }

                recyclerView {
                    backgroundColor = ContextCompat.getColor(context, R.color.colorItemFeed)
                    layoutManager = LinearLayoutManager(context)
                    adapter = feedsAdapter
                }.lparams(matchParent, matchParent) {
                    below(R.id.profileFragmentTvUsername)
                    topMargin = dip(20)
                }

                editProfile = textView("Edit Profile") {
                    textSize = px2dip(dimen(R.dimen.textSize15))
                    typeface = Typeface.SERIF
                    textColor = Color.BLACK
                    backgroundColor = ContextCompat.getColor(context, R.color.colorBackgroundEditprofile)
                    gravity = Gravity.CENTER
                    visibility = View.INVISIBLE
                    enableHighLightWhenClicked()
                    onClick {
                        owner.handleWhenEditProfileClick()
                    }
                }.lparams(dip(150), dip(30)) {
                    alignParentRight()
                    below(R.id.profileFragmenMore)
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
                        textSize = px2dip(dimen(R.dimen.textSize14))
                    }.lparams {
                        topMargin = dip(12)
                        rightOf(R.id.feedFragmentAvatarPlay)
                        leftMargin = dip(8)
                    }

                    filePlay = textView {
                        id = R.id.feedFragmentFilePlay
                        textColor = Color.WHITE
                        textSize = px2dip(dimen(R.dimen.textSize12))
                        maxLines=2
                        ellipsize = TextUtils.TruncateAt.END
                    }.lparams(wrapContent, dip(200)) {
                        rightMargin = getWidth() / 2
                        sameLeft(R.id.feedFragmentUsernamePlay)
                        below(R.id.feedFragmentUsernamePlay)
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
}
