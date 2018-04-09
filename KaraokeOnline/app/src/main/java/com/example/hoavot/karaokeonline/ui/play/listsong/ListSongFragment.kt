package com.example.hoavot.karaokeonline.ui.play.listsong

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hoavot.karaokeonline.data.model.nomal.Item
import com.example.hoavot.karaokeonline.ui.base.BaseFragment
import org.jetbrains.anko.AnkoContext
import java.util.*

/**
 *
 * @author at-hoavo.
 */
class ListSongFragment : BaseFragment() {
    private lateinit var ui: ListSongFragmentUI

    companion object {
        internal const val KEY_ITEMS = "items"

        internal fun newInstance(items: MutableList<Item>): ListSongFragment {
            val instance = ListSongFragment()
            instance.arguments = Bundle().apply {
                putParcelableArrayList(KEY_ITEMS, items as? ArrayList<out Parcelable>)
            }
            return instance
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val items = arguments.getParcelableArrayList<Item>(KEY_ITEMS)
        ui = ListSongFragmentUI(items, context)
        return ui.createView(AnkoContext.Companion.create(context, this, false))
    }

    override fun onBindViewModel() {
        // Todo: Handle later
    }

}