package com.example.hoavot.karaokeonline.ui.profile

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.View
import com.example.hoavot.karaokeonline.R
import com.example.hoavot.karaokeonline.data.model.other.Record
import com.example.hoavot.karaokeonline.ui.extensions.expandleLayout
import com.example.hoavot.karaokeonline.ui.extensions.roundImage
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView

/**
 *
 * @author at-hoavo.
 */
class ProfileFragmentUI(records: MutableList<Record>) : AnkoComponent<ProfileFragment> {
    override fun createView(ui: AnkoContext<ProfileFragment>): View {
        return with(ui) {
            relativeLayout {
                lparams(matchParent, matchParent)
                view {
                    id = R.id.profileFragmentViewBackground
                    backgroundColor = ContextCompat.getColor(context, R.color.colorBackground)
                }.lparams(matchParent, dip(100))

                relativeLayout {
                    lparams(dip(40), dip(40)) {
                        sameBottom(R.id.profileFragmentViewBackground)
                        topMargin = dip(20)
                        centerHorizontally()
                    }

                    roundImage {
                        borderRadius = 20.toFloat()
                    }.lparams(matchParent, matchParent)

                    imageView(R.drawable.ic_camera_alt_blue_900_24dp) {
                    }.lparams {
                        centerInParent()
                    }
                }

                textView("Account") {
                    id = R.id.profileFragmentTvAccount
                }

                imageView(R.drawable.ic_keyboard_arrow_right_black_36dp) {

                }.lparams {
                    rightOf(id = R.id.profileFragmentTvAccount)
                    sameBottom(R.id.profileFragmentTvAccount)
                }

                imageView(R.drawable.ic_keyboard_arrow_down_black_36dp) {
                    visibility = View.GONE
                }.lparams {
                    rightOf(id = R.id.profileFragmentTvAccount)
                    sameBottom(R.id.profileFragmentTvAccount)
                }
                expandleLayout {
                    lparams(matchParent, dip(200)) {
                        below(R.id.profileFragmentTvAccount)
                    }
                    verticalLayout {
                        lparams(matchParent, wrapContent)
                        linearLayout {
                            editText {
                                backgroundResource = R.drawable.custom_edittext_search_video
                                isEnabled = false
                                leftPadding = dip(10)
                            }.lparams(matchParent, dip(15)) {
                                gravity = Gravity.CENTER_HORIZONTAL
                                horizontalMargin = dip(30)
                            }

                            textView("Edit") {
                                textSize = px2dip(dimen(R.dimen.feedCommentTextSize))
                                paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
                            }.lparams {
                                leftMargin = dip(10)
                            }
                        }

                        linearLayout {
                            lparams(matchParent, dip(15)) {
                                topMargin = dip(5)
                            }
                            editText {
                                backgroundResource = R.drawable.custom_edittext_search_video
                                isEnabled = false
                                leftPadding = dip(10)
                            }.lparams(matchParent, matchParent) {
                                gravity = Gravity.CENTER_HORIZONTAL
                                horizontalMargin = dip(30)
                            }

                            textView("Edit") {
                                textSize = px2dip(dimen(R.dimen.feedCommentTextSize))
                                paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
                            }.lparams {
                                leftMargin = dip(10)
                            }
                        }

                        linearLayout {
                            lparams(matchParent, dip(15)) {
                                topMargin = dip(5)
                            }
                            editText {
                                backgroundResource = R.drawable.custom_edittext_search_video
                                isEnabled = false
                                leftPadding = dip(10)
                            }.lparams(matchParent, matchParent) {
                                gravity = Gravity.CENTER_HORIZONTAL
                                horizontalMargin = dip(30)
                            }

                            textView("Edit") {
                                textSize = px2dip(dimen(R.dimen.feedCommentTextSize))
                                paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
                            }.lparams {
                                leftMargin = dip(10)
                            }
                        }

                        linearLayout {
                            lparams(matchParent, dip(15)) {
                                topMargin = dip(5)
                            }
                            editText {
                                backgroundResource = R.drawable.custom_edittext_search_video
                                isEnabled = false
                                leftPadding = dip(10)
                            }.lparams(matchParent, matchParent) {
                                gravity = Gravity.CENTER_HORIZONTAL
                                horizontalMargin = dip(30)
                            }

                            textView("Edit") {
                                textSize = px2dip(dimen(R.dimen.feedCommentTextSize))
                                paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
                            }.lparams {
                                leftMargin = dip(10)
                            }
                        }
                    }
                }

                textView("Records") {
                    id = R.id.profileFragmentTvRecord
                    textSize = px2dip(dimen(R.dimen.textSize15))
                    typeface = Typeface.DEFAULT_BOLD
                    textColor = Color.BLACK
                }.lparams {
                    topMargin = dip(15)
                }

                recyclerView {

                }.lparams(matchParent, matchParent) {
                    topMargin = dip(10)
                }
            }
        }
    }

}