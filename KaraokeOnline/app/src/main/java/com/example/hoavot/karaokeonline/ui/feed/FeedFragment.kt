package com.example.hoavot.karaokeonline.ui.feed

import android.app.ProgressDialog
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hoavot.karaokeonline.data.model.nomal.FeedEvent
import com.example.hoavot.karaokeonline.data.model.other.Feed
import com.example.hoavot.karaokeonline.ui.base.BaseFragment
import com.example.hoavot.karaokeonline.ui.extensions.RxBus
import com.example.hoavot.karaokeonline.ui.extensions.observeOnUiThread
import com.example.hoavot.karaokeonline.ui.feed.caption.CaptionFragment
import com.example.hoavot.karaokeonline.ui.feed.comment.CommentFragment
import com.example.hoavot.karaokeonline.ui.feed.comment.CommentLayoutUI
import io.reactivex.Notification
import org.jetbrains.anko.AnkoContext

/**
 *
 * @author at-hoavo.
 */
class FeedFragment : BaseFragment() {
    private lateinit var ui: FeedFragmentUI
    private var feeds = mutableListOf<Feed>()
    private lateinit var viewModel: FeedViewModel
    private lateinit var progressDialog: ProgressDialog
    private lateinit var bottomSheetCommentUI: CommentLayoutUI
    private lateinit var bottomSheetDialog: BottomSheetDialog

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        ui = FeedFragmentUI(feeds)
        return ui.createView(AnkoContext.Companion.create(context, this))
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initProgressDialog()
        ui.feedsAdapter.likeListener = this::eventWhenLikeclicked
        ui.feedsAdapter.unLikeListener = this::eventWhenUnLikeclicked
        ui.feedsAdapter.commentListener = this::eventWhenCommentclicked
        viewModel = FeedViewModel(feeds)
        RxBus.listen(FeedEvent::class.java)
                .observeOnUiThread()
                .subscribe(this::handleWhenUpdateComment)
    }

    override fun onBindViewModel() {
        addDisposables(
                viewModel.feedsObserverable
                        .observeOnUiThread()
                        .subscribe(this::handleGetFeedsSuccess),
                viewModel.progressDilogObserverable
                        .observeOnUiThread()
                        .subscribe(this::handleShowProgressDialog),
                viewModel.feedsCommentObserverable
                        .observeOnUiThread()
                        .subscribe(this::handleAddCommentSuccess)
        )
    }

    internal fun eventOnAddCaptionClicked() {
        childFragmentManager.beginTransaction().add(CaptionFragment.newInstance(), "ADD CAPTION").commit()
    }

    private fun handleGetFeedsSuccess(notification: Notification<DiffUtil.DiffResult>) {
        if (notification.isOnNext) {
            notification.value?.dispatchUpdatesTo(ui.feedsAdapter)
        } else {
            // Todo: Handle later
        }
    }

    private fun handleShowProgressDialog(show: Boolean) {
        if (show) {
            progressDialog.show()
        } else {
            progressDialog.hide()
        }
    }

    private fun handleAddCommentSuccess(notification: Notification<DiffUtil.DiffResult>) {
        if (notification.isOnNext) {
            notification.value?.dispatchUpdatesTo(bottomSheetCommentUI.commentsAdapter)
        } else {
            // Todo: Handle later
        }
    }

    private fun eventWhenCommentclicked(position: Int) {
        val fragment = CommentFragment.newInstance(feeds[position], position)
        childFragmentManager.beginTransaction().add(fragment, "COMMENT").commit()
    }

    private fun eventWhenLikeclicked(position: Int) {
        viewModel.addLike(position)
    }

    private fun eventWhenUnLikeclicked(position: Int) {
        viewModel.removeLike(position)
    }

    private fun initProgressDialog() {
        progressDialog = ProgressDialog(context)
        progressDialog.setCancelable(false)
    }

    private fun handleWhenUpdateComment(event: FeedEvent) {
        feeds[event.position].commentCount = event.comments.size.toLong()
        feeds[event.position].comments.clear()
        feeds[event.position].comments.addAll(event.comments)
    }
}
