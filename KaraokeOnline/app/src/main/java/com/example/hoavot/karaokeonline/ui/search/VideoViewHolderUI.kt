package com.example.hoavot.karaokeonline.ui.search

import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.hoavot.karaokeonline.R
import org.jetbrains.anko.*

/**
 *  Copyright © 2017 AsianTech inc.
 *  Created by at-hoavo on 15/12/2017.
 */
class VideoViewHolderUI(private val colorText: Int) : AnkoComponent<ViewGroup> {
    internal lateinit var thumnailVideo: ImageView
    internal lateinit var tvDurationVideo: TextView
    internal lateinit var tvQuantityVideo: TextView
    internal lateinit var tvNameVideo: TextView
    internal lateinit var tvChannelVideo: TextView
    internal lateinit var tvViewCount: TextView
    internal lateinit var tvDayPublish: TextView

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun createView(ui: AnkoContext<ViewGroup>): View = with(ui) {
        linearLayout {
            lparams(matchParent, dip(100))

            relativeLayout {
                thumnailVideo = imageView {
                    id = R.id.search_video_adapter_img_video
                }.lparams(matchParent, matchParent) {
                    leftMargin = dip(10)
                    rightMargin = dip(10)
                    bottomMargin = dip(10)
                }

                tvDurationVideo = textView {
                    id = R.id.search_video_adapter_tv_duratiion_time_video
                    textSize = px2dip(35)
                    backgroundColor = ContextCompat.getColor(context, R.color.colorBlackTransparentDuration)
                    textColor = Color.WHITE
                }.lparams {
                    sameBottom(R.id.search_video_adapter_img_video)
                    sameRight(R.id.search_video_adapter_img_video)
                    rightMargin = dip(5)
                    bottomMargin = dip(5)
                }

                tvQuantityVideo = textView {
                    id = R.id.search_video_adapter_tv_quantity_video
                    textSize = px2dip(65)
                    gravity=Gravity.CENTER_VERTICAL
                    textAlignment = View.TEXT_ALIGNMENT_CENTER
                    textColor = Color.WHITE
                    backgroundColor = ContextCompat.getColor(context, R.color.colorBlackTransparent)
                    visibility = View.INVISIBLE
                }.lparams(dip(100), matchParent) {
                    sameRight(R.id.search_video_adapter_img_video)
                    bottomMargin = dip(10)
                }
            }.lparams(dip(0), matchParent) {
                weight = 1f
            }

            verticalLayout {
                lparams(dip(0), matchParent) {
                    rightPadding = dip(10)
                    topMargin = dip(5)
                    weight = 1f
                }

                tvNameVideo = textView {
                    id = R.id.search_video_adapter_name_video
                    textColor = colorText
                    typeface = Typeface.DEFAULT_BOLD
                    textSize = px2dip(30)
                    maxLines = 3
                    ellipsize = TextUtils.TruncateAt.END

                }.lparams {
                    topMargin = dip(3)
                }

                tvChannelVideo = textView {
                    id = R.id.search_video_adapter_channel_video
                    textSize = px2dip(25)
                    textColor = colorText
                }

                tvViewCount = textView {
                    id = R.id.search_video_adapter_quantity_view_video
                    textSize = px2dip(20)
                    textColor = colorText
                }

                tvDayPublish = textView {
                    id = R.id.search_video_adapter_tv_day_publish_video
                    textSize = px2dip(25)
                    textColor = colorText
                }

            }
        }
    }
}
