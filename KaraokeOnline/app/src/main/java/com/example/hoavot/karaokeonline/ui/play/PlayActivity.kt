package com.example.hoavot.karaokeonline.ui.play

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.hoavot.karaokeonline.R
import com.example.hoavot.karaokeonline.data.model.nomal.Item
import com.example.hoavot.karaokeonline.ui.base.BaseActivity
import com.example.hoavot.karaokeonline.ui.extensions.getDayPublish
import com.example.hoavot.karaokeonline.ui.extensions.toViewCountVideo
import com.example.hoavot.karaokeonline.ui.utils.ShowVideoAdapter
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import org.jetbrains.anko.longToast
import org.jetbrains.anko.setContentView
import org.jetbrains.anko.toast

/**
 *
 * @author at-hoavo.
 */
class PlayActivity : BaseActivity() {

    companion object {
        private const val API_KEY = "AIzaSyAjEu7QRhmJUAHHJ2EODfmTjhLc6dkZ2Eg"
        internal const val TYPE_VIDEO = "type video"
    }

    private lateinit var ui: PlayActivityUI
    private val items = mutableListOf<Item>()
    private lateinit var viewModel: PlayActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = PlayActivityUI(items, this)
        ui.setContentView(this)
        val item = intent.getParcelableExtra<Item>(TYPE_VIDEO)
        item?.let {
            initPlayer(it.video.id.videoId)
            loadDataForPlayVideo(it)
        }
        viewModel = PlayActivityViewModel(item?.video?.id?.videoId!!)
        ui.adapterShowVideo.onItemClick = { video, type ->
            handleItemVideoOnClick(video, type)
        }
    }

    override fun onBindViewModel() {
        addDisposables(
                viewModel
                        .itemsObserver
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                this::handleSearchMoreVideoSuccess,
                                this::handleSearchMoreVideoError
                        )
        )
    }

    internal fun eventOnClickItemMenu(view: View) {
        when (view) {
//            ui.fabMenuGroup.imgBtnDownload -> startActivity<RegisterActivity>()
//            ui.fabMenuGroup.imgBtnRecord -> startActivity<GroupActivity>()
//            ui.fabMenuGroup.imgBtnSearch -> startActivity<SearchActivity>()
        }
    }

    private fun initPlayer(videoId: String) {
        val youtubeFragmentPlayer = YouTubePlayerSupportFragment.newInstance()
        supportFragmentManager.beginTransaction().replace(R.id.play_activity_framelayout_play, youtubeFragmentPlayer).commit()
        youtubeFragmentPlayer.initialize(API_KEY, object : YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(a0: YouTubePlayer.Provider?, a1: YouTubePlayer?, a2: Boolean) {
                if (!a2) {
                    a1?.setShowFullscreenButton(true)
                    a1?.loadVideo(videoId)
                }
            }

            override fun onInitializationFailure(a0: YouTubePlayer.Provider?, a1: YouTubeInitializationResult?) {
                longToast("Can't load video, please check your internet!")
            }
        })
    }

    private fun loadDataForPlayVideo(item: Item) {
        ui.nameVideo.text = item.snippet.title
        ui.channelName.text = item.snippet.channelTitle
        Glide.with(this)
                .load(item.snippet.thumbnails.medium.url)
                .into(ui.thumnailVideo)
        ui.like.text = item.statistics?.likeCount
        ui.dislike.text = item.statistics?.dislikeCount
        ui.dayPublish.text = item.snippet.publishedAt.getDayPublish()
        ui.viewCount.text = item.statistics?.viewCount?.toViewCountVideo()
    }

    private fun handleSearchMoreVideoSuccess(itemVideos: MutableList<Item>) {
        items.clear()
        items.addAll(itemVideos)
        ui.adapterShowVideo.notifyDataSetChanged()
    }

    private fun handleSearchMoreVideoError(error: Throwable) {
        toast("error load more")
    }

    private fun handleItemVideoOnClick(item: Item, type: Int) {
        when (type) {
            ShowVideoAdapter.TYPE_VIDEO -> {
                initPlayer(item.video.id.videoId)
                loadDataForPlayVideo(item)
                viewModel.videoId = item.video.id.videoId
                viewModel.loadMoreVideo()
            }

        //Todo: Handle for another type
        }
    }
}
