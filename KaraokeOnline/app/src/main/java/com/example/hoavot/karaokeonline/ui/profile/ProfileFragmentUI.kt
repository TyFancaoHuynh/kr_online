package com.example.hoavot.karaokeonline.ui.profile

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.hoavot.karaokeonline.R
import com.example.hoavot.karaokeonline.data.model.other.Feed
import com.example.hoavot.karaokeonline.data.model.other.User
import com.example.hoavot.karaokeonline.ui.extensions.enableHighLightWhenClicked
import com.example.hoavot.karaokeonline.ui.feed.FeedAdapter
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
    internal var feedAdapter = FeedAdapter(feeds,user)

    override fun createView(ui: AnkoContext<ProfileFragment>): View {
        return with(ui) {
            relativeLayout {
                lparams(matchParent, matchParent)
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

                username = textView("Mikasa") {
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
                    textColor = Color.DKGRAY
                }.lparams {
                    rightOf(R.id.profileFragmentTvUsername)
                    sameTop(R.id.profileFragmentTvUsername)
                    leftMargin = dip(10)
                    centerHorizontally()
                }

                countFeed = textView("23 Bài viết") {
                    id = R.id.profileFragmentTvCountFeed
                    textSize = px2dip(dimen(R.dimen.textSize13))
                    textColor = Color.BLUE
                    paintFlags = Paint.UNDERLINE_TEXT_FLAG
                }.lparams {
                    below(R.id.profileFragmentTvUsername)
                    topMargin = dip(10)
                    centerHorizontally()
                }

                recyclerView {
                    backgroundColor = ContextCompat.getColor(context, R.color.colorItemFeed)
                    layoutManager = LinearLayoutManager(context)
                    adapter = feedAdapter
                }.lparams(matchParent, matchParent) {
                    below(R.id.profileFragmentTvCountFeed)
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
            }
        }
    }
}
