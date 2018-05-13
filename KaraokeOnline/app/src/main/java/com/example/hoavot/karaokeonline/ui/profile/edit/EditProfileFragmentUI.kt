package com.example.hoavot.karaokeonline.ui.profile.edit

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.text.InputType
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.hoavot.karaokeonline.R
import com.example.hoavot.karaokeonline.data.model.other.User
import com.example.hoavot.karaokeonline.ui.extensions.enableHighLightWhenClicked
import com.example.hoavot.karaokeonline.ui.extensions.onClickWithAvoidRapidAction
import com.example.hoavot.karaokeonline.ui.utils.AvoidRapidAction
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 *
 * @author at-hoavo.
 */
class EditProfileFragmentUI(private val user: User) : AnkoComponent<EditProfileFragment> {

    internal lateinit var nameUser: TextView
    internal lateinit var passWord: TextView
    internal lateinit var email: TextView
    internal lateinit var save: Button
    internal lateinit var editProfile: Button
    internal lateinit var edit: ImageView
    override fun createView(ui: AnkoContext<EditProfileFragment>) = with(ui) {
        verticalLayout {
            backgroundColor = Color.WHITE
            lparams(matchParent, matchParent)

            toolbar {
                lparams(matchParent, dimen(R.dimen.toolBarHeight)) {
                    leftMargin = -dip(15)
                }
                id = R.id.toolBarCaption
                relativeLayout {
                    lparams(matchParent, matchParent)
                    imageView(R.drawable.ic_left_arrow) {
                        id = R.id.profileFragmentToolBarBack
                        topPadding = dimen(R.dimen.toolBarLeftRightIconTopPadding)
                        bottomPadding = dimen(R.dimen.toolBarLeftRightIconBottomPadding)
                        enableHighLightWhenClicked()
                        onClickWithAvoidRapidAction(AvoidRapidAction.DELAY_TIME) {
                            owner.eventOnBackClicked()
                        }
                    }.lparams(dip(40), matchParent)

                    textView(R.string.profileFragmentEditToolbarTitle) {
                        id = R.id.profileFragmentToolBarTitleEdit
                        textSize = px2dip(dimen(R.dimen.toolBarMiddleTitleTextSize))
                        maxLines = 1
                        ellipsize = TextUtils.TruncateAt.END
                        textColor = ContextCompat.getColor(ctx, android.R.color.black)
                    }.lparams {
                        centerHorizontally()
                        topMargin = dimen(R.dimen.uploadReviewFragmentToolBarTitleTopPadding)
                    }

                    imageView(R.drawable.ic_complete) {
                        id = R.id.profileFragmentToolBarTitleEditNext
                        enableHighLightWhenClicked()
                        onClickWithAvoidRapidAction(AvoidRapidAction.DELAY_TIME) {
                            owner.eventOnCompleteButtonClicked()
                        }
                        topPadding = dimen(R.dimen.toolBarLeftRightIconTopPadding)
                        bottomPadding = dimen(R.dimen.toolBarLeftRightIconBottomPadding)
                    }.lparams(dip(40), matchParent) {
                        alignParentRight()
                    }
                }
            }

            linearLayout {
                lparams(dip(300), dip(50)) {
                    topMargin = dip(50)
                    gravity = Gravity.CENTER_HORIZONTAL
                }

                textView("Username") {
                    textSize = px2dip(dimen(R.dimen.textSize15))
                }.lparams(dip(0), matchParent) {
                    weight = 2f
                }

                nameUser = editText(user.username) {
                    backgroundResource = R.drawable.custom_edittext_search_video
                    textColor=ContextCompat.getColor(context,R.color.colorBlackBold)
                    leftPadding = dip(10)
                }.lparams(dip(0), matchParent) {
                    weight = 6f
                    leftMargin = dip(7)
                }
            }

            linearLayout {
                lparams(dip(300), dip(50)) {
                    topMargin = dip(10)
                    gravity = Gravity.CENTER_HORIZONTAL
                }

                textView("Password") {
                    textSize = px2dip(dimen(R.dimen.textSize15))
                }.lparams(dip(0), matchParent) {
                    weight = 2f
                }

                passWord = editText(user.password) {
                    inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                    backgroundResource = R.drawable.custom_edittext_search_video
                    textColor=ContextCompat.getColor(context,R.color.colorBlackBold)
                    leftPadding = dip(10)
                }.lparams(dip(0), matchParent) {
                    weight = 6f
                    leftMargin = dip(7)
                }
            }

            linearLayout {
                lparams(dip(300), dip(50)) {
                    topMargin = dip(10)
                    gravity = Gravity.CENTER_HORIZONTAL
                }

                textView("Email") {
                    textSize = px2dip(dimen(R.dimen.textSize15))
                }.lparams(dip(0), matchParent) {
                    weight = 2f
                }

                email = editText(user.email) {
                    backgroundResource = R.drawable.custom_edittext_search_video
                    leftPadding = dip(10)
                    textColor=ContextCompat.getColor(context,R.color.colorBlackBold)
                }.lparams(dip(0), matchParent) {
                    weight = 6f
                    leftMargin = dip(7)
                }
            }

            linearLayout {
                editProfile = button("Edit") {
                    backgroundColor = ContextCompat.getColor(context, R.color.colorRed)
                    textColor = Color.WHITE
                    enableHighLightWhenClicked()
                    onClick {
                        owner.eventOnEditClick()
                    }
                }

                save = button("Save") {
                    backgroundColor = ContextCompat.getColor(context, R.color.colorRed)
                    textColor = Color.WHITE
                    enableHighLightWhenClicked()
                    onClick {
                        owner.eventOnSaveCLicked()
                    }
                }.lparams {
                    leftMargin = dip(6)
                }
            }.lparams(wrapContent, dip(40)) {
                gravity = Gravity.END
                topMargin = dip(15)
                rightMargin = dip(20)
            }
        }
    }
}
