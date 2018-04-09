package com.example.hoavot.karaokeonline.ui.play.listsong

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.hoavot.karaokeonline.data.model.nomal.Item
import com.example.hoavot.karaokeonline.ui.utils.ShowVideoAdapter
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.verticalLayout

/**
 *
 * @author at-hoavo.
 */
class ListSongFragmentUI(private val items: MutableList<Item>, private val context: Context):AnkoComponent<ListSongFragment> {
    internal lateinit var recyclerviewListVideo: RecyclerView
    internal var adapterShowVideo = ShowVideoAdapter(context, items, Color.WHITE)

    override fun createView(ui: AnkoContext<ListSongFragment>): View =with(ui){
        verticalLayout {
            recyclerviewListVideo = recyclerView {
                backgroundColor = Color.BLACK
                layoutManager = LinearLayoutManager(context)
                adapter = adapterShowVideo
            }
        }
    }
}