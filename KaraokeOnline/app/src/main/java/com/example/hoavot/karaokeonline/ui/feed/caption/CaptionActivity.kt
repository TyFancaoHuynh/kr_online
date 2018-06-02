package com.example.hoavot.karaokeonline.ui.feed.caption

import android.app.Activity
import android.app.ProgressDialog
import android.content.CursorLoader
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.util.Log.d
import android.widget.EditText
import com.aditya.filebrowser.Constants
import com.aditya.filebrowser.FileChooser
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.hoavot.karaokeonline.R
import com.example.hoavot.karaokeonline.data.LocalRepository
import com.example.hoavot.karaokeonline.data.model.other.Feed
import com.example.hoavot.karaokeonline.data.source.response.FeedResponse
import com.example.hoavot.karaokeonline.ui.base.BaseActivity
import com.example.hoavot.karaokeonline.ui.base.Image
import com.example.hoavot.karaokeonline.ui.extensions.UriUtil
import com.example.hoavot.karaokeonline.ui.extensions.observeOnUiThread
import com.example.hoavot.karaokeonline.ui.extensions.showAlertError
import com.example.hoavot.karaokeonline.ui.extensions.touchHideKeyboardWithView
import com.example.hoavot.karaokeonline.ui.main.MainActivity
import com.example.hoavot.karaokeonline.ui.profile.ProfileFragment
import com.facebook.FacebookSdk
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.SingleSubject
import org.jetbrains.anko.*
import java.io.File
import java.util.concurrent.TimeUnit


/**
 *
 * @author at-hoavo.
 */
class CaptionActivity : BaseActivity() {

    companion object {
        private const val REQ_CODE_PICK_SOUNDFILE = 100
        private const val KEY_FROM_PROFILE = "KEY_FROM_PROFILE"
    }

    private lateinit var ui: CaptionActivityUI
    private lateinit var viewModel: CaptionViewModel
    private lateinit var progressDialog: ProgressDialog
    private var file: File? = null
    private var path = ""
    private var fileName = ""
    private var updateStartFromUpdateFeed: Feed? = null
    private var imageFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateStartFromUpdateFeed = intent.getSerializableExtra(KEY_FROM_PROFILE) as? Feed
        ui = CaptionActivityUI()
        ui.setContentView(this)
        touchHideKeyboardWithView(ui.rlParent){}
        initProgressDialog()
        viewModel = CaptionViewModel(LocalRepository(this))
        val user = viewModel.getUserFromSharePrefrence()
        if (user.username.isNotEmpty()) {
            ui.tvUsername.text = user.username
        } else {
            ui.tvUsername.text = "Your Name"
        }
        val option = RequestOptions()
                .override(ui.avatar.width, ui.avatar.width)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE) // https://github.com/bumptech/glide/issues/319
                .placeholder(R.drawable.ic_avatar_feed)

        Glide.with(this)
                .load(user.avatar)
                .apply(option)
                .into(ui.avatar)
        if (updateStartFromUpdateFeed != null) {
            initUpdateScreen()
        }
    }

    override fun onBindViewModel() {
        addDisposables(
                viewModel.progressObserver
                        .observeOnUiThread()
                        .subscribe(this::handleShowProgressDialog),
                viewModel.postFeedObserver
                        .observeOnUiThread()
                        .subscribe(this::handleWhenPostFeedSuccess)
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_CODE_PICK_SOUNDFILE && resultCode == Activity.RESULT_OK) {
            if (data != null && data.data != null) {
                val audioFileUri = UriUtil.getPath(this, data.data)
                d("TAGGGGGGG", "path0: ${audioFileUri}")

                path = audioFileUri!!
                alert {
                    title = "Bạn có muốn đổi tên bài hát?"
                    yesButton {
                        showEditSongDialog()
                    }
                    noButton {
                        file = File(path)
                        ui.tvFileName.text = path
                        fileName = path
                    }
                }.show()
            }
        }

        if (resultCode == Activity.RESULT_OK && data != null && requestCode == ProfileFragment.TYPE_GALLERY) {
            val uri: Uri = data.data as Uri
            getImageBitmap(uri.toString(), 1f)
                    .observeOnUiThread()
                    .subscribe({
                        imageFile = Image.convertBitmapToFile(it, this)
                        val option = RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.RESOURCE) // https://github.com/bumptech/glide/issues/319

                        Glide.with(this)
                                .load(imageFile)
                                .apply(option)
                                .into(ui.imgMusic)
                    }, {})
        }
    }

    private fun getAudioPath(uri: Uri): String {
        val data = arrayOf(MediaStore.Audio.Media.DATA)
        val loader = CursorLoader(applicationContext, uri, data, null, null, null)
        val cursor = loader.loadInBackground()
        val column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }

    private fun getImageBitmap(uri: String, ratio: Float): Single<Bitmap> {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(File(uri).absolutePath, options)
        val imageWidth = options.outWidth
        val imageHeight = options.outHeight
        val newW = (imageWidth * ratio).toInt()
        val newH = (imageHeight * ratio).toInt()
        val result: SingleSubject<Bitmap> = SingleSubject.create()
        val option = RequestOptions
                .overrideOf(newW, newH)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
        Glide.with(applicationContext)
                .asBitmap()
                .load(uri)
                .apply(option)
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        result.onSuccess(resource)
                    }
                })
        return result
    }

    internal fun eventOnBackClicked() {
        onBackPressed()
    }

    internal fun eventOnCompleteButtonClicked() {
        d("TAGGGG", "on completed")
        if (fileName.isBlank()) {
            showAlertError(Throwable("Bạn phải chọn bài hát"))
        } else {
            viewModel.postFeed(fileName, file, ui.edtCaption.text.toString(), imageFile)
        }
    }

    internal fun eventWhenClickedAddFileRecord() {
        val intent: Intent
        intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "audio/*"
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_audio_file_title)), REQ_CODE_PICK_SOUNDFILE)
    }

    internal fun eventOnCameraClick() {
        val i2 = Intent(FacebookSdk.getApplicationContext(), FileChooser::class.java)
        i2.putExtra(Constants.SELECTION_MODE, Constants.SELECTION_MODES.SINGLE_SELECTION.ordinal)
        startActivityForResult(i2, ProfileFragment.TYPE_GALLERY)
    }


    private fun initUpdateScreen() {
        ui.edtCaption.setText(updateStartFromUpdateFeed?.caption)
        ui.tvFileName.text = updateStartFromUpdateFeed?.fileMusicUserWrite
        val option = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE) // https://github.com/bumptech/glide/issues/319
                .placeholder(R.drawable.bg_play)

        Glide.with(this)
                .load(updateStartFromUpdateFeed?.imageFile)
                .apply(option)
                .into(ui.imgMusic)
    }

    private fun initProgressDialog() {
        progressDialog = ProgressDialog(this)
        progressDialog.setCancelable(false)
    }

    private fun handleShowProgressDialog(show: Boolean) {
        if (show) {
            progressDialog.show()
        } else {
            progressDialog.hide()
        }
    }

    private fun handleWhenPostFeedSuccess(feedResponse: FeedResponse) {
        startActivity<MainActivity>()
    }


    fun showEditSongDialog() {
        file = File(path)
        Observable
                .timer(2000, TimeUnit.MILLISECONDS)
                .observeOnUiThread()
                .subscribe({
                    var edtSongName: EditText? = null
                    alert {
                        title = "Nhập tên  bài hát..."
                        customView {
                            linearLayout {
                                edtSongName = editText {
                                    textSize = px2dip(dimen(R.dimen.textSize13))
                                    hint = "Add name..."
                                    hintTextColor = ContextCompat.getColor(context, R.color.colorGrayLight)
                                    horizontalPadding = dip(7)
                                    maxLines = 5
                                    backgroundResource = R.drawable.custom_edittext_comment
                                }.lparams(matchParent, matchParent) {
                                    horizontalMargin = dip(50)
                                    topMargin = dip(10)
                                }
                            }
                        }
                        positiveButton("OK") {
                            ui.tvFileName.text = edtSongName?.text.toString()
                            fileName = edtSongName?.text.toString().plus("." + path.substring(path.length - 3, path.length))
                        }

                        negativeButton("Cancel") {
                            ui.tvFileName.text = path
                            fileName = path
                        }
                    }.show()
                })

    }
}
