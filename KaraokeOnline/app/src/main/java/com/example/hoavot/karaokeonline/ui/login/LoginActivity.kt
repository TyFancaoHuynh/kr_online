package com.example.hoavot.karaokeonline.ui.login

import android.os.Bundle
import android.util.Log.d
import android.view.View
import com.example.hoavot.karaokeonline.R
import com.example.hoavot.karaokeonline.data.LocalRepository
import com.example.hoavot.karaokeonline.data.model.other.User
import com.example.hoavot.karaokeonline.data.source.KaraRepository
import com.example.hoavot.karaokeonline.data.source.api.ApiClient
import com.example.hoavot.karaokeonline.data.source.api.ApiException
import com.example.hoavot.karaokeonline.data.source.response.LoginResponse
import com.example.hoavot.karaokeonline.ui.base.BaseActivity
import com.example.hoavot.karaokeonline.ui.extensions.observeOnUiThread
import com.example.hoavot.karaokeonline.ui.main.MainActivity
import com.example.hoavot.karaokeonline.ui.register.RegisterActivity
import org.jetbrains.anko.alert
import org.jetbrains.anko.setContentView
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.yesButton

/**
 * Created by TienHuynh3 on 12/05/2018.
 * Copyright © AsianTech inc...
 */
class LoginActivity : BaseActivity() {
    private val ui = LoginActivityUI()
    private lateinit var viewModel: LoginViewModel
    private var isRequesting = false
    private lateinit var user: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui.setContentView(this)
        viewModel = LoginViewModel(LocalRepository(this), KaraRepository())
        isRoot()
    }

    internal fun eventLoginBtnClicked() {
        login()
    }

    internal fun eventRegisterTvClicked() {
        startActivity<RegisterActivity>()
    }

    internal fun login() {
        val username = ui.edtUserName.text.toString()
        val password = ui.edtPass.text.toString()
        user = User(username = username, password = password)
        if (username.isNotBlank() && password.isNotBlank()) {
            if (!isRequesting) {
                isRequesting = true
                viewModel.login(username, password)
                        .observeOnUiThread()
                        .doFinally {
                            isRequesting = false
                        }
                        .subscribe(this::handleLoginSuccess, this::handleLoginError)
            }
        } else {
            alert {
                title = "ERROR"
                message = "Bạn phải nhập đầy đủ thông tin!"

                yesButton { }
            }.show()
        }
    }

    private fun handleLoginError(throwable: Throwable) {
        alert {
            title = getString(R.string.errorAlertTitle)
            if (throwable is ApiException) {
                (throwable as? ApiException)?.let {
                    message = it.messageError
                }
            } else {
                message = throwable.message ?: ""
            }
            positiveButton("Ok", {
            })
        }.show()
    }

    private fun handleLoginSuccess(result: LoginResponse) {
        d("TAGGGGG", "tokenlogin: ${result.token}")
        viewModel.saveInfor(result.token, result.user)
        ApiClient.getInstance(null).token = result.token
        startActivity<MainActivity>()
        finishAffinity()
    }

    private fun isRoot() {
        if (isTaskRoot) {
            if (viewModel.hasAppToken().isNotBlank()) {
                val token = viewModel.hasAppToken()
                ApiClient.getInstance(null).token = token
                startActivity<MainActivity>()
                finishAffinity()
            }
        }
    }
}