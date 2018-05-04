package com.example.hoavot.karaokeonline.ui.profile

import android.graphics.Color
import android.view.Gravity
import android.widget.ImageView
import android.widget.TextView
import com.example.hoavot.karaokeonline.R
import org.jetbrains.anko.*

/**
 *
 * @author at-hoavo.
 */
class EditProfileFragmentUI : AnkoComponent<EditProfileFragment> {

    internal lateinit var nameUser: TextView
    internal lateinit var passWord: TextView
    internal lateinit var email: TextView
    internal lateinit var save: ImageView
    internal lateinit var edit: ImageView
    override fun createView(ui: AnkoContext<EditProfileFragment>) = with(ui) {
        verticalLayout {

            backgroundColor = Color.WHITE
            lparams(matchParent, matchParent)

            linearLayout {
                lparams(dip(250), dip(30)) {
                    topMargin = dip(50)
                    gravity = Gravity.CENTER_HORIZONTAL
                }

                nameUser = editText {
                    backgroundResource = R.drawable.custom_edittext_search_video
                    isEnabled = false
                    leftPadding = dip(10)
                }.lparams()
            }


            passWord = editText {
                backgroundResource = R.drawable.custom_edittext_search_video
                isEnabled = false
                leftPadding = dip(10)
            }.lparams(dip(250), dip(30)) {
                topMargin = dip(10)
                gravity = Gravity.CENTER_HORIZONTAL
            }

            email = editText {
                backgroundResource = R.drawable.custom_edittext_search_video
                isEnabled = false
                leftPadding = dip(10)
            }.lparams(dip(250), dip(30)) {
                topMargin = dip(10)
                gravity = Gravity.CENTER_HORIZONTAL
            }

            editText {
                backgroundResource = R.drawable.custom_edittext_search_video
                isEnabled = false
                leftPadding = dip(10)
            }.lparams(dip(250), dip(30)) {
                topMargin = dip(10)
                gravity = Gravity.CENTER_HORIZONTAL
            }


            save = imageView(R.drawable.ic_tick)
                    .lparams(dip(35), dip(35)) {
                        topMargin = dip(20)
                        gravity = Gravity.CENTER_HORIZONTAL
                    }
        }
    }
}
