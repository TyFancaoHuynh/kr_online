package com.example.hoavot.karaokeonline.ui.feed.share

import android.content.Intent
import android.os.Bundle
import android.widget.ShareActionProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
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
    }

    private lateinit var ui: ShareActivityUI
    private lateinit var fileMusic: String
    private lateinit var id: String
    private lateinit var viewModel: ShareViewModel
    private lateinit var shareDialog: ShareDialog
    private lateinit var shareActionProvider: ShareActionProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fileMusic = intent.getStringExtra(KEY_FILE_MUSIC)
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
        onBackPressed()
    }

    internal fun eventWhenShareClicked() {

    }
}
