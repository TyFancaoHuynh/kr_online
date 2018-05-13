package com.example.hoavot.karaokeonline.ui.login

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.text.InputType
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.example.hoavot.karaokeonline.R
import com.example.hoavot.karaokeonline.ui.extensions.enableHighLightWhenClicked
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 * Created by TienHuynh3 on 12/05/2018.
 * Copyright © AsianTech inc...
 */
class LoginActivityUI : AnkoComponent<LoginActivity> {
    internal lateinit var edtUserName: EditText
    internal lateinit var edtPass: EditText
    internal lateinit var btnLogin: Button
    internal lateinit var tvSignIn: TextView
    internal lateinit var tvNotify: TextView
    override fun createView(ui: AnkoContext<LoginActivity>) = with(ui) {
        verticalLayout {
            backgroundDrawable = ContextCompat.getDrawable(context, R.drawable.bglogin)
            lparams(matchParent, matchParent)
            gravity = Gravity.CENTER
            verticalLayout {
                gravity = Gravity.CENTER
                backgroundColor = Color.WHITE
                this.background.alpha = 70
                imageView(R.drawable.ic_user_login).lparams(dip(64), dip(64)) {
                    topMargin = dip(20)
                }

                tvSignIn = textView("Sign In") {
                    textSize = px2sp(dip(30))
                    textColor = R.color.colorTextSearch
                }.lparams {
                    topMargin = dip(10)
                }

                linearLayout {
                    orientation = LinearLayout.HORIZONTAL
                    gravity = Gravity.CENTER
                    backgroundDrawable = ContextCompat.getDrawable(ctx, R.drawable.bg_edt_login)

                    imageView(R.drawable.ic_edt_user_left).lparams {
                        leftMargin = dip(10)
                    }
                    edtUserName = editText {
                        backgroundColor = Color.WHITE
                        hint = "User Name"
                        inputType = InputType.TYPE_CLASS_TEXT
                    }.lparams(matchParent, wrapContent) {
                        horizontalMargin = dip(10)
                    }
                }.lparams(matchParent, wrapContent) {
                    topMargin = dip(10)
                    horizontalMargin = dip(20)
                }
                linearLayout {
                    orientation = LinearLayout.HORIZONTAL
                    gravity = Gravity.CENTER
                    backgroundDrawable = ContextCompat.getDrawable(ctx, R.drawable.bg_edt_login)

                    imageView(R.drawable.edt_pass_left).lparams {
                        leftMargin = dip(10)
                    }
                    edtPass = editText {
                        backgroundColor = Color.WHITE
                        hint = "Password"
                        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    }.lparams(matchParent, wrapContent) {
                        horizontalMargin = dip(10)
                    }
                }.lparams(matchParent, wrapContent) {
                    topMargin = dip(10)
                    horizontalMargin = dip(20)
                }
                btnLogin = button("Login") {
                    backgroundDrawable = ContextCompat.getDrawable(ctx, R.drawable.bg_btn_login)
                    textColor = Color.WHITE
                    enableHighLightWhenClicked()
                    onClick { owner.eventLoginBtnClicked() }
                }.lparams(matchParent, wrapContent) {
                    horizontalMargin = dip(20)
                    topMargin = dip(10)
                    bottomMargin = dip(20)
                }

            }.lparams(dip(300), wrapContent)

            tvNotify = textView("Do not have account? Register") {
                textColor = Color.WHITE
                onClick { owner.eventRegisterTvClicked() }
            }.lparams {
                topMargin = dip(20)
                gravity = Gravity.CENTER_HORIZONTAL
            }
        }
    }
}