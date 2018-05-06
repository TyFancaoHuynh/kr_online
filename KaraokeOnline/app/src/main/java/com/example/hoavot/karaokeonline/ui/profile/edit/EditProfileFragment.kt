package com.example.hoavot.karaokeonline.ui.profile.edit

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hoavot.karaokeonline.R
import com.example.hoavot.karaokeonline.data.LocalRepository
import com.example.hoavot.karaokeonline.data.model.other.User
import com.example.hoavot.karaokeonline.ui.base.BaseFragment
import com.example.hoavot.karaokeonline.ui.extensions.*
import com.example.hoavot.karaokeonline.ui.profile.ProfileFragment
import com.example.hoavot.karaokeonline.ui.profile.baseprofile.BaseProfileFragment
import org.jetbrains.anko.AnkoContext

/**
 *
 * @author at-hoavo.
 */
class EditProfileFragment : BaseFragment() {

    private lateinit var ui: EditProfileFragmentUI
    private lateinit var viewModel: EditProfileViewModel
    private lateinit var user: User
    private lateinit var progressDialog: ProgressDialog

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = EditProfileViewModel(LocalRepository(context))
        initProgressDialog()
        user = viewModel.getMeInfor()
        ui = EditProfileFragmentUI(user)
        return ui.createView(AnkoContext.Companion.create(context, this, false))
    }

    override fun onBindViewModel() {
        addDisposables(
                viewModel.updateUserObserver
                        .observeOnUiThread()
                        .subscribe(this::handleUpdateUserSuccess, {
                            context.showAlertError(it)
                        })
        )
    }

    internal fun eventOnCompleteButtonClicked() {
        user.username = ui.nameUser.text.toString()
        user.password = ui.passWord.text.toString()
        user.email = ui.email.text.toString()
        viewModel.updateProfile(user)
    }

    internal fun eventOnBackClicked() {
        (parentFragment as? BaseProfileFragment)?.gotoPreviousScreen()
    }

    internal fun eventOnSaveCLicked() {
        if (ui.nameUser.text.toString().isBlank() || ui.passWord.text.toString().isBlank()) {
            if (ui.nameUser.text.toString().isBlank()) {
                context.showAlertError(Throwable("Tên người dùng không được để trống"))
            } else {
                context.showAlertError(Throwable("Mật khẩu không được để trống"))
            }
        } else {
            ui.nameUser.isEnabled = false
            ui.passWord.isEnabled = false
            ui.email.isEnabled = false
        }
    }

    internal fun eventOnEditClick() {
        ui.save.visibility = View.VISIBLE
        context.showKeyboard(ui.nameUser)
    }

    private fun initProgressDialog() {
        progressDialog = ProgressDialog(context)
        progressDialog.setCancelable(false)
    }

    private fun handleUpdateUserSuccess(user: User) {
        viewModel.saveUser(user)
        (parentFragment as? BaseProfileFragment)?.addChildFragment(R.id.profileFragmentContainer, ProfileFragment(), ProfileFragment::class.java.name, {
            it.animSlideInRightSlideOutRight()
        })
    }
}
