package com.example.hoavot.karaokeonline.ui.records

import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.hoavot.karaokeonline.R
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 *
 * @author at-hoavo.
 */
class RecordUI : AnkoComponent<ViewGroup> {
    internal lateinit var nameRecord: TextView
    internal lateinit var timeRecord: TextView
    internal lateinit var dateRecord: TextView
    internal lateinit var shareAudio: RelativeLayout

    override fun createView(ui: AnkoContext<ViewGroup>): View = with(ui) {
        relativeLayout {
            backgroundResource = R.drawable.custom_edittext_search_video
            lparams(matchParent, dip(80)) {
                horizontalMargin = dip(10)
                topMargin = dip(5)
            }

            nameRecord = textView {
                id = R.id.recordUIName
                textSize = px2dip(dimen(R.dimen.textSize15))
            }.lparams {
                topMargin = dip(5)
            }

            timeRecord = textView {
                id = R.id.recordUITime
                textSize = px2dip(dimen(R.dimen.feedCommentTextSize))
            }.lparams {
                below(R.id.recordUIName)
            }

            dateRecord = textView {
                id = R.id.recordUIDate
                textSize = px2dip(dimen(R.dimen.feedCaptionTextSize))
            }.lparams {
                below(R.id.recordUITime)
            }

            shareAudio = relativeLayout {
                lparams(wrapContent, dip(81)) {
                    sameTop(R.id.recordUIName)
                    rightOf(R.id.recordUIName)
                    rightMargin = dip(20)
                }
                imageView(R.drawable.ic_share_audio) {
                }.lparams {
                    centerInParent()
                }
                onClick {

                }
            }
        }
    }

}