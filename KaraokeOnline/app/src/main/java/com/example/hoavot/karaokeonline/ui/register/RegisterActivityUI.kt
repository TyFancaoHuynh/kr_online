package com.example.hoavot.karaokeonline.ui.register

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.example.hoavot.karaokeonline.R
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 *
 * @author at-hoavo
 */
class RegisterActivityUI : AnkoComponent<RegisterActivity> {
    internal lateinit var edtUserName: EditText
    internal lateinit var edtPass: EditText
    internal lateinit var btnRegister: Button
    internal lateinit var tvSignIn: TextView
    override fun createView(ui: AnkoContext<RegisterActivity>) = with(ui) {
        verticalLayout {
            backgroundDrawable = ContextCompat.getDrawable(context, R.drawable.bg_register)
            lparams(matchParent, matchParent)
            gravity = Gravity.CENTER
            verticalLayout {
                gravity = Gravity.CENTER
                backgroundColor = Color.WHITE
                this.background.alpha = 70

                tvSignIn = textView("Register") {
                    textSize = px2sp(dip(40))
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

                linearLayout {
                    orientation = LinearLayout.HORIZONTAL
                    gravity = Gravity.CENTER
                    backgroundDrawable = ContextCompat.getDrawable(ctx, R.drawable.bg_edt_login)

                    imageView(R.drawable.edt_pass_left).lparams {
                        leftMargin = dip(10)
                    }
                    edtPass = editText {
                        backgroundColor = Color.WHITE
                        hint = "Confirm Password"
                        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    }.lparams(matchParent, wrapContent) {
                        horizontalMargin = dip(10)
                    }
                }.lparams(matchParent, wrapContent) {
                    topMargin = dip(10)
                    horizontalMargin = dip(20)
                }

                btnRegister = button("Register") {
                    backgroundDrawable = ContextCompat.getDrawable(ctx, R.drawable.bg_btn_login)
                    textColor = Color.WHITE
                    onClick { owner.eventRegisterButtonClicked() }
                }.lparams(matchParent, wrapContent) {
                    horizontalMargin = dip(20)
                    topMargin = dip(10)
                    bottomMargin = dip(20)
                }


            }.lparams(dip(300), wrapContent)
        }
    }
}
