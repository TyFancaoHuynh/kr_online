package com.example.hoavot.karaokeonline.ui.feed.caption

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hoavot.karaokeonline.ui.base.BaseFragment

/**
 *
 * @author at-hoavo.
 */
class CaptionFragment : BaseFragment() {

    companion object {

        internal fun newInstance(): CaptionFragment {
            return CaptionFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onBindViewModel() {
        //Todo:Handle later
    }

    internal fun eventOnBackClicked() {

    }

    internal fun eventOnCompleteButtonClicked() {

    }

    internal fun eventSearchTextChange() {

    }
}