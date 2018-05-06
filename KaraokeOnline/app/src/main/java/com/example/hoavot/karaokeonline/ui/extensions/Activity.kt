package com.example.hoavot.karaokeonline.ui.extensions

import android.app.Activity
import android.content.Context
import android.os.Build
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentTransaction
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import com.example.hoavot.karaokeonline.R
import com.example.hoavot.karaokeonline.data.source.api.ApiException
import com.example.hoavot.karaokeonline.ui.base.BaseFragment
import io.reactivex.Notification
import org.jetbrains.anko.alert
import org.jetbrains.anko.dimen
import org.jetbrains.anko.yesButton
import java.net.HttpURLConnection

/**
 *
 * @author at-hoavo.
 */
internal fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
    inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
}

internal fun FragmentActivity.replaceFragment(@IdRes containerId: Int, fragment: Fragment,
                                              isAddBackStack: Boolean = false) {
    if (supportFragmentManager.findFragmentByTag(fragment.javaClass.simpleName) == null) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(containerId, fragment, fragment.javaClass.simpleName)
        if (isAddBackStack) {
            transaction.addToBackStack(fragment.javaClass.simpleName)
        }
        transaction.commit()
    }
}

internal fun FragmentActivity.addFragment(@IdRes containerId: Int, fragment: BaseFragment,
                                          t: (transaction: FragmentTransaction) -> Unit = {}, backStackString: String? = null) {
    if (supportFragmentManager.findFragmentByTag(fragment.javaClass.simpleName) == null) {
        val transaction = supportFragmentManager.beginTransaction()
        t.invoke(transaction)
        transaction.add(containerId, fragment, fragment.javaClass.simpleName)
        if (backStackString != null) {
            transaction.addToBackStack(backStackString)
        }
        transaction.commit()
        supportFragmentManager.executePendingTransactions()
    }
}

internal fun FragmentActivity.getCurrentFragment(@IdRes containerId: Int) = supportFragmentManager.findFragmentById(containerId)

internal fun FragmentActivity.popFragment() {
    supportFragmentManager.popBackStackImmediate()
}

internal fun FragmentTransaction.animSlideInBottomSlideOutBottom() {
    setCustomAnimations(R.anim.abc_slide_in_bottom, 0, 0, R.anim.abc_slide_out_bottom)
}

internal fun <T> Context.showAlertError(notification: Notification<T>) {
    val error = notification.error
    if (error is ApiException) {
        val apiException = error as? ApiException
        if (apiException?.statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
            // Do nothing
            return
        }
    }

    alert {
        title = getString(R.string.errorAlertTitle)
        isCancelable = false
        if (error is ApiException) {
            (error as? ApiException)?.let {
                message = it.messageError
            }
        } else {
            message = notification.error?.message ?: ""
        }
        yesButton {}
    }.show()
}

internal fun Context.showAlertUnAuthorized(apiException: ApiException, onYesClicked: () -> Unit) {
    alert {
        title = getString(R.string.errorAlertTitle)
        isCancelable = false
        apiException.let {
            message = it.messageError
        }
        yesButton {
            onYesClicked.invoke()
        }
    }.show()
}

internal fun Context.showAlertError(throwable: Throwable) {
    if (throwable is ApiException) {
        if (throwable.statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
            // Do nothing
            return
        }
    }
    alert {
        title = getString(R.string.errorAlertTitle)
        isCancelable = false
        if (throwable is ApiException) {
            (throwable as? ApiException)?.let {
                message = it.messageError
            }
        } else {
            message = throwable.message ?: ""
        }
        yesButton {}
    }.show()
}

internal fun Context.showAlertNotification(tt: String, content: String, onYesClick: () -> Unit) {
    alert {
        title = tt
        message = content
        yesButton {
            onYesClick()
        }
    }
}

internal fun Context.getStatusBarHeight(): Int {
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    return if (resourceId > 0)
        dimen(resourceId)
    else
        Math.ceil((if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            resources.getInteger(R.integer.defaultStatusBarLowerAndroidM)
        else R.integer.defaultStatusBarHigherAndroidM) * resources.displayMetrics.density.toDouble()).toInt()
}

internal fun Context.getNavigationBarHeight(): Int {
    val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
    if (resourceId > 0) {
        return resources.getDimensionPixelSize(resourceId)
    }
    return 0
}

internal fun Context.hasNavBar(): Boolean {
    val id = resources.getIdentifier("config_showNavigationBar", "bool", "android")
    return id > 0 && resources.getBoolean(id)
}

internal fun Context.getHeightScreen(): Int {
    val wm = getSystemService(Context.WINDOW_SERVICE) as? WindowManager
    val dimension = DisplayMetrics()
    wm?.defaultDisplay?.getMetrics(dimension)
    return dimension.heightPixels
}

internal fun Context.showKeyboard(view: View) {
    if (view.requestFocus()) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        view.postDelayed({
            imm?.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }, 100)
    }
}

internal fun Context.showSettingPermissionAlert(onSettingClicked: () -> Unit) {
    alert {
        message = getString(R.string.permissionSettingAlertTitle)
        isCancelable = false
        negativeButton("Cancel", {})
        positiveButton("Settings", {
            onSettingClicked.invoke()
        })
    }.show()
}
