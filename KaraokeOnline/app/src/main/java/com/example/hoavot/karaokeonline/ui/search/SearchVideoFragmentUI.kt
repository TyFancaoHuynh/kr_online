package com.example.hoavot.karaokeonline.ui.search

import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import com.example.hoavot.karaokeonline.R
import com.example.hoavot.karaokeonline.data.model.nomal.Item
import com.example.hoavot.karaokeonline.ui.utils.ShowVideoAdapter
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.sdk25.coroutines.onEditorAction

/**
 *  Copyright © 2017 AsianTech inc.
 *  Created by hoavot on 12/12/2017.
 */
class SearchVideoFragmentUI(private val items: MutableList<Item>, private val context: Context) : AnkoComponent<SearchVideoFragment> {
    internal lateinit var edtInput: EditText
    internal var adapterSearch = ShowVideoAdapter(context, items, Color.BLACK)
    internal lateinit var recyclerView: RecyclerView

    override fun createView(ui: AnkoContext<SearchVideoFragment>): View = with(ui) {
        verticalLayout {
            lparams(matchParent, matchParent)
            linearLayout {
                lparams(matchParent, dip(50))
                imageView(R.drawable.ic_search_black) {
                }.lparams(dip(20), matchParent) {
                    leftMargin = dip(20)
                }

                edtInput = editText {
                    id = R.id.search_video_screen_edt_input
                    textSize = px2dip(35)
                    backgroundResource = R.drawable.custom_edittext_search_video
                    leftPadding = dip(10)
                    topPadding = dip(5)
                    hint = "Search..."
                    hintTextColor = ContextCompat.getColor(context, R.color.colorTextSearch)
                    imeOptions = EditorInfo.IME_ACTION_DONE
                    singleLine = true

                    onEditorAction { v, actionId, event ->
                        owner.eventWhenSearchButtonClick(text.toString())
                    }
                }.lparams(matchParent, dip(30)) {
                    horizontalMargin = dip(5)
                    verticalMargin = dip(10)
                }
            }

            recyclerView = recyclerView {
                layoutManager = LinearLayoutManager(context)
                adapter = adapterSearch
            }.lparams(matchParent, matchParent) {
                topMargin = dip(30)
            }
        }
    }
}
