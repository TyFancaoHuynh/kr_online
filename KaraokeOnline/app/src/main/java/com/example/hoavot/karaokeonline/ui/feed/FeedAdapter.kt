package com.example.hoavot.karaokeonline.ui.feed

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.hoavot.karaokeonline.R
import com.example.hoavot.karaokeonline.data.model.other.Feed
import com.example.hoavot.karaokeonline.ui.base.Time
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.sdk25.coroutines.onClick


/**
 *
 * @author at-hoavo.
 */
class FeedAdapter(private val feeds: MutableList<Feed>, val isFromFeed: Boolean) : RecyclerView.Adapter<FeedAdapter.FeedHolder>() {
    internal var likeListener: (Int) -> Unit = {}
    internal var unLikeListener: (Int) -> Unit = {}
    internal var commentListener: (Int) -> Unit = { }
    internal var shareListener: (Int) -> Unit = {}
    internal var fileMusicListener: (Int) -> Unit = {}
    internal var updateFeedClickListener: (Int) -> Unit = {}
    internal var deleteFeedClickListener: (Int) -> Unit = {}
    internal var likeSmallListener: (Int) -> Unit = {}

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
                ui.like.isEnabled = false
            }
            ui.unlike.onClick {
                unLikeListener(layoutPosition)
                ui.unlike.isEnabled = false
            }
            ui.comment.onClick {
                commentListener(layoutPosition)
            }

            ui.share.onClick {
                shareListener(layoutPosition)
            }

            ui.fileMusic.onClick {
                fileMusicListener(layoutPosition)
            }

            ui.more.onClick {
                ui.optionArea.visibility = View.VISIBLE
            }

            ui.updateFeed.onClick {
                ui.optionArea.visibility = View.INVISIBLE
                updateFeedClickListener(layoutPosition)
            }

            ui.deleteFeed.onClick {
                ui.optionArea.visibility = View.INVISIBLE
                deleteFeedClickListener(layoutPosition)
            }
            ui.imgBGMusic.onClick {
                fileMusicListener(layoutPosition)
            }

            ui.rlParent.onClick {
                ui.optionArea.visibility = View.GONE
            }

            ui.likeSmall.onClick {
                likeSmallListener(layoutPosition)
            }
        }

        private val option = RequestOptions()
                .override(ui.avatar.width, ui.avatar.width)
                .placeholder(R.drawable.user_default)

        internal fun onBind() {
            if (feeds[layoutPosition].likeFlag == 1) {
                ui.like.visibility = View.INVISIBLE
                ui.unlike.visibility = View.VISIBLE
                ui.unlike.isEnabled = true
            } else {
                ui.like.visibility = View.VISIBLE
                ui.unlike.visibility = View.INVISIBLE
                ui.like.isEnabled = true
            }

            ui.share.visibility = View.GONE
            if (!isFromFeed) {
                ui.more.visibility = View.VISIBLE
            }
            Glide.with(item)
                    .load(feeds[layoutPosition].avatar)
                    .apply(option)
                    .into(ui.avatar)
            if (feeds[layoutPosition].imageFile != null) {
                Glide.with(item)
                        .load(feeds[layoutPosition].imageFile)
                        .into(ui.imgBGMusic)
            }
            ui.userName.text = feeds[layoutPosition].username
            ui.countLike.text = feeds[layoutPosition].likeCount.toInt().toString()
            ui.countComment.text = feeds[layoutPosition].commentCount.toInt().toString().plus(" comment")
            ui.captionArea.text = feeds[layoutPosition].caption
            if (feeds[layoutPosition].fileMusic?.isNotBlank()!!) {
                ui.fileMusic.visibility = View.VISIBLE
                ui.fileMusic.text = feeds[layoutPosition].fileMusicUserWrite
            }
            ui.time.text = Time.parseDay(feeds[layoutPosition].time)
        }
    }
}
