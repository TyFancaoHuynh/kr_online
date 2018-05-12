package com.example.hoavot.karaokeonline.ui.playmusic.adapter

import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.example.hoavot.karaokeonline.R
import com.example.hoavot.karaokeonline.ui.playmusic.model.Song
import com.example.hoavot.karaokeonline.ui.playmusic.utils.showDate
import org.jetbrains.anko.AnkoContext

/**
 * Copyright © 2017 AsianTech inc.
 * Created by at-hoavo on 01/07/2017.
 */
class SongAdapter(private val mSongs: List<Song>, private val mContext: Context, val mOnChooseSongListener: OnChooseSongListener) : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {
    override fun onBindViewHolder(holder: SongViewHolder?, position: Int) {
        holder?.onBind(position)
    }

    private var mPositionCurrent: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val ui = SongHolderUI()
        return SongViewHolder(ui, ui.createView(AnkoContext.create(parent.context, parent, false)))
    }

    override fun getItemCount(): Int {
        return mSongs.size
    }

    fun setPosition(position: Int) {
        mPositionCurrent = position
    }

    /**
     * Create SongViewHolder
     */
    inner class SongViewHolder(val ui: SongHolderUI, private val mItemView: View) : RecyclerView.ViewHolder(mItemView) {

        init {
            ui.mItemView.setOnClickListener {
                mOnChooseSongListener.onSongUpdate(layoutPosition)
                mPositionCurrent = layoutPosition
            }
        }

        fun onBind(position: Int) {
            if (mPositionCurrent == position) {
                ui.mItemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorItemChoosePlay))
            } else {
                ui.mItemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorItemNoChoosePlay))
            }
            ui.nameRecord.setText(mSongs[position].name)
            ui.artist.setText(mSongs[position].artist)
            ui.timeRecord.text=mSongs[position].date?.toLong()?.showDate()
        }
    }
}

interface OnChooseSongListener {
    fun onSongUpdate(position: Int)
}
