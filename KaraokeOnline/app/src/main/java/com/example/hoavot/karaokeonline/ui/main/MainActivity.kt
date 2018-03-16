package com.example.hoavot.karaokeonline.ui.main

import android.os.Bundle
import com.example.hoavot.karaokeonline.ui.base.BaseActivity
import org.jetbrains.anko.setContentView

/**
 *
 * @author at-hoavo.
 */
class MainActivity : BaseActivity() {

    private lateinit var ui: MainActivityUI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Init MainTab
        val mainTabs = listOf(MainTab(MainTab.TabItemType.ITEM_HOME),
                MainTab(MainTab.TabItemType.ITEM_USER),
                MainTab(MainTab.TabItemType.ITEM_SEARCH),
                MainTab(MainTab.TabItemType.ITEM_VOICE))
        ui = MainActivityUI(mainTabs)
        ui.setContentView(this)
    }

    override fun onBindViewModel() {
        // Todo: Handle later
    }
}
