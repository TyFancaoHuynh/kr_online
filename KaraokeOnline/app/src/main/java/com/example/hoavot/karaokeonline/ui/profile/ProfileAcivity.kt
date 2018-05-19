package com.example.hoavot.karaokeonline.ui.profile

import android.os.Bundle
import com.example.hoavot.karaokeonline.R
import com.example.hoavot.karaokeonline.ui.base.BaseActivity
import com.example.hoavot.karaokeonline.ui.extensions.addFragment
import com.example.hoavot.karaokeonline.ui.extensions.animSlideInRightSlideOutRight
import org.jetbrains.anko.setContentView

/**
 *
 * @author at-hoavo
 */
class ProfileAcivity : BaseActivity() {
    companion object {
        const val KEY_USER_ID = "KEY_USER_ID"
    }

    private var userId = -1
    private lateinit var ui: ProfileActivityUI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = intent.getIntExtra(KEY_USER_ID, -1)
        ui = ProfileActivityUI()
        ui.setContentView(this)
        openProfileFragment()
    }

    internal fun openProfileFragment() {
        addFragment(R.id.profileActivityContainer, ProfileFragment.newInstance(userId, false),{
            it.animSlideInRightSlideOutRight()
        })
    }
}
