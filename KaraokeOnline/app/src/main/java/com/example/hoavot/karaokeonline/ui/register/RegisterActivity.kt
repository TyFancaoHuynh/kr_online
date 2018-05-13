package com.example.hoavot.karaokeonline.ui.register

import android.os.Bundle
import android.util.Log.d
import com.example.hoavot.karaokeonline.R
import com.example.hoavot.karaokeonline.data.LocalRepository
import com.example.hoavot.karaokeonline.data.model.other.User
import com.example.hoavot.karaokeonline.data.source.KaraRepository
import com.example.hoavot.karaokeonline.data.source.api.ApiClient
import com.example.hoavot.karaokeonline.data.source.api.ApiException
import com.example.hoavot.karaokeonline.data.source.response.LoginResponse
import com.example.hoavot.karaokeonline.ui.base.BaseActivity
import com.example.hoavot.karaokeonline.ui.extensions.observeOnUiThread
import com.example.hoavot.karaokeonline.ui.login.LoginActivityUI
import com.example.hoavot.karaokeonline.ui.login.LoginViewModel
import com.example.hoavot.karaokeonline.ui.main.MainActivity
import org.jetbrains.anko.alert
import org.jetbrains.anko.setContentView
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.yesButton

/**
 *
 * @author at-hoavo
 */
class RegisterActivity : BaseActivity() {
    private val ui = RegisterActivityUI()
    private lateinit var viewModel: LoginViewModel
    private var isRequesting = false
    private lateinit var user: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui.setContentView(this)
        viewModel = LoginViewModel(LocalRepository(this), KaraRepository())
    }

    internal fun eventRegisterButtonClicked() {
        register()
    }

    internal fun register() {
        val username = ui.edtUserName.text.toString()
        val password = ui.edtPass.text.toString()
        user = User(username = username, password = password)
        if (username.isNotBlank() && password.isNotBlank()) {
            if (!isRequesting) {
                isRequesting = true
                viewModel.register(username, password)
                        .observeOnUiThread()
                        .doFinally {
                            isRequesting = false
                        }
                        .subscribe(this::handleRegisterSuccess, this::handleRegisterError)
            }
        } else {
            alert {
                title = "ERROR"
                message = "Bạn phải nhập đầy đủ thông tin!"

                yesButton { }
            }
        }
    }

    private fun handleRegisterError(throwable: Throwable) {
        d("TAGG", "error at register: ${throwable.message}")
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

    private fun handleRegisterSuccess(result: LoginResponse) {
        viewModel.saveInfor(result.token, user)
        ApiClient.getInstance(null).token = result.token
        startActivity<MainActivity>()
        finishAffinity()
    }
}
