package com.example.hoavot.karaokeonline.ui.login

import android.graphics.Color
import android.os.Bundle
import com.example.hoavot.karaokeonline.ui.base.BaseActivity
import jp.wasabeef.blurry.Blurry
import org.jetbrains.anko.setContentView

/**
 * Created by TienHuynh3 on 12/05/2018.
 * Copyright © AsianTech inc...
 */
class LoginActivity : BaseActivity() {
    val ui = LoginActivityUI()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui.setContentView(this)
    }
    internal fun eventLoginBtnClicked(){
        //TODO handle login btn
    }

    internal fun eventRegisterTvClicked(){
        //TODO handle register later
    }
}