package com.example.hoavot.karaokeonline.ui.feed

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.hoavot.karaokeonline.R
import com.example.hoavot.karaokeonline.data.model.other.Feed
import com.example.hoavot.karaokeonline.ui.extensions.underlineText
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.sdk25.coroutines.onClick


/**
 *
 * @author at-hoavo.
 */
class FeedAdapter(private val feeds: MutableList<Feed>, private var updateCommentsAdapter: (DiffUtil.DiffResult) -> Unit) : RecyclerView.Adapter<FeedAdapter.FeedHolder>() {
    internal var likeListener: (Int) -> Unit = {}
    internal var unLikeListener: (Int) -> Unit = {}
    internal var commentListener: (Int, String) -> Unit = { _, _ -> }

    override fun onBindViewHolder(holder: FeedHolder?, position: Int) {
        holder?.onBind()
    }

    override fun getItemCount() = feeds.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedHolder {
        val ui = FeedHolderUI()
        return FeedHolder(ui, ui.createView(AnkoContext.Companion.create(parent.context, parent, false)))
    }

    inner class FeedHolder(private val ui: FeedHolderUI, private val item: View) : RecyclerView.ViewHolder(item) {
        init {
            ui.like.onClick {
                likeListener(layoutPosition)
            }
            ui.unlike.onClick {
                unLikeListener(layoutPosition)
            }
            ui.countComment.onClick {

            }
            updateCommentsAdapter = {
                it.dispatchUpdatesTo(ui.commentsAdapter)
            }
        }

        private val option = RequestOptions()
                .centerCrop()
                .override(ui.avatar.width, ui.avatar.width)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE) // https://github.com/bumptech/glide/issues/319
                .placeholder(R.drawable.bg_item_place_holder)

        internal fun onBind() {
            if (feeds[layoutPosition].likeFlag) {
                ui.like.visibility = View.INVISIBLE
                ui.unlike.visibility = View.VISIBLE
            } else {
                ui.like.visibility = View.VISIBLE
                ui.unlike.visibility = View.INVISIBLE
            }
            Glide.with(item)
                    .asBitmap()
                    .load(feeds[layoutPosition].avatar)
                    .apply(option)
                    .transition(BitmapTransitionOptions.withCrossFade())
                    .into(ui.avatar)
            ui.userName.text = feeds[layoutPosition].userName
            ui.countLike.text = feeds[layoutPosition].likeCount.toInt().toString()
            ui.countComment.text = feeds[layoutPosition].commentCount.toInt().toString().plus(" comment").underlineText {}
            ui.captionArea.text = feeds[layoutPosition].caption
        }
    }
}
