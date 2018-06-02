package com.example.hoavot.karaokeonline.ui.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Handler
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.util.Log.d
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewManager
import com.example.hoavot.karaokeonline.R
import com.example.hoavot.karaokeonline.data.model.other.*
import com.example.hoavot.karaokeonline.ui.custom.nonSwipeAbleViewPager
import com.example.hoavot.karaokeonline.ui.extensions.RxBus
import com.example.hoavot.karaokeonline.ui.feed.SongFeedService
import com.example.hoavot.karaokeonline.ui.home.HomeFragment
import com.example.hoavot.karaokeonline.ui.playmusic.PlayFragment
import com.example.hoavot.karaokeonline.ui.playmusic.service.Action
import com.example.hoavot.karaokeonline.ui.playmusic.service.SongService
import com.example.hoavot.karaokeonline.ui.profile.baseprofile.BaseProfileFragment
import org.jetbrains.anko.*
import org.jetbrains.anko.design.tabLayout

/**
 *
 * @author at-hoavo.
 */
class MainActivityUI(private val mainTabs: List<MainTab>)
    : AnkoComponent<MainActivity> {
    companion object {
        private const val ROTATION_X = 180f
    }

    internal lateinit var viewPager: ViewPager
    internal lateinit var tabLayout: TabLayout
    internal lateinit var mainPagerAdapter: MainPagerAdapter
    private var lastTab = -1

    override fun createView(ui: AnkoContext<MainActivity>): View = with(ui) {
        mainPagerAdapter = MainPagerAdapter(owner.supportFragmentManager, mainTabs)
        relativeLayout {
            lparams(matchParent, matchParent)
            backgroundColor = Color.WHITE
            tabLayout = tabLayout {
                backgroundColor = Color.WHITE
                rotationX = ROTATION_X
                id = R.id.mainTabLayout
                setSelectedTabIndicatorColor(ContextCompat.getColor(ctx, R.color.colorStatus))

                addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                    override fun onTabReselected(tab: TabLayout.Tab?) {
                    }

                    override fun onTabUnselected(tab: TabLayout.Tab?) {

                    }

                    override fun onTabSelected(tab: TabLayout.Tab?) {
                        (tab?.tag as? Int)?.let {
                            viewPager.setCurrentItem(it, true)
                            if (lastTab != -1) {
                                val fragment = mainPagerAdapter.instantiateItem(viewPager, viewPager.currentItem) as Fragment
                                sendIntent(owner, Action.STOP_MEDIA.value, context, SongFeedService::class.java)
                                Handler().postDelayed({
                                    when (fragment) {
                                        is HomeFragment -> {
                                            RxBus.publish(LoadDataFeed())
//                                            RxBus.publish(StopBRProfileEvent())
//                                            RxBus.publish(RegisterBRFeedEvent())
                                        }
                                        is BaseProfileFragment -> {
                                            RxBus.publish(LoadDataFeedMe())
//                                            RxBus.publish(StopBRFeedEvent())
//                                            RxBus.publish(RegisterBRProfileEvent())
                                        }
                                    }
                                }, 300)
                            }
                            lastTab = it
                            sendIntent(owner, Action.STOP_MEDIA.value, context, SongFeedService::class.java)
                        }
                    }
                })
            }.lparams(matchParent, dimen(R.dimen.tabLayoutHeight)) {
                alignParentBottom()
            }
            viewPager = nonSwipeAbleViewPager {
                id = R.id.mainViewPager
                adapter = mainPagerAdapter
            }.lparams(matchParent, matchParent) {
                above(R.id.mainTabLayout)
            }
            // Init Tab Item
            for (i in 0 until mainTabs.size) {
                val tab = tabLayout.newTab()
                tab.customView = tabItem(i)
                tab.tag = i
                tabLayout.addTab(tab)
            }
            reduceMarginsInTabs(tabLayout, dimen(R.dimen.tabLayoutItemExternalMargin),
                    dimen(R.dimen.tabLayoutItemHorizontalMargin))
        }
    }

    private fun ViewManager.tabItem(pos: Int) = relativeLayout {
        gravity = Gravity.CENTER
        lparams(dimen(R.dimen.tabLayoutItemWidth), dimen(R.dimen.tabLayoutItemWidth))
        imageView(mainTabs[pos].itemType.icon) {
            rotationX = ROTATION_X
        }
    }

    private fun reduceMarginsInTabs(tabLayout: TabLayout, externalMargin: Int, internalMargin: Int) {
        tabLayout.getChildAt(0)?.let {
            if (it is ViewGroup) {
                reduceMarginsViewGroup(it, externalMargin, internalMargin)
                tabLayout.requestLayout()
            }
        }
    }

    private fun reduceMarginsViewGroup(tabTrip: ViewGroup, externalMargin: Int, internalMargin: Int) {
        tabTrip.let {
            for (i in 0 until it.childCount) {
                val tabView = tabTrip.getChildAt(i)
                reduceMarginsTabView(tabView, i, it.childCount, externalMargin, internalMargin)
            }
        }
    }

    private fun reduceMarginsTabView(view: View, i: Int, count: Int, externalMargin: Int, internalMargin: Int) {
        view.minimumWidth = 0
        view.setPadding(0, view.paddingTop, 0, view.paddingBottom)
        view.backgroundDrawable = null
        if (view.layoutParams is ViewGroup.MarginLayoutParams) {
            val layoutParams = view.layoutParams as? ViewGroup.MarginLayoutParams
            layoutParams?.let {
                when (i) {
                    0 -> settingMargin(it, externalMargin, internalMargin) // Left
                    count - 1 -> settingMargin(it, internalMargin, externalMargin) // Right
                    else -> settingMargin(it, internalMargin, internalMargin) // Internal
                }
            }
        }
    }

    private fun settingMargin(layoutParams: ViewGroup.MarginLayoutParams, start: Int, end: Int) {
        layoutParams.leftMargin = start
        layoutParams.rightMargin = end
    }

    private fun sendIntent(owner: Activity, action: String, context: Context, nameService: Class<*>) {
        val intent = Intent(context, nameService)
        intent.action = action
        owner.startService(intent)
    }
}
