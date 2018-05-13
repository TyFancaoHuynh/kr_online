package com.example.hoavot.karaokeonline.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hoavot.karaokeonline.R
import com.example.hoavot.karaokeonline.ui.base.BaseFragment
import com.example.hoavot.karaokeonline.ui.extensions.addChildFragment
import com.example.hoavot.karaokeonline.ui.extensions.animSlideInRightSlideOutRight
import com.example.hoavot.karaokeonline.ui.feed.FeedFragment
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.support.v4.ctx

/**
 *
 * @author at-hoavo.
 */
class HomeFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val ui = HomeFragmentUI()
        return ui.createView(AnkoContext.Companion.create(ctx, this, false))
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addChildFragment(R.id.homeFragmentContainer,FeedFragment(),null,{
            it.animSlideInRightSlideOutRight()
        })
    }

    override fun onBindViewModel() {
        // Todo:
    }
}
