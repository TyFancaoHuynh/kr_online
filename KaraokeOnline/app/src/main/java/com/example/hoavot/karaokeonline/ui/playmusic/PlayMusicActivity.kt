package com.example.hoavot.karaokeonline.ui.playmusic

import android.net.Uri
import android.os.Bundle
import android.util.Log.d
import com.example.hoavot.karaokeonline.ui.base.BaseActivity
import org.jetbrains.anko.setContentView

/**
 *
 * @author at-hoavo
 */
class PlayMusicActivity : BaseActivity() {
    companion object {
        const val PARAM_ID = "id"
    }

    private lateinit var ui: PlayMusicActivityUI
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = PlayMusicActivityUI()
        ui.setContentView(this)
        val uri: Uri? = intent.data
        uri?.let {
            val id = uri.getQueryParameter(PARAM_ID)
            d("TAGGGG", "id: ${id}")
        }
    }
}
