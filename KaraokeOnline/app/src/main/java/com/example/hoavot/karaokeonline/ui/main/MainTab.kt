package com.example.hoavot.karaokeonline.ui.main

import android.support.annotation.DrawableRes
import android.support.v4.app.Fragment
import com.example.hoavot.karaokeonline.R
import com.example.hoavot.karaokeonline.ui.feed.FeedFragment
import com.example.hoavot.karaokeonline.ui.home.HomeFragment
import com.example.hoavot.karaokeonline.ui.search.SearchVideoFragment

/**
 * MainTab
 * @author at-hoavo
 */
class MainTab(val itemType: TabItemType) {

    /**
     * TabItemType
     */
    enum class TabItemType(@DrawableRes val icon: Int, @DrawableRes val iconSelected: Int) {
        /**
         * First Item On Tab
         */
        ITEM_HOME(R.drawable.ic_home_black_24dp, R.drawable.ic_home_pink),

        /**
         * Second Item On Tab
         */
        ITEM_USER(R.drawable.ic_perm_identity_black_24dp, R.drawable.ic_perm_identity_pink_800_24dp),

        /**
         * Third Item On Tab
         */
        ITEM_SEARCH(R.drawable.ic_search_black_24dp, R.drawable.ic_search_pink_800_24dp),

        /**
         * Fourth Item On Tab
         */
        ITEM_VOICE(R.drawable.ic_settings_voice_black_24dp, R.drawable.ic_settings_voice_pink_800_24dp)
    }

    /**
     * Method return item of tab each position
     */
    class NonSwipeAbleViewPager {
    }

    fun getItem(): Fragment? = when (itemType) {
        TabItemType.ITEM_HOME -> FeedFragment()
//        TabItemType.ITEM_USER -> FeedFragment.newInstance()
        TabItemType.ITEM_SEARCH -> SearchVideoFragment()
        else -> HomeFragment()
    }
}
