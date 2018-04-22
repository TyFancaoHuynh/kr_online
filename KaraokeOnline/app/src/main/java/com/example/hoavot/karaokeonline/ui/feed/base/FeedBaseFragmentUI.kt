package com.example.hoavot.karaokeonline.ui.feed.base

import android.view.View
import com.example.hoavot.karaokeonline.R
import org.jetbrains.anko.*

/**
 *
 * @author at-hoavo.
 */
class FeedBaseFragmentUI : AnkoComponent<FeedBaseFragment> {
    override fun createView(ui: AnkoContext<FeedBaseFragment>): View = with(ui) {
        linearLayout {
            lparams(matchParent, matchParent)
            frameLayout {
                id = R.id.feedFragmentBase
            }
        }
    }

}