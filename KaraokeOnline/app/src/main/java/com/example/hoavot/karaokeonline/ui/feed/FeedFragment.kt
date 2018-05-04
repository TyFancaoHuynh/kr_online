package com.example.hoavot.karaokeonline.ui.feed

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.util.DiffUtil
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hoavot.karaokeonline.data.model.other.*
import com.example.hoavot.karaokeonline.ui.base.BaseFragment
import com.example.hoavot.karaokeonline.ui.extensions.RxBus
import com.example.hoavot.karaokeonline.ui.extensions.observeOnUiThread
import com.example.hoavot.karaokeonline.ui.feed.caption.CaptionActivity
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
    private lateinit var bottomSheetDialogComment: CommentFragment
    private var isLikeFromCommentScreen = false

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        ui = FeedFragmentUI(feeds)
        return ui.createView(AnkoContext.Companion.create(context, this))
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initProgressDialog()
        initSortDialog()
        ui.feedsAdapter.likeListener = this::eventWhenLikeclicked
        ui.feedsAdapter.unLikeListener = this::eventWhenUnLikeclicked
        ui.feedsAdapter.commentListener = this::eventWhenCommentclicked
        viewModel = FeedViewModel(feeds)
        RxBus.listen(CommentEvent::class.java)
                .observeOnUiThread()
                .subscribe(this::handleWhenAddComment)
        RxBus.listen(LikeEvent::class.java)
                .observeOnUiThread()
                .subscribe(this::handleWhenUpdateLike)
        RxBus.listen(UnlikeEvent::class.java)
                .observeOnUiThread()
                .subscribe(this::handleWhenUpdateUnLike)
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
                        .subscribe(this::handleAddCommentSuccess),
                viewModel.commentObserverable
                        .observeOnUiThread()
                        .subscribe(this::handleUpdateCommentSuccess),
                viewModel.isLikeFromCommentScreenObserver
                        .observeOnUiThread()
                        .subscribe(this::handleUpdateLikeFromCommentcreenSuccess)
        )
    }

    internal fun eventOnAddCaptionClicked() {
        val intent = Intent(context, CaptionActivity::class.java)
        startActivity(intent)
    }

    private fun handleGetFeedsSuccess(notification: Notification<DiffUtil.DiffResult>) {
        if (notification.isOnNext) {
            d("TAGGGG", "on next feeds")
            notification.value?.dispatchUpdatesTo(ui.feedsAdapter)
        } else {
            // Todo: Handle later
            d("TAGGGG", "on error feeds")
        }
    }

    private fun handleShowProgressDialog(show: Boolean) {
        if (show) {
//            progressDialog.show()
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
        isLikeFromCommentScreen = false
        bottomSheetDialogComment.feed = feeds[position]
        bottomSheetDialogComment.position = position
        bottomSheetDialogComment.show(fragmentManager, "COMMENT")
    }

    private fun initSortDialog() {
        bottomSheetDialogComment = CommentFragment()
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

    private fun handleWhenAddComment(event: CommentEvent) {
        d("TAGGGGG", "handle add comment")
        viewModel.addComment(event.position, event.comment)
    }

    private fun handleWhenUpdateLike(event: LikeEvent) {
        isLikeFromCommentScreen = true
        viewModel.addLike(event.position)
        viewModel.removeLike(event.position)
    }

    private fun handleWhenUpdateUnLike(event: UnlikeEvent) {
        isLikeFromCommentScreen = true
        viewModel.removeLike(event.position)
    }

    private fun handleUpdateCommentSuccess(notification: Notification<MutableList<Comment>>) {
        if (notification.isOnNext) {
            val position = bottomSheetDialogComment.position
            val comments = notification.value!!
            bottomSheetDialogComment.ui.cmtFirst.visibility = View.GONE
            bottomSheetDialogComment.feed.comments.clear()
            bottomSheetDialogComment.feed.comments.addAll(comments)
            bottomSheetDialogComment.ui.commentsAdapter.notifyDataSetChanged()
            bottomSheetDialogComment.ui.recyclerComment.scrollToPosition(comments.size)
            bottomSheetDialogComment.ui.countComment.text = comments.size.toString().plus(" comments")
            feeds[position].commentCount = comments.size.toLong()
            feeds[position].comments.clear()
            feeds[position].comments.addAll(comments)
            ui.feedsAdapter.notifyItemChanged(position)
        } else {
            // Todo: Handle later
        }
    }

    private fun handleUpdateLikeFromCommentcreenSuccess(notification: Notification<Feed>) {
        if (notification.isOnNext) {
            if (isLikeFromCommentScreen) {
                val feed = notification.value!!
                bottomSheetDialogComment.feed = feed
                bottomSheetDialogComment.updateLike()
            }
        } else {
            // Todo: Handle later
        }
    }
}
