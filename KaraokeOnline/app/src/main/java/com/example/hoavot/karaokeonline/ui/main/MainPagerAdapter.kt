package com.example.hoavot.karaokeonline.ui.main

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.FragmentStatePagerAdapter

/**
 * MainPagerAdapter
 * @author at-hoavo
 */
class MainPagerAdapter(fragmentManager: FragmentManager, private val tabs: List<MainTab>)
    : FragmentStatePagerAdapter(fragmentManager) {

    override fun getItem(position: Int)
            = tabs[position].getItem()

    override fun getCount() = tabs.size
}
