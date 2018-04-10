package com.example.hoavot.karaokeonline.ui.profile

import android.graphics.Color
import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.hoavot.karaokeonline.R
import com.example.hoavot.karaokeonline.data.model.other.Feed
import com.example.hoavot.karaokeonline.ui.extensions.expandleLayout
import com.example.hoavot.karaokeonline.ui.extensions.roundImage
import com.example.hoavot.karaokeonline.ui.feed.FeedAdapter
import com.github.siyamed.shapeimageview.CircularImageView
import net.cachapa.expandablelayout.ExpandableLayout
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 *
 * @author at-hoavo.
 */
class ProfileFragmentUI(private val feeds: MutableList<Feed>) : AnkoComponent<ProfileFragment> {
    internal lateinit var avatar: CircularImageView
    internal lateinit var pickImage: ImageView
    internal lateinit var arrowRight: ImageView
    internal lateinit var arrowDown: ImageView
    internal lateinit var nameUser: TextView
    internal lateinit var passWord: TextView
    internal lateinit var email: TextView
    internal lateinit var save: ImageView
    internal lateinit var edit: ImageView
    internal lateinit var expandlelayout: ExpandableLayout

    override fun createView(ui: AnkoContext<ProfileFragment>): View {
        return with(ui) {
            relativeLayout {
                lparams(matchParent, matchParent)
                view {
                    id = R.id.profileFragmentViewBackground
                    backgroundColor = ContextCompat.getColor(context, R.color.colorBackground)
                }.lparams(matchParent, dip(100))

                relativeLayout {
                    lparams(dip(80), dip(80)) {
                        sameBottom(R.id.profileFragmentViewBackground)
                        topMargin = dip(20)
                        centerHorizontally()
                    }

                    avatar = roundImage {
                        backgroundColor = Color.BLACK
                        backgroundResource = R.drawable.ic_home_pink
                    }.lparams(matchParent, matchParent)

                    pickImage = imageView(R.drawable.ic_camera_alt_blue_900_24dp) {
                    }.lparams {
                        centerInParent()
                    }
                }

                textView("Account") {
                    id = R.id.profileFragmentTvAccount
                    typeface = Typeface.DEFAULT_BOLD
                    textSize = px2dip(dimen(R.dimen.textSize15))
                    textColor = Color.BLACK
                }.lparams {
                    topMargin = dip(20)
                    leftMargin = dip(5)
                    below(R.id.profileFragmentViewBackground)
                }

                arrowRight = imageView(R.drawable.ic_keyboard_arrow_right_black_36dp) {
                    onClick {
                        owner.handleWhenArrowRightClick()
                    }
                }.lparams {
                    topMargin = dip(15)
                    below(R.id.profileFragmentViewBackground)
                    rightOf(id = R.id.profileFragmentTvAccount)
                }

                arrowDown = imageView(R.drawable.ic_keyboard_arrow_down_black_36dp) {
                    visibility = View.GONE
                    onClick {
                        owner.handleWhenArrowDownClick()
                    }
                }.lparams {
                    topMargin = dip(15)
                    rightOf(id = R.id.profileFragmentTvAccount)
                    below(R.id.profileFragmentViewBackground)
                }
                expandlelayout = expandleLayout {
                    id = R.id.profileFragmentExpandleLayout
                    lparams(matchParent, dip(270)) {
                        below(R.id.profileFragmentTvAccount)
                    }
                    isExpanded = false
                    verticalLayout {
                        lparams(matchParent, wrapContent) {
                            topMargin = dip(10)
                        }

                        nameUser = editText {
                            backgroundResource = R.drawable.custom_edittext_search_video
                            isEnabled = false
                            leftPadding = dip(10)
                        }.lparams(dip(250), dip(30)) {
                            topMargin = dip(10)
                            horizontalMargin = dip(30)
                        }

                        passWord = editText {
                            backgroundResource = R.drawable.custom_edittext_search_video
                            isEnabled = false
                            leftPadding = dip(10)
                        }.lparams(dip(250), dip(30)) {
                            topMargin = dip(10)
                            horizontalMargin = dip(30)
                        }

                        email = editText {
                            backgroundResource = R.drawable.custom_edittext_search_video
                            isEnabled = false
                            leftPadding = dip(10)
                        }.lparams(dip(250), dip(30)) {
                            topMargin = dip(10)
                            horizontalMargin = dip(30)
                        }

                        editText {
                            backgroundResource = R.drawable.custom_edittext_search_video
                            isEnabled = false
                            leftPadding = dip(10)
                        }.lparams(dip(250), dip(30)) {
                            topMargin = dip(10)
                            horizontalMargin = dip(30)
                        }

                        linearLayout {
                            lparams {
                                topMargin = dip(10)
                            }

                            save = imageView(R.drawable.ic_tick).lparams(dip(35), dip(35))
                            {
                                leftMargin = dip(10)
                            }

                            edit = imageView(R.drawable.ic_edit) {
                            }.lparams(dip(35), dip(35)) {
                                leftMargin = dip(10)
                            }
                        }
                    }
                }

                view {
                    backgroundColor = ContextCompat.getColor(context, R.color.colorGrayLight)
                }.lparams(matchParent, dip(1))

                textView("Feeds") {
                    id = R.id.profileFragmentTvRecord
                    textSize = px2dip(dimen(R.dimen.textSize15))
                    typeface = Typeface.DEFAULT_BOLD
                    textColor = Color.BLACK
                }.lparams {
                    below(R.id.profileFragmentExpandleLayout)
                    topMargin = dip(5)
                    leftMargin = dip(5)
                }

                recyclerView {
                    layoutManager = LinearLayoutManager(context)
                    adapter = FeedAdapter(feeds, {})
                }.lparams(matchParent, matchParent) {
                    below(R.id.profileFragmentTvRecord)
                    topMargin = dip(10)
                }

            }
        }
    }

}
