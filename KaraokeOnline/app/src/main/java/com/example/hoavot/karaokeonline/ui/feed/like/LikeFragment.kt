package com.example.hoavot.karaokeonline.ui.feed.like

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hoavot.karaokeonline.data.model.other.User
import com.example.hoavot.karaokeonline.data.source.KaraRepository
import com.example.hoavot.karaokeonline.ui.extensions.observeOnUiThread
import com.example.hoavot.karaokeonline.ui.profile.ProfileAcivity
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.support.v4.startActivity

/**
 *
 * @author at-hoavo
 */
class LikeFragment : BottomSheetDialogFragment() {
    internal var feedId = -1
    private val users = mutableListOf<User>()
    private lateinit var viewModel: LikeViewModel
    private lateinit var ui: LikeFragmentUI

    companion object {
        const val KEY_FEED_ID = "KEY_FEED_ID"
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = LikeViewModel(KaraRepository())
        ui = LikeFragmentUI(users)
        return ui.createView(AnkoContext.Companion.create(context, this, false))
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ui.likeAdapter.onAvatarClick = this::handleAvatarItemClicked
        viewModel.getListUserLike(feedId)
                .observeOnUiThread()
                .subscribe({
                    users.clear()
                    users.addAll(it.users)
                    ui.likeAdapter.notifyDataSetChanged()
                    ui.countLike.text = it.users.size.toString().plus(" likes")
                }, {}
                )
    }

    private fun handleAvatarItemClicked(position: Int) {
        this.dismiss()
        startActivity<ProfileAcivity>(ProfileAcivity.KEY_USER_ID to users[position].id)
    }
}
