package com.example.hoavot.karaokeonline.ui.feed.like

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.hoavot.karaokeonline.R
import com.example.hoavot.karaokeonline.data.model.other.User
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 *
 * @author at-hoavo
 */
class LikeAdapter(private val users: MutableList<User>) : RecyclerView.Adapter<LikeAdapter.LikeHolder>() {
    internal var onAvatarClick: (Int) -> Unit = {}
    override fun getItemCount() = users.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikeHolder {
        val ui = LikeUI()
        return LikeHolder(ui, ui.createView(AnkoContext.Companion.create(parent.context, parent, false)))
    }

    override fun onBindViewHolder(holder: LikeHolder?, position: Int) {
        holder?.onBind()
    }

    inner class LikeHolder(private val ui: LikeUI, private val item: View) : RecyclerView.ViewHolder(item) {

        init {
            ui.avatar.onClick {
                onAvatarClick(layoutPosition)
            }
        }

        private val option = RequestOptions()
                .centerCrop()
                .override(ui.avatar.width, ui.avatar.width)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE) // https://github.com/bumptech/glide/issues/319
                .placeholder(R.drawable.user_default)

        internal fun onBind() {
            Glide.with(item)
                    .load(users[layoutPosition].avatar)
                    .apply(option)
                    .into(ui.avatar)
            ui.username.text = users[layoutPosition].username
        }
    }

}
