package com.example.hoavot.karaokeonline.ui.profile

import android.os.Bundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hoavot.karaokeonline.R
import com.example.hoavot.karaokeonline.data.model.other.Feed
import com.example.hoavot.karaokeonline.ui.base.BaseFragment
import com.example.hoavot.karaokeonline.ui.extensions.addChildFragment
import com.example.hoavot.karaokeonline.ui.extensions.animSlideInRightSlideOutRight
import com.example.hoavot.karaokeonline.ui.profile.baseprofile.BaseProfileFragment
import org.jetbrains.anko.AnkoContext

/**
 *
 * @author at-hoavo.
 */
class ProfileFragment : BaseFragment() {
    private lateinit var ui: ProfileFragmentUI

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val feeds = mutableListOf<Feed>()
        ui = ProfileFragmentUI(feeds)
        return ui.createView(AnkoContext.Companion.create(context, this))
    }

    override fun onBindViewModel() {
        //Todo: Handle  later
    }

    internal fun onMoreClick() {
        ui.editProfile.visibility = View.VISIBLE
    }

    internal fun handleWhenEditProfileClick() {
        d("TAGGGG", "edit click")
        (parentFragment as? BaseProfileFragment)?.addChildFragment(R.id.profileFragmentContainer, EditProfileFragment(), null, {
            it.animSlideInRightSlideOutRight()
        })
    }
}
