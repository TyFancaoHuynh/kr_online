package com.example.hoavot.karaokeonline.ui.feed.caption

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.*
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Parcelable
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.util.Log.d
import android.widget.EditText
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.hoavot.karaokeonline.R
import com.example.hoavot.karaokeonline.data.LocalRepository
import com.example.hoavot.karaokeonline.data.source.response.FeedResponse
import com.example.hoavot.karaokeonline.ui.base.BaseActivity
import com.example.hoavot.karaokeonline.ui.base.Image.convertBitmapToFile
import com.example.hoavot.karaokeonline.ui.extensions.observeOnUiThread
import com.example.hoavot.karaokeonline.ui.extensions.showAlertError
import com.example.hoavot.karaokeonline.ui.main.MainActivity
import com.example.hoavot.karaokeonline.ui.profile.ProfileFragment
import io.reactivex.Observable
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
    }

    private lateinit var ui: CaptionActivityUI
    private lateinit var viewModel: CaptionViewModel
    private lateinit var progressDialog: ProgressDialog
    private var file: File? = null
    private var path = ""
    private var fileName = ""
    private lateinit var dialogShowCamera: Dialog
    private var imageFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = CaptionActivityUI()
        ui.setContentView(this)
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
                val audioFileUri = data.data
                path = getPath(this, audioFileUri)!!
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

        if (resultCode == Activity.RESULT_OK && data != null && requestCode == ProfileFragment.TYPE_CAMERA) {
            val extras = data.extras
            if (extras != null) {
                val bimap = extras.getParcelable<Parcelable>("data") as Bitmap
                imageFile = convertBitmapToFile(bimap, this)
                val option = RequestOptions()
                        .override(ui.avatar.width, ui.avatar.width)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE) // https://github.com/bumptech/glide/issues/319
                        .placeholder(R.drawable.bg_play)

                Glide.with(this)
                        .load(imageFile)
                        .apply(option)
                        .into(ui.imgMusic)
                dialogShowCamera.cancel()
            }
        }

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
        dialogShowCamera = createDialog()
        dialogShowCamera.show()
    }

    // Create dialog with list data got from resource
    private fun createDialog(): Dialog {
        val builder = android.support.v7.app.AlertDialog.Builder(this)
        builder.setTitle(R.string.dialog_title_please_choose)
                .setItems(R.array.items, DialogInterface.OnClickListener { dialog, which ->
                    when (which) {
                        ProfileFragment.TYPE_GALLERY -> {
                            val intent = Intent(Intent.ACTION_PICK,
                                    MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                            intent.type = "image/*"
                            startActivityForResult(intent, ProfileFragment.TYPE_GALLERY)
                        }
                        else -> try {
                            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            startActivityForResult(cameraIntent, ProfileFragment.TYPE_CAMERA)
                        } catch (anfe: ActivityNotFoundException) {
                            val toast = Toast
                                    .makeText(this, "This device doesn't support the camera action!", Toast.LENGTH_SHORT)
                            toast.show()
                        }

                    }
                })
        return builder.create()
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

    fun getPath(context: Context, uri: Uri): String? {

        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }

                // TODO handle non-primary volumes
            } else if (isDownloadsDocument(uri)) {

                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)!!)

                return getDataColumn(context, contentUri, null, null)
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }

                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])

                return getDataColumn(context, contentUri, selection, selectionArgs)
            }// MediaProvider
            // DownloadsProvider
        } else if ("content".equals(uri.getScheme(), ignoreCase = true)) {
            return getDataColumn(context, uri, null, null)
        } else if ("file".equals(uri.getScheme(), ignoreCase = true)) {
            return uri.getPath()
        }// File
        // MediaStore (and general)

        return null
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    fun getDataColumn(context: Context, uri: Uri?, selection: String?,
                      selectionArgs: Array<String>?): String? {

        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val column_index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(column_index)
            }
        } finally {
            if (cursor != null)
                cursor.close()
        }
        return null
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.getAuthority()
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.getAuthority()
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.getAuthority()
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
