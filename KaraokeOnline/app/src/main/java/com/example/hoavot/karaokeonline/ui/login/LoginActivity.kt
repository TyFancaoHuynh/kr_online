package com.example.hoavot.karaokeonline.ui.login

import android.content.Intent
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
import com.example.hoavot.karaokeonline.ui.main.MainActivity
import com.example.hoavot.karaokeonline.ui.register.RegisterActivity
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import org.jetbrains.anko.alert
import org.jetbrains.anko.setContentView
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.yesButton
import java.util.*
import com.facebook.GraphResponse
import org.json.JSONObject
import com.facebook.GraphRequest



/**
 * Created by TienHuynh3 on 12/05/2018.
 * Copyright © AsianTech inc...
 */
class LoginActivity : BaseActivity() {
    private val ui = LoginActivityUI()
    private lateinit var viewModel: LoginViewModel
    private var isRequesting = false
    private lateinit var user: User
    private var callbackManager: CallbackManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui.setContentView(this)
        viewModel = LoginViewModel(LocalRepository(this), KaraRepository())
        isRoot()
        FacebookSdk.sdkInitialize(applicationContext)
        callbackManager = CallbackManager.Factory.create()
        faceBookInitialize()
        ui.btnLoginFacebook.setOnClickListener {
//            LoginManager.getInstance().logInWithReadPermissions(this@LoginActivity, Arrays.asList("email", "user_photos", "public_profile"))
        }
    }

    fun faceBookInitialize() {
        LoginManager.getInstance().registerCallback(callbackManager!!, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                println("=========================onsuccess")
                val accessToken = AccessToken.getCurrentAccessToken()
                val request = GraphRequest.newMeRequest(accessToken) { `object`, response ->
                    println("===================JSON++" + `object`)
                    d("MMMMMMMM", "on success login :  ${`object`}")
                    var SfacebookID = ""
                    var Sname = ""
                    var Semail = ""
                    var Sgender = ""
                    var Surl = ""
                    val Sphone = ""

                    try {

                        if (`object`.has("id")) {
                            SfacebookID = `object`.getString("id")
                        }

                        if (`object`.has("name")) {
                            Sname = `object`.getString("name")
                        }

                        if (`object`.has("email")) {
                            Semail = `object`.getString("email")
                        }

                        if (`object`.has("gender")) {
                            Sgender = `object`.getString("gender")
                        }

                        if (`object`.has("picture")) {
                            Surl = `object`.getJSONObject("picture").getJSONObject("data").getString("url")
                        }
                        if (!isRequesting) {
                            isRequesting = true
                            viewModel.login(Sname, Semail)
                                    .observeOnUiThread()
                                    .doFinally {
                                        d("TAGGGGGGGGGGGGGG","login success0 ")
                                        isRequesting = false
                                    }
                                    .subscribe({
                                        d("TAGGGGGGGGGGGGGG","login success1")
                                        viewModel.saveInfor(it.token, it.user)
                                        ApiClient.getInstance(null).token = it.token
                                        startActivity<MainActivity>()
                                        finishAffinity()
                                    }, {
                                        d("TAGGGGGGGGGGGGGG","login eror")
                                        alert {
                                            title = getString(R.string.errorAlertTitle)
                                            if (it is ApiException) {
                                                (it as? ApiException)?.let {
                                                    message = it.messageError
                                                }
                                            } else {
                                                message = it.message ?: ""
                                            }
                                            positiveButton("Ok", {
                                            })
                                        }.show()
                                    })
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                val parameters = Bundle()
                parameters.putString("fields", "id,name,link,email,picture,gender, birthday")
                request.parameters = parameters
                request.executeAsync()

            }

            override fun onCancel() {
                //TODO Auto-generated method stub
                println("=========================onCancel")
                d("MMMMMMMM", "on cancel login")
            }

            override fun onError(error: FacebookException) {
                //TODO Auto-generated method stub
                println("=========================onError" + error.toString())
                d("MMMMMMMM", "on error login")
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        callbackManager!!.onActivityResult(requestCode, resultCode, data)
        if(callbackManager!!.onActivityResult(requestCode, resultCode, data)) {
            return;
        }
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