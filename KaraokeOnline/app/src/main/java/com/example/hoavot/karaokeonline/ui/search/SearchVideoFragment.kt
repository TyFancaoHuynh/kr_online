package com.example.hoavot.karaokeonline.ui.search

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hoavot.karaokeonline.data.model.nomal.Item
import com.example.hoavot.karaokeonline.ui.base.BaseFragment
import com.example.hoavot.karaokeonline.ui.extensions.hideKeyboard
import com.example.hoavot.karaokeonline.ui.play.PlayActivity
import com.example.hoavot.karaokeonline.ui.utils.ShowVideoAdapter
import io.reactivex.Notification
import io.reactivex.android.schedulers.AndroidSchedulers
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.support.v4.toast

/**
 *  Copyright © 2017 AsianTech inc.
 *  Created by hoavot on 12/12/2017.
 */
class SearchVideoFragment : BaseFragment() {
    private val items = mutableListOf<Item>()
    private var viewModel = SearchVideoViewModel()
    private lateinit var ui: SearchVideoFragmentUI
    private lateinit var query: String
    private lateinit var progressDialog: ProgressDialog

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        ui = SearchVideoFragmentUI(items, context)
        initProgressDialog()
        ui.adapterSearch.onItemClick = { item, type -> handleWhenItemVideoClick(item, type) }
        return ui.createView(AnkoContext.Companion.create(context, this))
    }

    override fun onBindViewModel() {
        addDisposables(
                viewModel
                        .itemsObserver
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                this::handleWhenSearchSuccess,
                                this::handleWhenSearchError
                        ),
                viewModel
                        .queryObserver
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            query = it
                        }, {}),
                viewModel
                        .progressDialogObserverable
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::handleProgressDialog)
        )
    }

    internal fun eventWhenSearchButtonClick(query: String) {
        viewModel.triggerSearchObserver.onNext(query)
        context.hideKeyboard(ui.edtInput)
    }

    private fun handleWhenSearchSuccess(itemVideos: MutableList<Item>) {
        items.clear()
        items.addAll(itemVideos)
        ui.adapterSearch.notifyDataSetChanged()
        ui.recyclerView.scrollToPosition(0)
    }

    private fun handleWhenSearchError(throwable: Throwable) {
        toast("error search")
    }

    private fun handleWhenItemVideoClick(item: Item, type: Int) {
        when (type) {
            ShowVideoAdapter.TYPE_VIDEO -> {
                val intent = Intent(context, PlayActivity::class.java)
                intent.apply {
                    putExtra(PlayActivity.TYPE_VIDEO, item)
                }
                startActivity(intent)
            }

            ShowVideoAdapter.TYPE_PLAY_LIST -> {

            }
            else -> {

            }
        }
    }

    private fun initProgressDialog() {
        progressDialog = ProgressDialog(context)
        progressDialog.setCancelable(false)
    }

    private fun handleProgressDialog(notification: Notification<Boolean>) {
        if (notification.isOnNext) {
            if (notification.value == true) {
                progressDialog.show()
            } else {
                progressDialog.hide()
            }
        } else {
            toast("Error occured. Please check error and try again!")
            progressDialog.hide()
        }
    }
}
