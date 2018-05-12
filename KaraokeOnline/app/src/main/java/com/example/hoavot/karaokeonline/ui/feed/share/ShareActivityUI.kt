package com.example.hoavot.karaokeonline.ui.feed.share

import android.graphics.Color
import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.util.Log.d
import android.view.View
import android.view.ViewManager
import android.widget.TextView
import com.example.hoavot.karaokeonline.R
import com.example.hoavot.karaokeonline.ui.extensions.circleImageView
import com.example.hoavot.karaokeonline.ui.extensions.enableHighLightWhenClicked
import com.facebook.share.widget.ShareButton
import de.hdodenhof.circleimageview.CircleImageView
import org.jetbrains.anko.*
import org.jetbrains.anko.custom.ankoView
import org.jetbrains.anko.sdk25.coroutines.onClick
import com.example.hoavot.karaokeonline.data.model.other.User
/**
 *
 * @author at-hoavo
 */
class ShareActivityUI(private val fileMusic: String,private val user:User) : AnkoComponent<ShareActivity> {
    internal lateinit var avatar: CircleImageView
    internal lateinit var tvUsername: TextView
    internal lateinit var tvSong: TextView

    override fun createView(ui: AnkoContext<ShareActivity>): View {
        return with(ui) {
            relativeLayout {
                avatar = circleImageView {
                    id = R.id.captionFragmentAvartar
                    borderWidth = dip(0.4f)
                    borderColor = Color.GRAY
                }.lparams(dip(30), dip(30)) {
                    leftMargin = dip(15)
                    verticalMargin = dip(15)
                }

                tvUsername = textView (user.username){
                    id = R.id.captionFragmenttvUsername
                    textSize = px2dip(dimen(R.dimen.textSize15))
                    typeface = Typeface.DEFAULT_BOLD
                    textColor = Color.BLACK
                }.lparams {
                    rightOf(R.id.captionFragmentAvartar)
                    sameTop(R.id.captionFragmentAvartar)
                    leftMargin = dip(5)
                }

                textView("Bài hát của tôi") {
                    id = R.id.shareActivityTextMusic
                    textSize = px2dip(dimen(R.dimen.textSize14))
                    typeface = Typeface.DEFAULT_BOLD
                    textColor = ContextCompat.getColor(context, R.color.colorBlackBold)
                }.lparams {
                    rightOf(R.id.captionFragmenttvFileRecordImage)
                    sameTop(R.id.captionFragmentAvartar)
                    leftMargin = dip(10)
                }

                tvSong = textView(fileMusic) {
                    id = R.id.shareActivityTextSong
                    textSize = px2dip(dimen(R.dimen.textSize13))
                    typeface = Typeface.DEFAULT_BOLD
                    textColor = ContextCompat.getColor(context, R.color.colorGrayLight)
                }.lparams {
                    sameLeft(R.id.shareActivityTextMusic)
                    below(R.id.shareActivityTextMusic)
                }

                loginFacebook {
                    isEnabled=true
                    enableHighLightWhenClicked()
                    onClick {
                        d("TAGGG","on share touch")
                        owner.eventWhenLoginFaceBook()
                    }
                }.lparams(dip(150), dip(40)) {
                    below(R.id.shareActivityTextSong)
                    topMargin = dip(50)
                }
            }
        }

    }

    inline fun ViewManager.loginFacebook(init: ShareButton.() -> Unit): ShareButton {
        return ankoView({ ShareButton(it) }, theme = 0, init = init)
    }
}
