package com.example.hoavot.karaokeonline.ui.home

import android.view.View
import com.example.hoavot.karaokeonline.R
import org.jetbrains.anko.*

/**
 *
 * @author at-hoavo.
 */
class HomeFragmentUI : AnkoComponent<HomeFragment> {
    override fun createView(ui: AnkoContext<HomeFragment>): View {
        return with(ui) {
            linearLayout {
                lparams(matchParent, matchParent)
                frameLayout {
                    id = R.id.homeFragmentContainer
                }.lparams(matchParent, matchParent)
            }
        }
    }

}
