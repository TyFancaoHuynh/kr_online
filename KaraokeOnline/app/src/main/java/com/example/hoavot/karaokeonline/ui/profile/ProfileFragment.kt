package com.example.hoavot.karaokeonline.ui.profile

import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.ActivityCompat
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import com.aditya.filebrowser.Constants
import com.aditya.filebrowser.FileChooser
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.hoavot.karaokeonline.R
import com.example.hoavot.karaokeonline.data.LocalRepository
import com.example.hoavot.karaokeonline.data.model.other.*
import com.example.hoavot.karaokeonline.ui.base.BaseFragment
import com.example.hoavot.karaokeonline.ui.extensions.*
import com.example.hoavot.karaokeonline.ui.feed.FeedFragment
import com.example.hoavot.karaokeonline.ui.feed.FeedViewModel
import com.example.hoavot.karaokeonline.ui.feed.SongFeedService
import com.example.hoavot.karaokeonline.ui.feed.comment.CommentFragment
import com.example.hoavot.karaokeonline.ui.feed.like.LikeFragment
import com.example.hoavot.karaokeonline.ui.feed.share.ShareActivity
import com.example.hoavot.karaokeonline.ui.login.LoginActivity
import com.example.hoavot.karaokeonline.ui.main.MainActivity
import com.example.hoavot.karaokeonline.ui.playmusic.model.Song
import com.example.hoavot.karaokeonline.ui.playmusic.service.Action
import com.example.hoavot.karaokeonline.ui.profile.baseprofile.BaseProfileFragment
import com.example.hoavot.karaokeonline.ui.profile.edit.EditProfileFragment
import com.facebook.FacebookSdk.getApplicationContext
import io.reactivex.Notification
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.SingleSubject
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.noButton
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.yesButton
import java.io.File
import java.util.*


/**
 *
 * @author at-hoavo.
 */
class ProfileFragment : BaseFragment() {
    companion object {
        internal const val TYPE_GALLERY = 0
        internal const val TYPE_CAMERA = 1
        internal const val KEY_USER_ID = "KEY_USER_ID"
        internal const val KEY_PROFILE_FRAGMENT = "KEY_PROFILE_FRAGMENT"

        fun newInstance(userId: Int, isFromProfileFragment: Boolean): ProfileFragment {
            val instance = ProfileFragment()
            val bundle = Bundle().apply {
                putInt(KEY_USER_ID, userId)
                putBoolean(KEY_PROFILE_FRAGMENT, isFromProfileFragment)
            }
            instance.arguments = bundle
            return instance
        }
    }

    private lateinit var ui: ProfileFragmentUI
    private lateinit var feedViewModel: FeedViewModel
    private lateinit var user: User
    private var userId: Int = -1
    private var isStartFromProfileFragment = false
    private lateinit var bottomSheetDialogLike: LikeFragment
    private lateinit var dialogShowCamera: Dialog
    private var feeds = mutableListOf<Feed>()
    private lateinit var progressDialog: ProgressDialog
    private lateinit var bottomSheetDialogComment: CommentFragment
    private var isLikeFromCommentScreen = false
    private lateinit var animationRotate: Animation
    private var myBroadcastProfile = MyBroadcastProfile()
    private var mSongs = mutableListOf<Song>()

    val option = RequestOptions()
            .centerCrop()

    internal var isPlaying = false

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        userId = arguments.getInt(KEY_USER_ID)
        isStartFromProfileFragment = arguments.getBoolean(KEY_PROFILE_FRAGMENT, false)
        feedViewModel = FeedViewModel(LocalRepository(context), feeds)
        ui = ProfileFragmentUI(feeds)
        return ui.createView(AnkoContext.Companion.create(context, this))
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        animationRotate = AnimationUtils.loadAnimation(context, R.anim.anim_rotate_start)
        initProgressDialog()
        initSortDialog()
        initBottomSheetLike()
        if (!isStartFromProfileFragment) {
            ui.more.visibility = View.GONE
        }
        feedViewModel.getMeFeeds(userId)
        RxBus.listen(LoadDataFeedMe::class.java)
                .observeOnUiThread()
                .subscribe(this::handleLoadDataFeedMe)
        RxBus.listen(UserUpdateEvent::class.java)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleUpdateUser)
        ui.feedsAdapter.likeListener = this::eventWhenLikeclicked
        ui.feedsAdapter.unLikeListener = this::eventWhenUnLikeclicked
        ui.feedsAdapter.commentListener = this::eventWhenCommentclicked
        ui.feedsAdapter.shareListener = this::eventWhenShareclicked
        ui.feedsAdapter.fileMusicListener = this::eventWhenFileMusicclicked
        ui.feedsAdapter.updateFeedClickListener = this::eventUpdateClicked
        ui.feedsAdapter.deleteFeedClickListener = this::eventDeleteClicked
        ui.feedsAdapter.likeSmallListener = this::eventWhenLikeSmallclicked
        RxBus.listen(LikeFeedMeEvent::class.java)
                .observeOnUiThread()
                .subscribe(this::handleWhenUpdateLike)
        RxBus.listen(UnlikeFeedMeEvent::class.java)
                .observeOnUiThread()
                .subscribe(this::handleWhenUpdateUnLike)
        val intentFilter = IntentFilter()
        intentFilter.addAction(Action.PAUSE.value)
        intentFilter.addAction(Action.AUTO_NEXT.value)
        (activity as? MainActivity)?.registerReceiver(myBroadcastProfile, intentFilter)
        RxBus.listen(RegisterBRProfileEvent::class.java)
                .subscribe({
                    toast("vo day ni")
                    (activity as? MainActivity)?.registerReceiver(myBroadcastProfile, intentFilter)
                })
        RxBus.listen(StopBRProfileEvent::class.java)
                .subscribe({
                    (activity as? MainActivity)?.unregisterReceiver(myBroadcastProfile)
                })
    }

    override fun onBindViewModel() {
        feedViewModel.getMeInfor(userId)
        addDisposables(
                feedViewModel.userObserverable
                        .observeOnUiThread()
                        .subscribe({
                            updateUser(it)
                        }),
                feedViewModel.feedsObserverable
                        .observeOnUiThread()
                        .subscribe(this::handleGetFeedsSuccess),
                feedViewModel.progressDilogObserverable
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::handleShowProgressDialog),
                feedViewModel.commentObserverable
                        .observeOnUiThread()
                        .subscribe(this::handleUpdateCommentSuccess),
                feedViewModel.isLikeFromCommentScreenObserver
                        .observeOnUiThread()
                        .subscribe(this::handleUpdateLikeFromCommentcreenSuccess),
                feedViewModel.startObserverable
                        .observeOnUiThread()
                        .subscribe(this::handleUpdateLikeSuccess),
                RxBus.listen(CommentFeedMeEvent::class.java)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::handleWhenAddComment)
        )
    }

    internal fun onMoreClick() {
        ui.editProfile.visibility = View.VISIBLE
    }

    internal fun eventOnCameraClick() {
        val i2 = Intent(getApplicationContext(), FileChooser::class.java)
        i2.putExtra(Constants.SELECTION_MODE, Constants.SELECTION_MODES.SINGLE_SELECTION.ordinal)
        startActivityForResult(i2, TYPE_GALLERY)
    }

    override fun onDetach() {
        super.onDetach()
        (activity as? MainActivity)?.unregisterReceiver(myBroadcastProfile)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TYPE_GALLERY && resultCode == RESULT_OK && data != null) {
            val uri: Uri = data.data as Uri
            val file = File(UriUtil.getPath(context, uri))

            feedViewModel.updateAvatar(file)
                    .observeOnUiThread()
                    .subscribe({
                        feedViewModel.saveUser(it.user)
                        val option = RequestOptions()
                                .fitCenter()
                        Glide.with(this)
                                .load(uri)
                                .apply(option)
                                .into(ui.avatar)
                        RxBus.publish(LoadDataFeedMe())
                    }, {
                        d("HHHHHHH", "error update avatar error: ${it.message}")
                    })
        }
    }

    fun getImageBitmap(uri: String, ratio: Float): Single<Bitmap> {
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

        Glide.with(context)
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

    internal fun handleWhenEditProfileClick() {
        ui.editProfile.visibility = View.GONE
        val editProfileFragment = EditProfileFragment()
        (parentFragment as? BaseProfileFragment)?.addChildFragment(R.id.profileFragmentContainer, editProfileFragment, EditProfileFragment::class.java.name, {
            it.animSlideInRightSlideOutRight()
        })
    }

    internal fun eventLogoutClicked() {
        feedViewModel.logout()
        ActivityCompat.finishAffinity(activity)
        startActivity<LoginActivity>()
    }

    internal fun eventOnProfileClicked() {
        ui.editProfile.visibility = View.GONE
    }

    private fun handleLoadDataFeedMe(event: LoadDataFeedMe) {
        ui.areaPlay.visibility = View.GONE
        feedViewModel.getMeFeeds(userId)
        feedViewModel.getMeInfor(userId)
    }

    private fun initSortDialog() {
        bottomSheetDialogComment = CommentFragment()
        bottomSheetDialogComment.isFeed = true
    }

    private fun eventWhenLikeclicked(position: Int) {
        feedViewModel.addLike(position)
    }

    private fun eventWhenUnLikeclicked(position: Int) {
        feedViewModel.removeLike(position)
    }

    private fun initBottomSheetLike() {
        bottomSheetDialogLike = LikeFragment()
    }

    private fun eventWhenLikeSmallclicked(position: Int) {
        bottomSheetDialogLike.feedId = feeds[position].id
        bottomSheetDialogLike.show(fragmentManager, "LIKE")
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

    private fun updateUser(user: User) {
        Glide.with(context)
                .load(user.avatar)
                .apply(option)
                .into(ui.avatar)
        ui.username.text = user.username
    }

    private fun handleUpdateUser(event: UserUpdateEvent) {
        updateUser(event.user)
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

    private fun sendListSong() {
        getListSong()
        val intent = Intent(context, SongFeedService::class.java)
        intent.action = Action.SONGS.value
        intent.putParcelableArrayListExtra(FeedFragment.TYPE_SONGS, mSongs as ArrayList<out Parcelable>)
        (activity as? MainActivity)?.startService(intent)
        d("TAGGGGGG", "handle feeds success ${mSongs.size}")
    }

    private fun getListSong() {
        mSongs.clear()
        feeds.forEach {
            if (it.fileMusic?.isNotBlank()!!) {
                val image = it.avatar ?: ""
                val song = Song(it.id, it.fileMusicUserWrite!!, it.username, image, it.fileMusic, 0, it.time.toString())
                mSongs.add(song)
            }
        }
    }

    internal fun sendIntent(action: String) {
        val intent = Intent(context, SongFeedService::class.java)
        intent.action = action
        (activity as? MainActivity)?.startService(intent)
    }

    private fun sendIntent(action: String, position: Int) {
        val intent = Intent(context, SongFeedService::class.java)
        intent.action = action
        intent.putExtra(action, position)
        (activity as? MainActivity)?.startService(intent)
    }

    private fun handleShowProgressDialog(show: Boolean) {
        if(userVisibleHint) {
            if (show) {
                progressDialog.show()
            } else {
                progressDialog.dismiss()
            }
        }
    }

    private fun handleUpdateLikeSuccess(notification: Notification<Feed>) {
        notification.value?.run {
            with(feeds.find {
                it.id == this.id
            }) {
                this?.let {
                    it.likeCount = this.likeCount
                    it.likeFlag = this.likeFlag
                    ui.feedsAdapter.notifyDataSetChanged()
                }
            }
        }

    }

    private fun eventWhenCommentclicked(position: Int) {
        isLikeFromCommentScreen = false
        bottomSheetDialogComment.feed = feeds[position]
        bottomSheetDialogComment.position = position
        bottomSheetDialogComment.show(fragmentManager, "COMMENT")
    }

    private fun eventWhenShareclicked(position: Int) {
//        val intent = Intent(context, ShareActivity::class.java)
//        intent.putExtra(ShareActivity.KEY_FILE_MUSIC, feeds[position].fileMusicUserWrite.toString())
//        intent.putExtra(ShareActivity.KEY_ID_FEED, feeds[position].id.toString())
//        startActivity(intent)
        shareMusic()
    }

    private fun shareMusic() {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        val message = "Enjoy this song !"
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, message)
        val url = "http://singnowapp.com/share_song/index.html?sid=19954"
        sharingIntent.putExtra(Intent.EXTRA_TEXT, url)
        startActivityForResult(Intent.createChooser(sharingIntent, "Share via"), ShareActivity.REQUEST_CODE_MUSIC)
    }

    private fun eventUpdateClicked(position: Int) {

    }

    private fun eventDeleteClicked(position: Int) {
        alert {
            title = "CONFIRM"
            message = "Bạn có muốn xoá bài viết này?"
            yesButton {
                feedViewModel.deleteFeed(feeds[position].id)
                        .observeOnUiThread()
                        .subscribe({
                            alert {
                                message = "Xoá thành công!"
                                yesButton {
                                }
                            }.show()

                            feeds.removeAt(position)
                            ui.feedsAdapter.notifyDataSetChanged()
                        }, {
                            alert {
                                title = "ERROR"
                                message = "Xoá thất bại!"
                                yesButton { }
                            }.show()
                        })
            }
            noButton {}
        }.show()
    }

    private fun initProgressDialog() {
        progressDialog = ProgressDialog(context)
        progressDialog.setCancelable(false)
    }

    private fun handleWhenAddComment(event: CommentFeedMeEvent) {
        feedViewModel.addComment(event.position, event.comment)
    }

    private fun handleWhenUpdateLike(event: LikeFeedMeEvent) {
        isLikeFromCommentScreen = true
        feedViewModel.addLike(event.position)
    }

    private fun handleWhenUpdateUnLike(event: UnlikeFeedMeEvent) {
        isLikeFromCommentScreen = true
        feedViewModel.removeLike(event.position)
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

    private fun handleGetFeedsSuccess(notification: Notification<MutableList<Feed>>) {
        if (notification.isOnNext) {
            d("NNNNNNNNNN", "get feed success")
            sendListSong()
            ui.countFeed.text = feeds.size.toString().plus(" Bài viết")
            ui.feedsAdapter.notifyDataSetChanged()
        } else {
            // Todo: Handle later
            d("TAGGGG", "on error feeds ${notification.error?.message}")
        }
    }

    /**
     * Create MyBroadcast
     */
    internal inner class MyBroadcastProfile : BroadcastReceiver() {
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
