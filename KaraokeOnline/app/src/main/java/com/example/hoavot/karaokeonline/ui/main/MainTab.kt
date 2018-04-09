package com.example.hoavot.karaokeonline.ui.main

import android.support.annotation.DrawableRes
import android.support.v4.app.Fragment
import com.example.hoavot.karaokeonline.R
import com.example.hoavot.karaokeonline.ui.feed.FeedFragment
import com.example.hoavot.karaokeonline.ui.play.record.RecordFragment
import com.example.hoavot.karaokeonline.ui.profile.ProfileFragment
import com.example.hoavot.karaokeonline.ui.records.RecordsFragment
import com.example.hoavot.karaokeonline.ui.search.SearchVideoFragment

/**
 * MainTab
 * @author at-hoavo
 */
class MainTab(val itemType: TabItemType) {

    /**
     * TabItemType
     */
    enum class TabItemType(@DrawableRes val icon: Int) {
        /**
         * First Item On Tab
         */
        ITEM_HOME(R.drawable.ic_home_black_24dp),

        /**
         * Second Item On Tab
         */
        ITEM_USER(R.drawable.ic_perm_identity_black_24dp),

        /**
         * Third Item On Tab
         */
        ITEM_SEARCH(R.drawable.ic_search_black_24dp),

        /**
         * Fourth Item On Tab
         */
        ITEM_VOICE(R.drawable.ic_settings_voice_black_24dp)
    }

    /**
     * Method return item of tab each position
     */
    class NonSwipeAbleViewPager {
    }

    fun getItem(): Fragment? = when (itemType) {
        TabItemType.ITEM_HOME -> FeedFragment()
        TabItemType.ITEM_USER -> ProfileFragment()
        TabItemType.ITEM_SEARCH -> SearchVideoFragment()
        else -> RecordsFragment()
    }
}
