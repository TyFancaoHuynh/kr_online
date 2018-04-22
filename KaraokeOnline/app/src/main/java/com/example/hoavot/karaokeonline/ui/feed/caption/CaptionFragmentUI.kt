package com.example.hoavot.karaokeonline.ui.feed.caption

import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.example.hoavot.karaokeonline.R
import com.example.hoavot.karaokeonline.ui.extensions.enableHighLightWhenClicked
import com.example.hoavot.karaokeonline.ui.extensions.onClickWithAvoidRapidAction
import com.example.hoavot.karaokeonline.ui.utils.AvoidRapidAction
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.toolbar

/**
 *
 * @author at-hoavo.
 */
class CaptionFragmentUI : AnkoComponent<CaptionFragment> {
    internal lateinit var edtCaption: EditText
    internal lateinit var tvTitle: TextView
    internal lateinit var toolbar: Toolbar
    override fun createView(ui: AnkoContext<CaptionFragment>): View = with(ui) {
        relativeLayout {
            toolbar = toolbar {
                lparams(matchParent, dimen(R.dimen.toolBarHeight))
                relativeLayout {
                    lparams(matchParent, matchParent)
                    imageView(R.drawable.ic_navigate_before_black_36dp) {
                        id = R.id.captionFragmentToolBarBack
                        topPadding = dimen(R.dimen.toolBarLeftRightIconTopPadding)
                        bottomPadding = dimen(R.dimen.toolBarLeftRightIconBottomPadding)
                        horizontalPadding = dimen(R.dimen.toolBarPadding)
                        enableHighLightWhenClicked()
                        onClickWithAvoidRapidAction(AvoidRapidAction.DELAY_TIME) {
                            owner.eventOnBackClicked()
                        }
                    }

                    tvTitle = textView(R.string.uploadReviewFragmentToolbarTitle) {
                        id = R.id.captionFragmentToolBarTitle
                        textSize = px2dip(dimen(R.dimen.toolBarMiddleTitleTextSize))
                        maxLines = 1
                        ellipsize = TextUtils.TruncateAt.END
                        textColor = ContextCompat.getColor(ctx, android.R.color.black)
                    }.lparams {
                        centerHorizontally()
                        topMargin = dimen(R.dimen.uploadReviewFragmentToolBarTitleTopPadding)
                    }

                    imageView(R.drawable.ic_navigate_before_black_36dp) {
                        id = R.id.captionFragmentToolBarUpload
                        enableHighLightWhenClicked()
                        onClickWithAvoidRapidAction(AvoidRapidAction.DELAY_TIME) {
                            owner.eventOnCompleteButtonClicked()
                        }
                        topPadding = dimen(R.dimen.toolBarLeftRightIconTopPadding)
                        bottomPadding = dimen(R.dimen.toolBarLeftRightIconBottomPadding)
                        horizontalPadding = dimen(R.dimen.toolBarPadding)
                    }.lparams {
                        alignParentRight()
                    }
                }
            }
            edtCaption = editText {
                topPadding = dip(20)
                hint = resources.getString(R.string.uploadReviewFragmentEdtCaptionHint)
                backgroundDrawable = null
                gravity = Gravity.TOP or Gravity.START
                id = R.id.captionFragmentEdtCaption
                hintTextColor = ContextCompat.getColor(ctx, R.color.colorGrayLight)
                textColor = ContextCompat.getColor(ctx, android.R.color.black)
                textSize = px2dip(dimen(R.dimen.captionFragmentEdtCaptionTextSize))
            }.lparams(matchParent, matchParent) {
                rightMargin = dimen(R.dimen.captionFragmentEdtHorizontalPadding)
            }
        }
    }
}