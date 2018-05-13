package com.example.hoavot.karaokeonline.ui.base.feed

import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Parcelable
import android.support.v7.util.DiffUtil
import android.util.Log
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.hoavot.karaokeonline.R
import com.example.hoavot.karaokeonline.data.LocalRepository
import com.example.hoavot.karaokeonline.data.model.other.*
import com.example.hoavot.karaokeonline.ui.base.BaseFragment
import com.example.hoavot.karaokeonline.ui.extensions.RxBus
import com.example.hoavot.karaokeonline.ui.extensions.observeOnUiThread
import com.example.hoavot.karaokeonline.ui.feed.SongFeedService
import com.example.hoavot.karaokeonline.ui.feed.comment.CommentFragment
import com.example.hoavot.karaokeonline.ui.feed.comment.CommentLayoutUI
import com.example.hoavot.karaokeonline.ui.feed.share.ShareActivity
import com.example.hoavot.karaokeonline.ui.main.MainActivity
import com.example.hoavot.karaokeonline.ui.playmusic.model.Song
import com.example.hoavot.karaokeonline.ui.playmusic.service.Action
import io.reactivex.Notification
import org.jetbrains.anko.AnkoContext
import java.util.ArrayList

/**
 *
 * @author at-hoavo
 */
class BaseFeedFragment : BaseFragment() {
    companion object {
        val TYPE_SONGS = "Songs"
        const val TYPE_IS_FEED_ME = "TYPE_IS_FEED"
        fun newInstance(isFeedMe: Boolean): BaseFeedFragment {
            return BaseFeedFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(TYPE_IS_FEED_ME, isFeedMe)
                }

            }
        }
    }

    internal lateinit var ui: BaseFeedFragmentUI
    private var feeds = mutableListOf<Feed>()
    private lateinit var viewModel: BaseFeedViewModel
    private lateinit var progressDialog: ProgressDialog
    private lateinit var bottomSheetDialogComment: CommentFragment
    private var isLikeFromCommentScreen = false
    private lateinit var animationRotate: Animation
    private var myBroadcastFeed = MyBroadcastFeed()
    private var mSongs = mutableListOf<Song>()
    internal var isPlaying = false
    private lateinit var user: User
    private var isFeedMe = false
    private val option = RequestOptions()
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE) // https://github.com/bumptech/glide/issues/319
            .placeholder(R.drawable.user_default)

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        isFeedMe = arguments.getBoolean(TYPE_IS_FEED_ME)
        retainInstance = true
        d("TAGGGG", "on create base")
        user = LocalRepository(context).getMeInfor()
        ui = BaseFeedFragmentUI(feeds, user)
        return ui.createView(AnkoContext.Companion.create(context, this))
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        animationRotate = AnimationUtils.loadAnimation(context, R.anim.anim_rotate_start)
        initProgressDialog()
        initSortDialog()
        ui.feedsAdapter.likeListener = this::eventWhenLikeclicked
        ui.feedsAdapter.unLikeListener = this::eventWhenUnLikeclicked
        ui.feedsAdapter.commentListener = this::eventWhenCommentclicked
        ui.feedsAdapter.shareListener = this::eventWhenShareclicked
        ui.feedsAdapter.fileMusicListener = this::eventWhenFileMusicclicked
        viewModel = BaseFeedViewModel(feeds)
        RxBus.listen(LikeEvent::class.java)
                .observeOnUiThread()
                .subscribe(this::handleWhenUpdateLike)
        RxBus.listen(UnlikeEvent::class.java)
                .observeOnUiThread()
                .subscribe(this::handleWhenUpdateUnLike)
        val intentFilter = IntentFilter()
        intentFilter.addAction(Action.PAUSE.value)
        intentFilter.addAction(Action.AUTO_NEXT.value)
        (activity as? MainActivity)?.registerReceiver(myBroadcastFeed, intentFilter)
    }

    override fun onBindViewModel() {
        RxBus.listen(LoadDataFeed::class.java)
                .observeOnUiThread()
                .subscribe(this::handleLoadData)
        RxBus.listen(CommentEvent::class.java)
                .observeOnUiThread()
                .subscribe(this::handleWhenAddComment)
        if (isFeedMe) {
            viewModel.getMeFeeds()
        } else {
            viewModel.getFeeds()
        }
        addDisposables(
                viewModel.feedsObserverable
                        .observeOnUiThread()
                        .subscribe(this::handleGetFeedsSuccess),
                viewModel.progressDilogObserverable
                        .observeOnUiThread()
                        .subscribe(this::handleShowProgressDialog),
                viewModel.commentObserverable
                        .observeOnUiThread()
                        .subscribe(this::handleUpdateCommentSuccess),
                viewModel.isLikeFromCommentScreenObserver
                        .observeOnUiThread()
                        .subscribe(this::handleUpdateLikeFromCommentcreenSuccess))
    }

    internal fun eventOnButtonClicked(view: ImageButton) {
        when (view) {
            ui.mImgBtnPause -> {
                animationRotate.cancel()
                ui.avatarPlay.clearAnimation()
                ui.mImgBtnPlay.visibility = View.VISIBLE
                ui.mImgBtnPause.visibility = View.INVISIBLE
                sendIntent(Action.PAUSE.value)
            }
            ui.mImgBtnPlay -> {
                isPlaying = true
                ui.avatarPlay.startAnimation(animationRotate)
                ui.mImgBtnPlay.visibility = View.INVISIBLE
                ui.mImgBtnPause.visibility = View.VISIBLE
                sendIntent(Action.PLAY.value)
            }
            ui.mImgBtnNext -> {
                isPlaying = true
                ui.avatarPlay.startAnimation(animationRotate)
                ui.mImgBtnPlay.visibility = View.INVISIBLE
                ui.mImgBtnPause.visibility = View.VISIBLE
                sendIntent(Action.NEXT.value)
            }

            ui.mImgBtnPrevious -> {
                isPlaying = true
                ui.avatarPlay.startAnimation(animationRotate)
                sendIntent(Action.PREVIOUS.value)
                ui.mImgBtnPlay.visibility = View.INVISIBLE
                ui.mImgBtnPause.visibility = View.VISIBLE
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        (activity as? MainActivity)?.unregisterReceiver(myBroadcastFeed)
        sendIntent(Action.STOP_MEDIA.value)
    }

    private fun sendListSong() {
        getListSong()
        val intent = Intent(context, SongFeedService::class.java)
        intent.action = Action.SONGS.value
        intent.putParcelableArrayListExtra(TYPE_SONGS, mSongs as ArrayList<out Parcelable>)
        (activity as? MainActivity)?.startService(intent)
        Log.d("TAGGGGGG", "handle feeds success ${mSongs.size}")
    }

    private fun getListSong() {
        mSongs.clear()
        feeds.forEach {
            if (it.fileMusic?.isNotBlank()!!) {
                Log.d("TAGGGGG", "fileMusic: ${it.fileMusicUserWrite}")
                val image = it.avatar ?: ""
                val song = Song(it.id, it.fileMusicUserWrite!!, it.username, image, it.fileMusic, 0, it.time.toString())
                mSongs.add(song)
            }
        }
    }

    internal fun sendIntent(action: String) {
        val intent = Intent(activity, SongFeedService::class.java)
        intent.action = action
        (activity as? MainActivity)?.startService(intent)
    }

    private fun sendIntent(action: String, position: Int) {
        val intent = Intent(context, SongFeedService::class.java)
        intent.action = action
        intent.putExtra(action, position)
        (activity as? MainActivity)?.startService(intent)
    }

    private fun handleGetFeedsSuccess(notification: Notification<DiffUtil.DiffResult>) {
        if (notification.isOnNext) {
            Log.d("TAGGGG", "handle get feed success 1")
            sendListSong()
            notification.value?.dispatchUpdatesTo(ui.feedsAdapter)
        } else {
            // Todo: Handle later
            Log.d("TAGGGG", "on error feeds ${notification.error?.message}")
        }
    }

    private fun handleShowProgressDialog(show: Boolean) {
        if (show) {
            progressDialog.show()
        } else {
            progressDialog.hide()
        }
    }

    private fun eventWhenCommentclicked(position: Int) {
        isLikeFromCommentScreen = false
        bottomSheetDialogComment.feed = feeds[position]
        bottomSheetDialogComment.position = position
        bottomSheetDialogComment.show(fragmentManager, "COMMENT")
    }

    private fun eventWhenShareclicked(position: Int) {
        val intent = Intent(context, ShareActivity::class.java)
        intent.putExtra(ShareActivity.KEY_FILE_MUSIC, feeds[position].fileMusic.toString())
        intent.putExtra(ShareActivity.KEY_ID_FEED, feeds[position].id.toString())

        startActivity(intent)
    }

    private fun initSortDialog() {
        bottomSheetDialogComment = CommentFragment()
    }

    private fun eventWhenLikeclicked(position: Int) {
        viewModel.addLike(position)
    }

    private fun eventWhenUnLikeclicked(position: Int) {
        viewModel.removeLike(position)
    }


    internal fun eventClosePlayFeedClicked() {
        ui.areaPlay.visibility = View.GONE
        sendIntent(Action.STOP_MEDIA.value)
    }

    private fun eventWhenFileMusicclicked(position: Int) {
        ui.areaPlay.visibility = View.VISIBLE
        Glide.with(context)
                .load(feeds[position].avatar)
                .apply(option)
                .into(ui.avatarPlay)
        ui.usernamePlay.text = feeds[position].username
        ui.filePlay.text = feeds[position].fileMusicUserWrite
        ui.avatarPlay.startAnimation(animationRotate)
        ui.mImgBtnPlay.visibility = View.GONE
        ui.mImgBtnPause.visibility = View.VISIBLE
        sendIntent(Action.ID_FEED.value, feeds[position].id)
        sendIntent(Action.PLAY.value)
    }

    private fun initProgressDialog() {
        progressDialog = ProgressDialog(context)
        progressDialog.setCancelable(false)
    }

    private fun handleWhenAddComment(event: CommentEvent) {
        viewModel.addComment(event.position, event.comment)
    }

    private fun handleWhenUpdateLike(event: LikeEvent) {
        isLikeFromCommentScreen = true
        viewModel.addLike(event.position)
        viewModel.removeLike(event.position)
    }

    private fun handleWhenUpdateUnLike(event: UnlikeEvent) {
        isLikeFromCommentScreen = true
        viewModel.removeLike(event.position)
    }

    private fun handleUpdateCommentSuccess(notification: Notification<MutableList<Comment>>) {
        if (notification.isOnNext) {
            val position = bottomSheetDialogComment.position
            val comments = notification.value!!
            bottomSheetDialogComment.ui.cmtFirst.visibility = View.GONE
            bottomSheetDialogComment.feed.comments.clear()
            bottomSheetDialogComment.feed.comments.addAll(comments)
            bottomSheetDialogComment.ui.commentsAdapter.notifyDataSetChanged()
            bottomSheetDialogComment.ui.recyclerComment.scrollToPosition(comments.size)
            bottomSheetDialogComment.ui.countComment.text = comments.size.toString().plus(" comments")
            feeds[position].commentCount = comments.size.toLong()
            feeds[position].comments.clear()
            feeds[position].comments.addAll(comments)
            ui.feedsAdapter.notifyItemChanged(position)
        } else {
            // Todo: Handle later
        }
    }

    private fun handleUpdateLikeFromCommentcreenSuccess(notification: Notification<Feed>) {
        if (notification.isOnNext) {
            if (isLikeFromCommentScreen) {
                val feed = notification.value!!
                bottomSheetDialogComment.feed = feed
                bottomSheetDialogComment.updateLike()
            }
        } else {
            // Todo: Handle later
        }
    }

    private fun handleLoadData(event: LoadDataFeed) {
        ui.areaPlay.visibility = View.GONE
        if (event.isFeedMe) {

            viewModel.getMeFeeds()
            return
        }
        viewModel.getFeeds()
    }

    /**
     * Create MyBroadcast
     */
    internal inner class MyBroadcastFeed : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val s = intent.action
            if (s == Action.PAUSE.value) {
                ui.mImgBtnPlay.visibility = View.VISIBLE
                ui.mImgBtnPause.visibility = View.INVISIBLE
                ui.avatarPlay.clearAnimation()
            } else {
                ui.mImgBtnPlay.visibility = View.INVISIBLE
                ui.mImgBtnPause.visibility = View.VISIBLE
                val currentPosition = intent.getIntExtra(Action.AUTO_NEXT.value, 0)
                ui.usernamePlay.text = mSongs[currentPosition].artist
                ui.filePlay.text = mSongs[currentPosition].name
            }
        }
    }
}