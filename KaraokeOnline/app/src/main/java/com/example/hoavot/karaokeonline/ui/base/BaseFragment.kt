package com.example.hoavot.karaokeonline.ui.base

import android.support.v4.app.Fragment
import com.example.hoavot.karaokeonline.R
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.jetbrains.anko.support.v4.indeterminateProgressDialog

/**
 *  Copyright © 2017 AsianTech inc.
 *  Created by hoavot on 12/12/2017.
 */
abstract class BaseFragment : Fragment() {
    private val subscription: CompositeDisposable = CompositeDisposable()
    private val progressDialog by lazy {
        indeterminateProgressDialog(
                getString(R.string.progress_dialog_message),
                getString(R.string.progress_dialog_title)
        )
    }

    override fun onPause() {
        super.onPause()
        subscription.clear()
    }

    override fun onResume() {
        super.onResume()
        onBindViewModel()
    }

    protected fun addDisposables(vararg ds: Disposable) {
        ds.forEach { subscription.add(it) }
    }

    protected fun showProgressDialog(message: String, title: String) {
        progressDialog.setMessage(message)
        progressDialog.setTitle(title)
    }

    protected fun showProgressDialog() {
        progressDialog.show()
    }

    protected fun hideProgressDialog() {
        progressDialog.hide()
    }

    /**
     * This function is used to define subscription
     */
    abstract fun onBindViewModel()
}