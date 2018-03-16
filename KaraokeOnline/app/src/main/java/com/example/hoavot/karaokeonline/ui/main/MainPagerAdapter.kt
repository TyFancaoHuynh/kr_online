package com.example.hoavot.karaokeonline.ui.main

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * MainPagerAdapter
 * @author at-hoavo
 */
class MainPagerAdapter(fragmentManager: FragmentManager, private val tabs: List<MainTab>)
    : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int)
            = tabs[position].getItem()

    override fun getCount() = tabs.size
}
