package com.example.hoavot.karaokeonline.ui.feed.share

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log.d
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.hoavot.karaokeonline.R
import com.example.hoavot.karaokeonline.data.LocalRepository
import com.example.hoavot.karaokeonline.ui.base.BaseActivity
import com.facebook.share.model.ShareLinkContent
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
    }

    private lateinit var ui: ShareActivityUI
    private lateinit var fileMusic: String
    private lateinit var id: String
    private lateinit var viewModel: ShareViewModel
    private lateinit var shareDialog: ShareDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fileMusic = intent.getStringExtra(KEY_FILE_MUSIC)
        d("TAGGGGGG", "file Music: ${fileMusic}")
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
        finish()
        onBackPressed()
    }

    internal fun eventWhenLoginFaceBook() {
        d("TAGGGGG", "on face book click")
        if (ShareDialog.canShow(ShareLinkContent::class.java)) {
            val linkContent = ShareLinkContent.Builder()
                    .setContentTitle("Android Facebook Integration and Login Tutorial")
                    .setContentDescription(
                            "This tutorial explains how to integrate Facebook and Login through Android Application")
                    .setContentUrl(Uri.parse("http://" + fileMusic + "/id=" + id))
                    .build()
            shareDialog.show(linkContent);  // Show facebook ShareDialog
        }
    }
}
