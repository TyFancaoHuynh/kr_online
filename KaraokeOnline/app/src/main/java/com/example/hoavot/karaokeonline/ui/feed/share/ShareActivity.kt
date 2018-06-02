package com.example.hoavot.karaokeonline.ui.feed.share

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.ShareActionProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.transition.Transition
import com.example.hoavot.karaokeonline.R
import com.example.hoavot.karaokeonline.data.LocalRepository
import com.example.hoavot.karaokeonline.ui.base.BaseActivity
import com.facebook.share.widget.ShareDialog
import org.jetbrains.anko.setContentView


/**
 *
 * @author at-hoavo
 */
class ShareActivity : BaseActivity() {
    companion object {
        internal const val KEY_FILE_MUSIC = "KEY_FILE_MUSIC"
        internal const val KEY_ID_FEED = "KEY_ID_FEED"
        internal const val KEY_ID_IMAGE_MUSIC = "KEY_ID_IMAGE_MUSIC"
        internal const val REQUEST_CODE_MUSIC = 11
    }

    private lateinit var ui: ShareActivityUI
    private lateinit var fileMusic: String
    private lateinit var imageMusic: String
    private lateinit var id: String
    private lateinit var viewModel: ShareViewModel
    private lateinit var shareDialog: ShareDialog
    private lateinit var shareActionProvider: ShareActionProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fileMusic = intent.getStringExtra(KEY_FILE_MUSIC)
        imageMusic = intent.getStringExtra(KEY_ID_IMAGE_MUSIC)
        id = intent.getStringExtra(KEY_ID_FEED)
        viewModel = ShareViewModel(LocalRepository(this))
        val user = viewModel.getUser()
        ui = ShareActivityUI(fileMusic, user)
        ui.setContentView(this)
        shareDialog = ShareDialog(this)
        val option = RequestOptions()
                .centerCrop()
                .override(ui.avatar.width, ui.avatar.width)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE) // https://github.com/bumptech/glide/issues/319
                .placeholder(R.drawable.bg_item_place_holder)

        Glide.with(this)
                .load(user.avatar)
                .apply(option)
                .into(ui.avatar)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        onBackPressed()
    }

    internal fun eventWhenShareClicked() {
        shareMusic()
    }

    private fun shareMusic() {
        val option = RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE) // https://github.com/bumptech/glide/issues/319
        Glide.with(this).asBitmap()
                .load(imageMusic)
                .apply(option)
                .into(object : BitmapImageViewTarget(ui.avatar) {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        val sharingIntent = Intent(Intent.ACTION_SEND)
                        val message = "My Song is ${fileMusic}"
                        sharingIntent.type = "text/plain"
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, message)
                        val url = "http://www.karafun.com/karaoke/?id=" + id + ""
                        sharingIntent.putExtra(Intent.EXTRA_TEXT, url)
                        startActivityForResult(Intent.createChooser(sharingIntent, "Share via"), REQUEST_CODE_MUSIC)
                    }

                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        super.onLoadFailed(errorDrawable)
                    }
                }
                )

    }
}
