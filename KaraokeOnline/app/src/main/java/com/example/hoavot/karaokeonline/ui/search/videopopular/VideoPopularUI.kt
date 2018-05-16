package com.example.hoavot.karaokeonline.ui.search.videopopular

import android.graphics.Color
import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.hoavot.karaokeonline.R
import com.example.hoavot.karaokeonline.ui.extensions.enableHighLightWhenClicked
import org.jetbrains.anko.*

/**
 *
 * @author at-hoavo
 */
class VideoPopularUI : AnkoComponent<ViewGroup> {
    internal lateinit var image: ImageView
    internal lateinit var name: TextView
    internal lateinit var channel: TextView
    internal lateinit var countView: TextView
    internal lateinit var tvDurationVideo: TextView
    internal lateinit var date: TextView

    override fun createView(ui: AnkoContext<ViewGroup>): View {
        return ui.apply {
            verticalLayout {
                lparams(matchParent, dip(300)) {
                    bottomMargin = dip(5)
                }
                relativeLayout {
                    image = imageView {
                        id = R.id.popular_video_adapter_tv_duratiion_time_video
                        scaleType = ImageView.ScaleType.CENTER_CROP
                    }.lparams(matchParent, matchParent)

                    tvDurationVideo = textView {
                        id = R.id.search_video_adapter_tv_duratiion_time_video
                        textSize = px2dip(35)
                        backgroundColor = ContextCompat.getColor(context, R.color.colorBlackTransparentDuration)
                        textColor = Color.WHITE
                    }.lparams {
                        sameBottom(R.id.popular_video_adapter_tv_duratiion_time_video)
                        sameRight(R.id.popular_video_adapter_tv_duratiion_time_video)
                        rightMargin = dip(5)
                        bottomMargin = dip(5)
                    }
                }.lparams(matchParent, dip(200))


                name = textView {
                    typeface = Typeface.DEFAULT_BOLD
                    textColor = Color.BLACK
                    textSize = px2dip(dimen(R.dimen.textSize16))
                }.lparams(matchParent, wrapContent) {
                    topMargin = dip(10)
                    leftMargin = dip(5)
                }

                channel = textView {
                    textSize = px2dip(dimen(R.dimen.textSize14))
                    typeface = Typeface.DEFAULT_BOLD
                }.lparams {
                    topMargin = dip(5)
                    leftMargin = dip(5)
                }
                linearLayout {
                    countView = textView {
                        textSize = px2dip(dimen(R.dimen.textSize13))
                        textColor = ContextCompat.getColor(context, R.color.colorGrayLight)
                    }
                    date = textView {
                        textSize = px2dip(dimen(R.dimen.textSize13))
                        textColor = ContextCompat.getColor(context, R.color.colorGrayLight)
                    }.lparams {
                        leftMargin = dip(10)
                    }
                }.lparams(matchParent, wrapContent) {
                    topMargin = dip(3)
                    leftMargin = dip(5)
                }
            }
        }.view
    }

}