package com.example.hoavot.karaokeonline.ui.base

import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import com.example.hoavot.karaokeonline.R
import com.example.hoavot.karaokeonline.data.LocalRepository
import com.example.hoavot.karaokeonline.data.model.network.UnAuthorizeEvent
import com.example.hoavot.karaokeonline.data.source.api.RxBus
import com.example.hoavot.karaokeonline.ui.extensions.getCurrentFragment
import com.example.hoavot.karaokeonline.ui.extensions.observeOnUiThread
import com.example.hoavot.karaokeonline.ui.extensions.showAlertUnAuthorized
import com.example.hoavot.karaokeonline.ui.main.MainActivity
import com.example.hoavot.karaokeonline.ui.splash.SplashActivity
import com.example.hoavot.karaokeonline.ui.utils.AvoidRapidAction
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.jetbrains.anko.startActivity

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Use this class to create base function for all activities in this app
 */
abstract class BaseActivity : AppCompatActivity() {

    private val subscription: CompositeDisposable = CompositeDisposable()
    private lateinit var viewModel: BaseVMContract

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = BaseViewModel(LocalRepository(this))
        viewModel.reInitApiToken()

        if (this !is MainActivity) {
            overridePendingTransition(R.anim.slide_in_right, R.anim.nothing)
        }
        supportFragmentManager.addOnBackStackChangedListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // Handle change color status bar in term of use fragment
//                if (getCurrentFragment(R.id.userActivityContainer) is TermOfUseFragment
//                        || getCurrentFragment(R.id.settingContainer) is TermOfUseFragment) {
//                    window.statusBarColor = ContextCompat.getColor(this@BaseActivity, R.color.colorGrayVeryLight)
//                } else {
//                    window.statusBarColor = ContextCompat.getColor(this@BaseActivity, R.color.colorPrimary)
//                }
            }
        }
    }

    protected fun addDisposables(vararg ds: Disposable) {
        ds.forEach { subscription.add(it) }
    }

    override fun onPause() {
        super.onPause()
        subscription.clear()
    }

    override fun onResume() {
        super.onResume()
        onBindViewModel()
    }

    override fun onBackPressed() {
        AvoidRapidAction.action(AvoidRapidAction.DELAY_TIME) {
            super.onBackPressed()
            if (this !is MainActivity && this !is SplashActivity) {
                overridePendingTransition(R.anim.nothing, R.anim.slide_out_right)
            }
        }
    }

    /**
     * This function is used to define subscription
     */
    open fun onBindViewModel() {
        addDisposables(
                RxBus.listen(UnAuthorizeEvent::class.java)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::handleWhenSessionExpired)
        )
    }

    private fun handleWhenSessionExpired(unAuthorizeEvent: UnAuthorizeEvent) {
        showAlertUnAuthorized(unAuthorizeEvent.apiException) {
            viewModel.logOut()
                    .observeOnUiThread()
                    .subscribe({
                        finishAffinity()
                        startActivity<SplashActivity>()
                    }, {})
        }
    }
}
