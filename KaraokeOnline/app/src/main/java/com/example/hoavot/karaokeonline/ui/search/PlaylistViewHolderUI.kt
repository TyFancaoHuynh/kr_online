package com.example.hoavot.karaokeonline.ui.search

import android.graphics.Color
import android.graphics.Typeface
import android.support.v4.content.ContextCompat
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
class PlaylistViewHolderUI : AnkoComponent<ViewGroup> {
    internal lateinit var thumnailPlaylist: ImageView
    internal lateinit var tvQuantityVideo: TextView
    internal lateinit var tvNamePlaylist: TextView
    internal lateinit var tvChannelPlaylist: TextView
    internal lateinit var tvViewCount: TextView
    internal lateinit var tvDayPublish: TextView

    override fun createView(ui: AnkoContext<ViewGroup>): View = with(ui) {
        linearLayout {
            lparams(matchParent, dip(200))

            relativeLayout {
                thumnailPlaylist = imageView {
                    id = R.id.search_video_adapter_img_video
                }.lparams(matchParent, matchParent)

                tvQuantityVideo = textView {
                    id = R.id.search_video_adapter_tv_quantity_video
                    textColor = Color.WHITE
                    backgroundColor = ContextCompat.getColor(context, R.color.colorBlackTransparent)
                    gravity = Gravity.CENTER
                }.lparams(dip(100), matchParent) {
                    sameRight(R.id.search_video_adapter_img_video)
                }
            }.lparams(dip(0), matchParent) {
                weight = 1f
            }

            verticalLayout {
                tvNamePlaylist = textView {
                    id = R.id.search_video_adapter_name_video
                    textColor = Color.BLACK
                    typeface = Typeface.DEFAULT_BOLD
                    textSize = px2dip(15)
                }

                tvChannelPlaylist = textView {
                    id = R.id.search_video_adapter_channel_video
                    textSize = px2dip(10)
                }

                tvViewCount = textView {
                    id = R.id.search_video_adapter_quantity_view_video
                    textSize = px2dip(10)
                }

                tvDayPublish = textView {
                    id = R.id.search_video_adapter_tv_day_publish_video
                    textSize = px2dip(10)
                }

            }.lparams(dip(0), matchParent) {
                weight = 1f
            }
        }
    }
}
