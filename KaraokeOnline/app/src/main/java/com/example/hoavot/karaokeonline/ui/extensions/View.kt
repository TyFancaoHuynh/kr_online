package com.example.hoavot.karaokeonline.ui.extensions

import android.view.ViewGroup
import com.github.aakira.expandablelayout.ExpandableWeightLayout
import com.github.siyamed.shapeimageview.CircularImageView
import org.jetbrains.anko.custom.ankoView

/**
 *
 * @author at-hoavo.
 */
internal fun ViewGroup.roundImage(init: CircularImageView.() -> Unit): CircularImageView {
    return ankoView({ CircularImageView(it) }, init = init, theme = 0)
}

internal fun ViewGroup.expandleLayout(init: ExpandableWeightLayout.() -> Unit): ExpandableWeightLayout {
    return ankoView({ ExpandableWeightLayout(it) }, init = init, theme = 0)
}
