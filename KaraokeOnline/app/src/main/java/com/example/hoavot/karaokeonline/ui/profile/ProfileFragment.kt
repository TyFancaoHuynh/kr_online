package com.example.hoavot.karaokeonline.ui.profile

import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import android.support.v7.app.AlertDialog
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.hoavot.karaokeonline.R
import com.example.hoavot.karaokeonline.data.LocalRepository
import com.example.hoavot.karaokeonline.data.model.other.Feed
import com.example.hoavot.karaokeonline.data.model.other.User
import com.example.hoavot.karaokeonline.ui.base.BaseFragment
import com.example.hoavot.karaokeonline.ui.extensions.addChildFragment
import com.example.hoavot.karaokeonline.ui.extensions.animSlideInRightSlideOutRight
import com.example.hoavot.karaokeonline.ui.extensions.observeOnUiThread
import com.example.hoavot.karaokeonline.ui.profile.baseprofile.BaseProfileFragment
import com.example.hoavot.karaokeonline.ui.profile.edit.EditProfileFragment
import org.jetbrains.anko.AnkoContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*


/**
 *
 * @author at-hoavo.
 */
class ProfileFragment : BaseFragment() {
    companion object {
        private const val TYPE_GALLERY = 0
        private const val TYPE_CAMERA = 1
    }

    private lateinit var ui: ProfileFragmentUI
    private lateinit var viewModel: ProfileViewModel
    private lateinit var user: User
    private lateinit var dialogShowCamera: Dialog
    private var feeds = mutableListOf<Feed>()


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        viewModel = ProfileViewModel(LocalRepository(context))
        user = viewModel.getMeInfor()
        ui = ProfileFragmentUI(feeds, user)
        return ui.createView(AnkoContext.Companion.create(context, this))
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val option = RequestOptions()
                .centerCrop()
                .override(ui.avatar.width, ui.avatar.width)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE) // https://github.com/bumptech/glide/issues/319
                .placeholder(R.drawable.user_default)
        d("TAGGGG", "view created profile")
        Glide.with(context)
                .load(user.avatar)
                .apply(option)
                .into(ui.avatar)
        ui.username.text = user.username
        ui.age.text = user.age.toString()
    }

    override fun onBindViewModel() {
        viewModel.getMeFeeds()
        addDisposables(
                viewModel.feedMeObserver
                        .observeOnUiThread()
                        .subscribe({
                            feeds.clear()
                            feeds.addAll(it)
                            ui.feedAdapter.notifyDataSetChanged()
                            ui.countFeed.text = it.size.toString()
                        })
        )
    }

    internal fun onMoreClick() {
        ui.editProfile.visibility = View.VISIBLE
    }

    internal fun eventOnCameraClick() {
        dialogShowCamera = createDialog()
        dialogShowCamera.show()
    }

    internal fun handleWhenEditProfileClick() {
        ui.editProfile.visibility = View.GONE
        val editProfileFragment = EditProfileFragment()
        (parentFragment as? BaseProfileFragment)?.addChildFragment(R.id.profileFragmentContainer, editProfileFragment, EditProfileFragment::class.java.name, {
            it.animSlideInRightSlideOutRight()
        })
    }

    // Create dialog with list data got from resource
    private fun createDialog(): Dialog {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.dialog_title_please_choose)
                .setItems(R.array.items, DialogInterface.OnClickListener { dialog, which ->
                    when (which) {
                        TYPE_GALLERY -> {
                            val intent = Intent(Intent.ACTION_PICK,
                                    MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                            intent.type = "image/*"
                            startActivityForResult(intent, TYPE_GALLERY)
                        }
                        else -> try {
                            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            startActivityForResult(cameraIntent, TYPE_CAMERA)
                        } catch (anfe: ActivityNotFoundException) {
                            val toast = Toast
                                    .makeText(context, "This device doesn't support the camera action!", Toast.LENGTH_SHORT)
                            toast.show()
                        }

                    }
                })
        return builder.create()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK && data != null) {
            val extras = data.extras
            if (extras != null) {
                val bimap = extras.getParcelable<Parcelable>("data") as Bitmap
                val avatarFile = convertBitmapToFile(bimap)
                viewModel.updateAvatar(avatarFile)
                        .observeOnUiThread()
                        .subscribe(
                                this::handleWhenUpdateAvatarSuccess,
                                {
                                    // Todo: Handle later
                                })
                dialogShowCamera.cancel()
            }
        }
    }

    private fun convertBitmapToFile(bitmap: Bitmap): File {
        val f = File(context.cacheDir, "avatar" + Date().time)
        f.createNewFile()

        //Convert bitmap to byte array
        val bos = ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        val bitmapdata = bos.toByteArray()

        //write the bytes in file
        val fos = FileOutputStream(f);
        fos.write(bitmapdata);
        fos.flush()
        fos.close()
        return f
    }

    private fun handleWhenUpdateAvatarSuccess(user: User) {
        viewModel.saveUser(user)
        val option = RequestOptions()
                .centerCrop()
                .override(ui.avatar.width, ui.avatar.width)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE) // https://github.com/bumptech/glide/issues/319
                .placeholder(R.drawable.bg_item_place_holder)
        Glide.with(context)
                .load(user.avatar)
                .apply(option)
                .into(ui.avatar)
    }
}
