package com.example.hoavot.karaokeonline.ui.play.record

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.example.hoavot.karaokeonline.R
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 *
 * @author at-hoavo.
 */
class RecordBottomSheetUI : AnkoComponent<RecordFragment> {
    internal lateinit var lnPlay: LinearLayout
    internal lateinit var lnPause: LinearLayout
    internal lateinit var lnStop: LinearLayout
    internal lateinit var lnRecordings: LinearLayout
    internal lateinit var timeRecord: TextView

    override fun createView(ui: AnkoContext<RecordFragment>): View = with(ui) {
        relativeLayout {
            lparams(matchParent, matchParent)

            relativeLayout {
                backgroundColor = Color.WHITE
                lparams(matchParent, matchParent) {
                    above(R.id.recordBottomSheet)
                }
                imageView(R.drawable.ic_fiber_manual_record_red_400_48dp) {
                }.lparams(dip(150), dip(150)) {
                    centerInParent()
                }

                timeRecord = textView("00:00") {
                    textSize = px2dip(dimen(R.dimen.textSize21))
                    textColor = Color.WHITE
                }.lparams {
                    centerInParent()
                }
            }

            linearLayout {
                id = R.id.recordBottomSheet
                backgroundColor = ContextCompat.getColor(context, R.color.colorBlackBold)
                linearLayout {
                    lnRecordings = linearLayout {
                        visibility = View.INVISIBLE
                        lparams(matchParent, matchParent)
                        id = R.id.recordBottomSheetPlay
                        backgroundResource = R.color.colorButton
                        imageView(R.drawable.ic_menu_white_24dp) {

                        }.lparams {
                            gravity = Gravity.CENTER_VERTICAL
                        }
                        textView("Recordings") {
                            textColor = Color.WHITE
                            leftPadding = dip(5)
                        }.lparams {
                            gravity = Gravity.CENTER_VERTICAL
                        }
                    }

                    lnStop = linearLayout {
                        lparams(matchParent, matchParent)
                        backgroundResource = R.color.colorButton
                        imageView(R.drawable.ic_stop_white_24dp).lparams {
                            gravity = Gravity.CENTER_VERTICAL
                        }
                        textView("Stop") {
                            textColor = Color.WHITE
                            leftPadding = dip(5)
                        }.lparams {
                            gravity = Gravity.CENTER_VERTICAL
                        }
                        onClick {
                            owner.eventStopClicked()
                        }
                    }

                }.lparams(dip(0), matchParent) {
                    weight = 1f
                }

                linearLayout {
                    lnPlay = linearLayout {
                        lparams(matchParent, matchParent)
                        id = R.id.recordBottomSheetPlay
                        backgroundResource = R.color.colorButton
                        imageView(R.drawable.ic_fiber_manual_record_red_500_24dp).lparams {
                            gravity = Gravity.CENTER_VERTICAL
                        }
                        textView("Start") {
                            textColor = Color.WHITE
                            leftPadding = dip(5)
                        }.lparams {
                            gravity = Gravity.CENTER_VERTICAL
                        }
                        onClick {
                            owner.eventPlayClicked()
                        }
                    }

                    lnPause = linearLayout {
                        lparams(matchParent, matchParent)
                        id = R.id.recordBottomSheetPlay
                        backgroundResource = R.color.colorButton
                        onClick {
                            owner.eventPauseClicked()
                        }
                        imageView(R.drawable.ic_pause_white_24dp).lparams {
                            gravity = Gravity.CENTER_VERTICAL
                        }
                        textView("Pause") {
                            textColor = Color.WHITE
                            leftPadding = dip(5)
                        }.lparams {
                            gravity = Gravity.CENTER_VERTICAL
                        }
                    }
                }.lparams(dip(0), matchParent) {
                    weight = 1f
                    leftMargin = dip(5)
                }
            }.lparams(matchParent, dip(60)) {
                alignParentBottom()
            }
        }
    }
}

