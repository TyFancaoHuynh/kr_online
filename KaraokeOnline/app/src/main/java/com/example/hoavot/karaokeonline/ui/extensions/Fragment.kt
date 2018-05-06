package com.example.hoavot.karaokeonline.ui.extensions

import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.util.Log.d
import com.example.hoavot.karaokeonline.R
import com.example.hoavot.karaokeonline.ui.base.BaseFragment
import com.example.hoavot.karaokeonline.ui.profile.baseprofile.BaseProfileFragment

/**
 *
 * @author at-hoavo.
 */
internal fun Fragment.addChildFragment(@IdRes containerId: Int, fragment: BaseFragment, backStack: String? = null,
                                       t: (transaction: FragmentTransaction) -> Unit = {}) {
    if (childFragmentManager.findFragmentByTag(fragment.javaClass.simpleName) == null) {
//        (getCurrentFragment(containerId) as? BaseFragment)?.onMoveToNextScreen()
        val transaction = childFragmentManager.beginTransaction()
        t.invoke(transaction)
        transaction.add(containerId, fragment, fragment.javaClass.simpleName)
        if (backStack != null) {
            d("TAGGGG", "add backstack ${backStack}")
            transaction.addToBackStack(backStack)

            val count = (parentFragment as? BaseProfileFragment)?.childFragmentManager?.backStackEntryCount
            d("TAGGGG", "edit click 000 ${count}")
        }
        transaction.commit()
        childFragmentManager.executePendingTransactions()
    }
}

internal fun Fragment.replaceChildFragment(@IdRes containerId: Int, fragment: BaseFragment, backStack: String? = null,
                                           t: (transaction: FragmentTransaction) -> Unit = {}) {
    val transaction = childFragmentManager.beginTransaction()
    t.invoke(transaction)
    transaction.replace(containerId, fragment, backStack)
    if (backStack != null) {
        transaction.addToBackStack(backStack)
    }
    transaction.commit()
}

internal fun Fragment.getCurrentFragment(@IdRes containerId: Int) = childFragmentManager.findFragmentById(containerId)

internal fun FragmentTransaction.animFadeInFadeOut() {
    setCustomAnimations(R.anim.fade_in, 0, 0, R.anim.fade_out)
}

internal fun FragmentTransaction.animSlideInRightSlideOutRight() {
    setCustomAnimations(R.anim.slide_in_right, 0, 0, R.anim.slide_out_right)
}
