package com.example.hoavot.karaokeonline.ui.profile.baseprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hoavot.karaokeonline.R
import com.example.hoavot.karaokeonline.data.LocalRepository
import com.example.hoavot.karaokeonline.ui.base.BaseFragment
import com.example.hoavot.karaokeonline.ui.extensions.addChildFragment
import com.example.hoavot.karaokeonline.ui.extensions.animSlideInRightSlideOutRight
import com.example.hoavot.karaokeonline.ui.profile.ProfileFragment
import org.jetbrains.anko.AnkoContext

/**
 *
 * @author at-hoavo.
 */
class BaseProfileFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val ui = BaseProfileFragmentUI()
        return ui.createView(AnkoContext.Companion.create(context, this, false))
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = LocalRepository(context).getMeInfor()
        addChildFragment(R.id.profileFragmentContainer, ProfileFragment.newInstance(user.id, true), ProfileFragment::class.java.name, {
            it.animSlideInRightSlideOutRight()
        })
    }

    override fun onBindViewModel() {
        // todo:
    }

    internal fun gotoPreviousScreen() {
        childFragmentManager.popBackStack()
    }
}
