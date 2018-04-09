package com.example.hoavot.karaokeonline.ui.profile

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
class ProfileFragment : BaseFragment() {
    private lateinit var ui: ProfileFragmentUI

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
        ui = ProfileFragmentUI(feeds)
        return ui.createView(AnkoContext.Companion.create(context, this))
    }

    override fun onBindViewModel() {
        //Todo: Handle  later
    }

    internal fun handleWhenArrowRightClick() {
        ui.expandlelayout.isExpanded = true
        ui.arrowRight.visibility = View.INVISIBLE
        ui.arrowDown.visibility = View.VISIBLE
    }

    internal fun handleWhenArrowDownClick() {
        ui.expandlelayout.isExpanded = false
        ui.arrowRight.visibility = View.VISIBLE
        ui.arrowDown.visibility = View.INVISIBLE
    }
}