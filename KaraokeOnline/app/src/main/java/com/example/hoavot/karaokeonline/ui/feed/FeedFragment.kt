package com.example.hoavot.karaokeonline.ui.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hoavot.karaokeonline.data.model.other.Feed
import com.example.hoavot.karaokeonline.ui.base.BaseFragment
import org.jetbrains.anko.AnkoContext

/**
 *
 * @author at-hoavo.
 */
class FeedFragment : BaseFragment() {
    private lateinit var ui: FeedFragmentUI

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val feeds = mutableListOf<Feed>()
        feeds.add(Feed("", "sjhha", "", "vchcahcxhx", "", 1436.0, 86.0, mutableListOf()))
        feeds.add(Feed("", "sjhha", "", "vchcahcxhx", "", 1436.0, 86.0, mutableListOf()))
        feeds.add(Feed("", "sjhha", "", "vchcahcxhx", "", 1436.0, 86.0, mutableListOf()))
        feeds.add(Feed("", "sjhha", "", "vchcahcxhx", "", 1436.0, 86.0, mutableListOf()))
        feeds.add(Feed("", "sjhha", "", "vchcahcxhx", "", 1436.0, 86.0, mutableListOf()))
        feeds.add(Feed("", "sjhha", "", "vchcahcxhx", "", 1436.0, 86.0, mutableListOf()))
        feeds.add(Feed("", "sjhha", "", "vchcahcxhx", "", 1436.0, 86.0, mutableListOf()))
        feeds.add(Feed("", "sjhha", "", "vchcahcxhx", "", 1436.0, 86.0, mutableListOf()))
        ui = FeedFragmentUI(feeds)
        return ui.createView(AnkoContext.Companion.create(context, this))
    }

    override fun onBindViewModel() {
        //Todo: Handle  later
    }
}