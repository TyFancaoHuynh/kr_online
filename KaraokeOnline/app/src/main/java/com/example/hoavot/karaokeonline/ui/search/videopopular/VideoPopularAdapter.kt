package com.example.hoavot.karaokeonline.ui.utils

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.hoavot.karaokeonline.data.model.nomal.Item
import com.example.hoavot.karaokeonline.ui.extensions.getDayPublish
import com.example.hoavot.karaokeonline.ui.extensions.toDurationTimeVideo
import com.example.hoavot.karaokeonline.ui.extensions.toViewCountVideo
import com.example.hoavot.karaokeonline.ui.search.videopopular.VideoPopularUI
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 *  Copyright © 2017 AsianTech inc.
 *  Created by hoavot on 14/12/2017.
 */
class VideoPopularAdapter(private val context: Context, private val videos: MutableList<Item>) : RecyclerView.Adapter<VideoPopularAdapter.VideoPopularHolder>() {

    internal var onItemClick: (Item) -> Unit = { }


    override fun getItemCount(): Int = videos.size

    override fun onBindViewHolder(holder: VideoPopularHolder?, position: Int) {
        holder?.onBindVideo()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoPopularHolder {
        val videoHolder = VideoPopularUI()
        return VideoPopularHolder(videoHolder,
                videoHolder.createView(AnkoContext.Companion.create(parent.context, parent, false)))
    }

    inner class VideoPopularHolder(private val ui: VideoPopularUI, item: View) : RecyclerView.ViewHolder(item) {
        init {
            item.onClick {
                onItemClick(videos[adapterPosition])
            }
        }

        fun onBindVideo() {
            Glide.with(context)
                    .load(videos[adapterPosition].snippet.thumbnails.medium.url)
                    .into(ui.image)
            ui.name.text = videos[adapterPosition].snippet.title
            ui.channel.text = videos[adapterPosition].snippet.channelTitle
            ui.countView.text = videos[adapterPosition].statistics?.viewCount?.toViewCountVideo()
            ui.date.text = videos[adapterPosition].snippet.publishedAt.getDayPublish()
            ui.tvDurationVideo.text = videos[adapterPosition].contentDetails?.duration?.toDurationTimeVideo()
        }
    }
}
