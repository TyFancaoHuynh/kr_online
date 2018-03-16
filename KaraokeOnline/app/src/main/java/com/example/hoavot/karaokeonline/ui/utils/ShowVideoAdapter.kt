package com.example.hoavot.karaokeonline.ui.utils

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.hoavot.karaokeonline.R
import com.example.hoavot.karaokeonline.data.model.nomal.Item
import com.example.hoavot.karaokeonline.ui.extensions.getDayPublish
import com.example.hoavot.karaokeonline.ui.extensions.toDurationTimeVideo
import com.example.hoavot.karaokeonline.ui.extensions.toViewCountVideo
import com.example.hoavot.karaokeonline.ui.search.ChannelViewHolderUI
import com.example.hoavot.karaokeonline.ui.search.VideoViewHolderUI
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 *  Copyright © 2017 AsianTech inc.
 *  Created by hoavot on 14/12/2017.
 */
class ShowVideoAdapter(private val context: Context, private val videos: MutableList<Item>, private val colorText: Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_VIDEO = 0
        const val TYPE_CHANNEL = 1
        const val TYPE_PLAY_LIST = 2
    }

    internal var onItemClick: (Item, Int) -> Unit = { _, _ -> }


    override fun getItemCount(): Int = videos.size

    override fun getItemViewType(position: Int): Int {

        if (!TextUtils.isEmpty(videos[position].video.id.channelId)) {
            return TYPE_CHANNEL
        } else if (!TextUtils.isEmpty(videos[position].video.id.playlistId)) {
            return TYPE_PLAY_LIST
        } else {
            return TYPE_VIDEO
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        when (holder) {
            is VideoHolder -> {
                if (!TextUtils.isEmpty(videos[position].video.id.playlistId)) {
                    holder.onBindVideo(TYPE_PLAY_LIST)
                } else {
                    holder.onBindVideo(TYPE_VIDEO)
                }
            }
            is ChannelHolder -> holder.onBindChannel()
        }
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            TYPE_CHANNEL -> {
                val channelHolder = ChannelViewHolderUI(colorText)
                return ChannelHolder(channelHolder,
                        channelHolder.createView(AnkoContext.Companion.create(parent.context, parent, false)))
            }
            else -> {
                val videoHolder = VideoViewHolderUI(colorText)
                return VideoHolder(videoHolder,
                        videoHolder.createView(AnkoContext.Companion.create(parent.context, parent, false)))
            }
        }
    }

    inner class VideoHolder(private val ui: VideoViewHolderUI, item: View) : RecyclerView.ViewHolder(item) {
        init {
            item.onClick {
                if (!TextUtils.isEmpty(videos[adapterPosition].video.id.playlistId)) {
                    onItemClick(videos[adapterPosition], TYPE_PLAY_LIST)
                } else {
                    onItemClick(videos[adapterPosition], TYPE_VIDEO)
                }
            }
        }

        fun onBindVideo(type: Int) {
            when (type) {
                TYPE_VIDEO -> {

                    ui.tvDurationVideo.text = videos[adapterPosition].contentDetails?.duration?.toDurationTimeVideo()
                    ui.tvViewCount.text = videos[adapterPosition].statistics?.viewCount?.toViewCountVideo()
                    ui.tvChannelVideo.text = videos[adapterPosition].snippet.channelTitle
                    ui.tvDayPublish.text = videos[adapterPosition].snippet.publishedAt.getDayPublish()
                }
                TYPE_PLAY_LIST -> {
                    ui.tvChannelVideo.text = "Youtube"
                    ui.tvQuantityVideo.visibility = View.VISIBLE
                    ui.tvQuantityVideo.text = context.getString(R.string.video_count, videos[adapterPosition].snippet.totalPlaylist.toString())

                }
            }
            Glide.with(context)
                    .load(videos[adapterPosition].snippet.thumbnails.medium.url)
                    .into(ui.thumnailVideo)
            ui.tvNameVideo.text = videos[adapterPosition].snippet.title
        }
    }

    inner class ChannelHolder(private val ui: ChannelViewHolderUI, item: View) : RecyclerView.ViewHolder(item) {
        init {
            onItemClick(videos[layoutPosition], TYPE_CHANNEL)
        }

        fun onBindChannel() {
            Glide.with(context)
                    .load(videos[layoutPosition].snippet.thumbnails.medium.url)
                    .into(ui.thumnailChannel)
            ui.tvNameChannel.text = videos[layoutPosition].snippet.title
        }
    }
}
