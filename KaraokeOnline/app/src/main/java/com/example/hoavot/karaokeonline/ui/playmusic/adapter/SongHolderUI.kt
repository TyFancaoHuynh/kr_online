package com.example.hoavot.karaokeonline.ui.playmusic.adapter

import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.hoavot.karaokeonline.R
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 *
 * @author at-hoavo
 */
class SongHolderUI : AnkoComponent<ViewGroup> {
    internal lateinit var nameRecord: TextView
    internal lateinit var timeRecord: TextView
    internal lateinit var artist: TextView
    internal lateinit var shareAudio: RelativeLayout
    internal lateinit var mItemView: RelativeLayout
    override fun createView(ui: AnkoContext<ViewGroup>): View
            = ui.apply {
        mItemView = relativeLayout {
            backgroundResource = R.drawable.custom_edittext_search_video
            lparams(matchParent, dip(80)) {
                horizontalMargin = dip(10)
                topMargin = dip(5)
            }

            nameRecord = textView {
                id = R.id.recordUIName
                typeface = Typeface.DEFAULT_BOLD
                textSize = px2dip(dimen(R.dimen.textSize15))
            }.lparams {
                topMargin = dip(5)
                leftMargin = dip(5)
            }

            artist = textView {
                id = R.id.recordUIDate
                textSize = px2dip(dimen(R.dimen.textSize13))
            }.lparams {
                below(R.id.recordUIName)
                leftMargin = dip(5)
            }

            timeRecord = textView {
                id = R.id.recordUITime
                textSize = px2dip(dimen(R.dimen.textSize12))
            }.lparams {
                topMargin=dip(5)
                leftMargin = dip(5)
                below(R.id.recordUIDate)
            }

            shareAudio = relativeLayout {
                lparams(wrapContent, dip(81)) {
                    sameTop(R.id.recordUIName)
                    rightOf(R.id.recordUIName)
                    rightMargin = dip(20)
                }
                visibility = View.GONE
                imageView(R.drawable.ic_share_audio) {
                }.lparams {
                    centerInParent()
                }
                onClick {

                }
            }
        }
    }.view
}