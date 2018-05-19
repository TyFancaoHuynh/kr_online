package com.example.hoavot.karaokeonline.ui.profile

import android.view.View
import com.example.hoavot.karaokeonline.R
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.matchParent

/**
 *
 * @author at-hoavo
 */
class ProfileActivityUI : AnkoComponent<ProfileAcivity> {

    override fun createView(ui: AnkoContext<ProfileAcivity>): View {
        return with(ui) {
            frameLayout {
                lparams(matchParent, matchParent)
                id = R.id.profileActivityContainer
            }
        }
    }
}
