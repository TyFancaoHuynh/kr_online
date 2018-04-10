package com.example.hoavot.karaokeonline.ui.feed

import android.app.ProgressDialog
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hoavot.karaokeonline.data.model.other.Feed
import com.example.hoavot.karaokeonline.ui.base.BaseFragment
import com.example.hoavot.karaokeonline.ui.extensions.observeOnUiThread
import com.example.hoavot.karaokeonline.ui.feed.comment.BottomSheetCommentUI
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
    private lateinit var bottomSheetCommentUI: BottomSheetCommentUI
    private var updateCommentsAdapter: (DiffUtil.DiffResult) -> Unit = {}
    private lateinit var bottomSheetDialog: BottomSheetDialog

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

    private fun initSortDialog() {
        bottomSheetCommentUI = BottomSheetCommentUI()
        bottomSheetDialog = BottomSheetDialog(context)
        bottomSheetDialog.setContentView(bottomSheetCommentUI.createView(AnkoContext.Companion.create(context, this)))
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
            updateCommentsAdapter(notification.value!!)
        } else {
            // Todo: Handle later
        }
    }

    private fun eventWhenLikeclicked(position: Int) {
        viewModel.addLike(position)
    }

    private fun eventWhenUnLikeclicked(position: Int) {
        viewModel.removeLike(position)
    }

    private fun eventWhenCommentclicked(position: Int) {

        bottomSheetDialog.show()
        // Handle later
    }

    private fun initProgressDialog() {
        progressDialog = ProgressDialog(context)
        progressDialog.setCancelable(false)
    }
}