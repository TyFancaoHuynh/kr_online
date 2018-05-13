package com.example.hoavot.karaokeonline.ui.feed.comment

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.BottomSheetDialogFragment
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.example.hoavot.karaokeonline.data.model.other.*
import com.example.hoavot.karaokeonline.ui.extensions.*
import org.jetbrains.anko.AnkoContext


/**
 *
 * @author at-hoavo.
 */
class CommentFragment : BottomSheetDialogFragment() {
    internal lateinit var ui: CommentLayoutUI
    internal lateinit var feed: Feed
    internal var position = -1
    internal var isFeed=false

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        ui = CommentLayoutUI(feed.comments)
        return ui.createView(AnkoContext.Companion.create(context, this, false))
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (feed.comments.size == 0) {
            ui.cmtFirst.visibility = View.VISIBLE
        }
        ui.countComment.text = feed.commentCount.toString().plus(" comments")
        updateLike()
        ui.areaComment.setOnVisibilityChangeListener(object : OnVisibilityChangeListener {
            override fun onShowKeyboard(keyboardHeight: Int) {
                val paddingBottom: Int = if (context.hasNavBar()) {
                    val padding = if (Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
                        keyboardHeight + activity.getStatusBarHeight() - activity.getNavigationBarHeight()
                    } else {
                        keyboardHeight - activity.getStatusBarHeight() - activity.getNavigationBarHeight()
                    }
                    padding
                } else {
                    keyboardHeight - activity.getStatusBarHeight()
                }
                ui.areaComment.setPadding(0, 0, 0, paddingBottom)
            }

            override fun onHideKeyboard() {
                ui.areaComment.setPadding(0, 0, 0, 0)
            }
        })
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        dialog.setOnShowListener { dialog ->
            val d = dialog as BottomSheetDialog

            val bottomSheet = d.findViewById<View>(android.support.design.R.id.design_bottom_sheet) as FrameLayout?
            BottomSheetBehavior.from(bottomSheet!!).state = BottomSheetBehavior.STATE_EXPANDED
        }


        // Do something with your dialog like setContentView() or whatever
        return dialog
    }

    internal fun eventWhenAddCommentClicked(text: String) {
        if (text.isBlank()) {
            context.showAlertNotification("WARNING", "please input comment!") {}
        } else {
            ui.edtComment.text.clear()
            if(isFeed) {
                RxBus.publish(CommentFeedMeEvent(position, text))
                return
            }
            RxBus.publish(CommentEvent(position, text))
            d("TAGGGG", "comment click")
        }
    }

    internal fun likeClicked() {
        d("TAGGGG", "on like click")
//        ui.like.isEnabled = false
        if(isFeed){
            RxBus.publish(LikeFeedMeEvent(position))
            return
        }
        RxBus.publish(LikeEvent(position))
    }

    internal fun unLikeClicked() {
        d("TAGGGG", "on unlike click")
//        ui.unlike.isEnabled = false
        if(isFeed){
            RxBus.publish(UnlikeFeedMeEvent(position))
            return
        }
        RxBus.publish(UnlikeEvent(position))
    }

    internal fun updateLike() {
        ui.countLike.text = feed.likeCount.toString()
        if (feed.likeFlag == 1) {
            d("TAGGG", "like")
            ui.like.visibility = View.INVISIBLE
            ui.unlike.visibility = View.VISIBLE
            ui.unlike.isEnabled = true
        } else {
            ui.unlike.visibility = View.INVISIBLE
            ui.like.visibility = View.VISIBLE
            ui.like.isEnabled = true
            d("TAGGG", "unlike")
        }
    }
}
