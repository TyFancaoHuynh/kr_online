package com.example.hoavot.karaokeonline.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hoavot.karaokeonline.ui.base.BaseFragment
import org.jetbrains.anko.AnkoContext

/**
 *
 * @author at-hoavo.
 */
class EditProfileFragment : BaseFragment() {

    private lateinit var ui: EditProfileFragmentUI

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        ui = EditProfileFragmentUI()
        return ui.createView(AnkoContext.Companion.create(context, this, false))
    }

    override fun onBindViewModel() {
        // Todo:  Handle later
    }
}
