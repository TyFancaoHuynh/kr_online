package com.example.hoavot.karaokeonline.ui.search

import android.view.View
import android.view.ViewGroup
import android.view.ViewManager
import android.widget.ImageView
import android.widget.TextView
import com.example.hoavot.karaokeonline.R
import com.example.hoavot.karaokeonline.ui.custom.CustomTextviewChannel
import org.jetbrains.anko.*
import org.jetbrains.anko.custom.ankoView

/**
 *  Copyright © 2017 AsianTech inc.
 *  Created by at-hoavo on 15/12/2017.
 */
class ChannelViewHolderUI(private val colorText: Int) : AnkoComponent<ViewGroup> {
    internal lateinit var thumnailChannel: ImageView
    internal lateinit var tvNameChannel: TextView
    internal lateinit var tvPersonRegister: TextView
    internal lateinit var tvQuantityVideoChannel: TextView
    override fun createView(ui: AnkoContext<ViewGroup>): View = with(ui) {
        linearLayout {
            lparams(matchParent, dip(100)) {
                verticalMargin = dip(5)
            }
            thumnailChannel = imageView {}.lparams(dip(0), matchParent) {
                weight = 1f
            }
            relativeLayout {
                tvNameChannel = textView {
                    id = R.id.search_video_adapter_tv_name_channel
                    textSize = px2dip(dimen(R.dimen.textSize15))
                }
                customTextViewChannel {
                    id = R.id.search_video_adapter_tv_channel
                    text = "KENH"
                    textSize = px2dip(dimen(R.dimen.textSize13))
                }.lparams {
                    below(R.id.search_video_adapter_tv_name_channel)
                    sameLeft(R.id.search_video_adapter_tv_name_channel)
                }

                tvPersonRegister = textView {
                    id = R.id.search_video_adapter_tv_person_register_channel
                }.lparams {
                    below(R.id.search_video_adapter_tv_name_channel)
                    rightOf(R.id.search_video_adapter_tv_channel)
                }

                tvQuantityVideoChannel = textView {

                }.lparams {
                    rightOf(R.id.search_video_adapter_tv_person_register_channel)
                }
            }.lparams(dip(0), matchParent) {
                weight = 1f
                topMargin = dip(20)
            }
        }
    }

    inline fun ViewManager.customTextViewChannel(init: CustomTextviewChannel.() -> Unit): CustomTextviewChannel {
        return ankoView({ CustomTextviewChannel(it) }, theme = 0, init = init)
    }
}
