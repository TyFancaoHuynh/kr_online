package com.example.hoavot.karaokeonline.ui.profile.baseprofile

import android.view.View
import com.example.hoavot.karaokeonline.R
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.matchParent

/**
 *
 * @author at-hoavo.
 */
class BaseProfileFragmentUI : AnkoComponent<BaseProfileFragment> {
    override fun createView(ui: AnkoContext<BaseProfileFragment>): View {
        return with(ui) {
            frameLayout {
                id = R.id.profileFragmentContainer
                lparams(matchParent, matchParent)
            }
        }
    }

}
