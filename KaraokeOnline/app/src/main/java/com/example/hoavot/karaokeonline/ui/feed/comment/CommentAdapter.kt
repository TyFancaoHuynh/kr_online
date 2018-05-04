package com.example.hoavot.karaokeonline.ui.feed.comment

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.hoavot.karaokeonline.R
import com.example.hoavot.karaokeonline.data.model.other.Comment
import org.jetbrains.anko.AnkoContext

/**
 *
 * @author at-hoavo.
 */
class CommentAdapter(private val comments: MutableList<Comment>) : RecyclerView.Adapter<CommentAdapter.CommentHolder>() {
    override fun getItemCount() = comments.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentHolder {
        val ui = CommentUI()
        return CommentHolder(ui, ui.createView(AnkoContext.Companion.create(parent.context, parent, false)))
    }

    override fun onBindViewHolder(holder: CommentHolder?, position: Int) {
        holder?.onBind()
    }

    inner class CommentHolder(private val ui: CommentUI, private val item: View) : RecyclerView.ViewHolder(item) {
        private val option = RequestOptions()
                .centerCrop()
                .override(ui.avatar.width, ui.avatar.width)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE) // https://github.com/bumptech/glide/issues/319
                .placeholder(R.drawable.user)

        internal fun onBind() {
            Glide.with(item)
                    .load(comments[layoutPosition].avatarUser)
                    .apply(option)
                    .into(ui.avatar)
            ui.comment.text = comments[layoutPosition].comment
            ui.username.text = comments[layoutPosition].username
        }
    }
}
