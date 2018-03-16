package com.example.hoavot.karaokeonline.ui.play.fab

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import com.example.hoavot.karaokeonline.R
import org.jetbrains.anko.*
import org.jetbrains.anko.custom.ankoView
import org.jetbrains.anko.sdk25.coroutines.onClick
import vn.asiantech.way.extension.setAnimation

@SuppressLint("ViewConstructor")
/**
 * FloatingMenuButton.
 *
 * @author at-ToanNguyen
 */
class FloatingMenuButton(private var onMenuClickListener: OnMenuClickListener,
                         context: Context, attrs: AttributeSet? = null) :
        LinearLayout(context, attrs) {
    internal lateinit var imgBtnSearch: ImageButton
    internal lateinit var imgBtnRecord: ImageButton
    internal lateinit var imgBtnDownload: ImageButton
    private lateinit var rlSearch: RelativeLayout
    private lateinit var rlRecord: RelativeLayout
    private lateinit var rlDownload: RelativeLayout
    private lateinit var imgBtnMenu: ImageButton
    private lateinit var frOverlay: FrameLayout
    private var isExpand = false

    init {
        AnkoContext.createDelegate(this).apply {
            relativeLayout {
                lparams(matchParent, matchParent)
                frOverlay = frameLayout {
                    lparams(matchParent, matchParent)
                    visibility = View.GONE
                    backgroundResource = R.color.colorOverlay
                    onClick {
                        if (isExpand) {
                            collapseMenu()
                            isExpand = false
                            setGoneOverLay()
                        }
                    }
                }
                verticalLayout {
                    gravity = Gravity.END or Gravity.BOTTOM
                    lparams(matchParent, matchParent)
                    rlSearch = itemFloatingButton(R.id.floating_btn_menu_img_btn_search,
                            R.drawable.custom_bg_item_search_button,
                            R.mipmap.ic_search, R.string.custom_floating_menu_search_title)
                    imgBtnSearch = rlSearch.find(R.id.floating_btn_menu_img_btn_search)
                    rlRecord = itemFloatingButton(R.id.floating_btn_menu_img_btn_record,
                            R.drawable.custom_bg_item_record_button,
                            R.mipmap.ic_search, R.string.custom_floating_menu_record_title)
                    imgBtnRecord = rlRecord.find(R.id.floating_btn_menu_img_btn_record)
                    rlDownload = itemFloatingButton(R.id.floating_btn_menu_img_btn_download,
                            R.drawable.custom_bg_item_download_button,
                            R.mipmap.ic_search, R.string.custom_floating_menu_download_title)
                    imgBtnDownload = rlDownload.find(R.id.floating_btn_menu_img_btn_download)
                    imgBtnMenu = imageButton {
                        id = R.id.floating_btn_menu
                        imageResource = R.mipmap.ic_menu
                        backgroundResource = R.drawable.custom_menu_button
                    }.lparams(dimen(R.dimen.width_height_image_button_menu),
                            dimen(R.dimen.width_height_image_button_menu)) {
                        gravity = Gravity.END
                        verticalMargin = dimen(R.dimen.top_bot_margin_image_button_menu)
                        padding = dimen(R.dimen.padding_floating)
                    }
                }.applyRecursively { view: View ->
                    when (view) {
                        is RelativeLayout -> {
                            view.gravity = Gravity.END
                            view.visibility = View.INVISIBLE
                        }
                        is TextView -> view.gravity = Gravity.CENTER
                        is ImageButton -> view.onClick {
                            if (view.id == R.id.floating_btn_menu) {
                                onMenuClick()
                                frOverlay.visibility = if (isExpand) View.VISIBLE else View.GONE
                            } else {
                                setGoneOverLay()
                                visibilityAllChildView(View.INVISIBLE)
                                onMenuClickListener.eventItemMenuClicked(view)
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Collapse menu
     */
    private fun collapseMenu() {
        val animInvisible: Animation = AnimationUtils.loadAnimation(context, R.anim.anim_invisible)
        startAnimationFab(animInvisible)
        visibilityAllChildView(View.INVISIBLE)
    }

    private fun ViewManager.itemFloatingButton(icId: Int, icBg: Int, icSrc: Int, title: Int) = relativeLayout {
        lparams(dimen(R.dimen.width_relative_layout), wrapContent)
        imageButton {
            id = icId
            imageResource = icSrc
            backgroundResource = icBg
        }.lparams(dimen(R.dimen.width_height_image_button), dimen(R.dimen.width_height_image_button)) {
            alignParentRight()
            rightMargin = dimen(R.dimen.margin_right_image_button)
        }
        textView(title) {
            horizontalPadding = dimen(R.dimen.padding_floating)
            textSize = px2dip(dimen(R.dimen.custom_menu_text_size))
            backgroundResource = R.drawable.custom_bg_item_menu_title
        }.lparams(wrapContent, dimen(R.dimen.height_text_view)) {
            centerVertically()
            rightMargin = dimen(R.dimen.margin_right_text_view)
            leftOf(icId)
            gravity = Gravity.CENTER
        }
    }

    private fun onMenuClick() {
        val anim: Animation = AnimationUtils.loadAnimation(context, R.anim.anim_rotate)
        val animVisible: Animation = AnimationUtils.loadAnimation(context, R.anim.anim_visible)
        val animInvisible: Animation = AnimationUtils.loadAnimation(context, R.anim.anim_invisible)
        imgBtnMenu.startAnimation(anim)
        isExpand = if (checkItemViewVisibility()) {
            startAnimationFab(animVisible)
            visibilityAllChildView(View.VISIBLE)
            true
        } else {
            startAnimationFab(animInvisible)
            false
        }
        animInvisible.setAnimation {
            visibilityAllChildView(View.INVISIBLE)
        }
    }

    private fun startAnimationFab(animation: Animation) {
        rlDownload.startAnimation(animation)
        rlRecord.startAnimation(animation)
        rlSearch.startAnimation(animation)
    }

    private fun visibilityAllChildView(visibilityState: Int) {
        rlDownload.visibility = visibilityState
        rlRecord.visibility = visibilityState
        rlSearch.visibility = visibilityState
    }

    private fun checkItemViewVisibility(): Boolean =
            rlDownload.visibility == View.INVISIBLE ||
                    rlRecord.visibility == View.INVISIBLE ||
                    rlSearch.visibility == View.INVISIBLE

    private fun setGoneOverLay() {
        frOverlay.visibility = View.GONE
    }

    /**
     * Interface menu click listener
     */
    interface OnMenuClickListener {
        /**
         * Event when Item button menu Click
         */
        fun eventItemMenuClicked(view: View)
    }
}

/**
 * Init custom view
 */
inline fun ViewManager.floatingButton(onMenuClickListener: FloatingMenuButton.OnMenuClickListener,
                                      init: FloatingMenuButton.() -> Unit):
        FloatingMenuButton = ankoView({ FloatingMenuButton(onMenuClickListener, it, null) },
        theme = 0, init = init)
