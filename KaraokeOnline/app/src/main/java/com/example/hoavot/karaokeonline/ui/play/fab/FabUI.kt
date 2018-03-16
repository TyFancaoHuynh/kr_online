package com.example.hoavot.karaokeonline.ui.play.fab

import android.view.View
import com.example.hoavot.karaokeonline.R
import org.jetbrains.anko.*

/**
 *
 * @author at-hoavo.
 */
class FabUI : AnkoComponent<FabFragment> {
    override fun createView(ui: AnkoContext<FabFragment>): View = with(ui) {
        relativeLayout {
            imageView {
                id = R.id.fabImgMain
            }.lparams(0, dip(100)) {
                alignParentRight()
            }
            linearLayout {

                imageView {
                }

                imageView {

                }

                imageView {

                }

                imageView {

                }
            }.lparams(matchParent, dip(100)) {
                rightOf(R.id.fabImgMain)
            }

        }
    }
}
