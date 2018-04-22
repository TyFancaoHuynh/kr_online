package com.example.hoavot.karaokeonline.ui.feed.comment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hoavot.karaokeonline.data.model.other.Feed
import com.example.hoavot.karaokeonline.ui.base.BaseFragment
import com.example.hoavot.karaokeonline.ui.extensions.showAlertNotification
import org.jetbrains.anko.AnkoContext

/**
 *
 * @author at-hoavo.
 */
class CommentFragment : BaseFragment() {
    companion object {
        internal const val KEY_FEED = "feed"
        internal const val KEY_POSITION = "position"

        fun newInstance(feed: Feed, position: Int): CommentFragment {
            val instance = CommentFragment()
            instance.arguments = Bundle().apply {
                putParcelable(KEY_FEED, feed)
                putInt(KEY_POSITION, position)
            }
            return instance
        }
    }

    private lateinit var ui: CommentLayoutUI
    private lateinit var feed: Feed
    private var position = -1

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        feed = arguments.getParcelable<Feed>(KEY_FEED)
        position = arguments.getInt(KEY_POSITION)
        ui = CommentLayoutUI(feed.comments)
        return ui.createView(AnkoContext.Companion.create(context, this, false))
    }

    override fun onBindViewModel() {
        // Todo: Handle later
    }

    internal fun eventWhenAddCommentClicked(text: String) {
        if (text.isBlank()) {
            context.showAlertNotification("WARNING", "please input comment!") {}
        } else {

        }
    }
}