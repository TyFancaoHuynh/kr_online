package com.example.hoavot.karaokeonline.ui.profile

import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.app.ProgressDialog
import android.content.*
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.app.AlertDialog
import android.support.v7.util.DiffUtil
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.ImageButton
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.hoavot.karaokeonline.R
import com.example.hoavot.karaokeonline.data.LocalRepository
import com.example.hoavot.karaokeonline.data.model.other.*
import com.example.hoavot.karaokeonline.data.source.response.UserResponse
import com.example.hoavot.karaokeonline.ui.base.BaseFragment
import com.example.hoavot.karaokeonline.ui.extensions.RxBus
import com.example.hoavot.karaokeonline.ui.extensions.addChildFragment
import com.example.hoavot.karaokeonline.ui.extensions.animSlideInRightSlideOutRight
import com.example.hoavot.karaokeonline.ui.extensions.observeOnUiThread
import com.example.hoavot.karaokeonline.ui.feed.FeedFragment
import com.example.hoavot.karaokeonline.ui.feed.FeedFragmentUI
import com.example.hoavot.karaokeonline.ui.feed.FeedViewModel
import com.example.hoavot.karaokeonline.ui.feed.SongFeedService
import com.example.hoavot.karaokeonline.ui.feed.caption.CaptionActivity
import com.example.hoavot.karaokeonline.ui.feed.comment.CommentFragment
import com.example.hoavot.karaokeonline.ui.feed.share.ShareActivity
import com.example.hoavot.karaokeonline.ui.main.MainActivity
import com.example.hoavot.karaokeonline.ui.playmusic.model.Song
import com.example.hoavot.karaokeonline.ui.playmusic.service.Action
import com.example.hoavot.karaokeonline.ui.profile.baseprofile.BaseProfileFragment
import com.example.hoavot.karaokeonline.ui.profile.edit.EditProfileFragment
import io.reactivex.Notification
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
    private lateinit var feedViewModel: FeedViewModel
    private lateinit var user: User
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
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE) // https://github.com/bumptech/glide/issues/319
            .placeholder(R.drawable.user_default)
    internal var isPlaying = false

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        feedViewModel = FeedViewModel(LocalRepository(context),feeds)
        user = feedViewModel.getMeInfor()
        ui = ProfileFragmentUI(feeds, user)
        return ui.createView(AnkoContext.Companion.create(context, this))
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initProgressDialog()
        initSortDialog()
        Glide.with(context)
                .load(user.avatar)
                .apply(option)
                .into(ui.avatar)
        ui.username.text = user.username
        ui.age.text = user.age.toString().plus(" Tuổi")
        feedViewModel.getMeFeeds()
        RxBus.listen(LoadDataFeedMe::class.java)
                .observeOnUiThread()
                .subscribe(this::handleLoadDataFeedMe)
        ui.feedsAdapter.likeListener = this::eventWhenLikeclicked
        ui.feedsAdapter.unLikeListener = this::eventWhenUnLikeclicked
        ui.feedsAdapter.commentListener = this::eventWhenCommentclicked
        ui.feedsAdapter.shareListener = this::eventWhenShareclicked
        ui.feedsAdapter.fileMusicListener = this::eventWhenFileMusicclicked
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
    }

    override fun onBindViewModel() {
        addDisposables(
                feedViewModel.feedsObserverable
                        .observeOnUiThread()
                        .subscribe(this::handleGetFeedsSuccess),
                feedViewModel.progressDilogObserverable
                        .observeOnUiThread()
                        .subscribe(this::handleShowProgressDialog),
                feedViewModel.commentObserverable
                        .observeOnUiThread()
                        .subscribe(this::handleUpdateCommentSuccess),
                feedViewModel.isLikeFromCommentScreenObserver
                        .observeOnUiThread()
                        .subscribe(this::handleUpdateLikeFromCommentcreenSuccess),
                RxBus.listen(CommentFeedMeEvent::class.java)
                        .observeOnUiThread()
                        .subscribe(this::handleWhenAddComment)
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
                feedViewModel.updateAvatar(avatarFile)
                        .observeOnUiThread()
                        .subscribe(
                                this::handleWhenUpdateAvatarSuccess,
                                {
                                    d("TAGGGG", "error update avatar error: ${it.message}")
                                })
                dialogShowCamera.cancel()
            }
        }
    }

    private fun convertBitmapToFile(bitmap: Bitmap): File {
        val f = File(context.cacheDir, "avatar" + Date().time + ".jpg")
        f.createNewFile()

        //Convert bitmap to byte array
        val bos = ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0 /*ignored for PNG*/, bos);
        val bitmapdata = bos.toByteArray()

        //write the bytes in file
        val fos = FileOutputStream(f);
        fos.write(bitmapdata);
        fos.flush()
        fos.close()
        return f
    }

    private fun handleWhenUpdateAvatarSuccess(userResponse: UserResponse) {
        d("TAGGGG", "handle update avatar success")
        feedViewModel.saveUser(userResponse.user)
        val option = RequestOptions()
                .centerCrop()
                .override(ui.avatar.width, ui.avatar.width)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE) // https://github.com/bumptech/glide/issues/319
                .placeholder(R.drawable.bg_item_place_holder)
        Glide.with(context)
                .load(userResponse.user.avatar)
                .apply(option)
                .into(ui.avatar)
    }

    private fun handleLoadDataFeedMe(event: LoadDataFeedMe) {
        feedViewModel.getMeFeeds()
    }

    private fun initSortDialog() {
        bottomSheetDialogComment = CommentFragment()
        bottomSheetDialogComment.isFeed=true
    }

    private fun eventWhenLikeclicked(position: Int) {
        feedViewModel.addLike(position)
    }

    private fun eventWhenUnLikeclicked(position: Int) {
        feedViewModel.removeLike(position)
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
                d("TAGGGGG", "fileMusic: ${it.fileMusicUserWrite}")
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
        feedViewModel.removeLike(event.position)
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

    private fun handleGetFeedsSuccess(notification: Notification<DiffUtil.DiffResult>) {
        if (notification.isOnNext) {
            d("TAGGGG", "handle get feed success 1")
            sendListSong()
            ui.countFeed.text=feeds.size.toString().plus(" Bài viết")
            notification.value?.dispatchUpdatesTo(ui.feedsAdapter)
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
