package com.example.hoavot.karaokeonline.ui.playmusic


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.SeekBar
import com.example.hoavot.karaokeonline.R
import com.example.hoavot.karaokeonline.ui.base.BaseFragment
import com.example.hoavot.karaokeonline.ui.main.MainActivity
import com.example.hoavot.karaokeonline.ui.playmusic.adapter.OnChooseSongListener
import com.example.hoavot.karaokeonline.ui.playmusic.adapter.SongAdapter
import com.example.hoavot.karaokeonline.ui.playmusic.model.Song
import com.example.hoavot.karaokeonline.ui.playmusic.service.Action
import com.example.hoavot.karaokeonline.ui.playmusic.service.SongService
import com.example.hoavot.karaokeonline.ui.playmusic.utils.MusicUtil
import com.example.hoavot.karaokeonline.ui.playmusic.utils.showDate
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.support.v4.ctx


import java.util.ArrayList

/**
 * Copyright © 2017 AsianTech inc.
 * Created by at-hoavo on 01/07/2017.
 */
class PlayFragment : BaseFragment(), OnChooseSongListener {

    private var mSongs = mutableListOf<Song>()
    private var mCurrentPlay: Int = 0
    private var mSongAdapter: com.example.hoavot.karaokeonline.ui.playmusic.adapter.SongAdapter? = null
    private var mMyBroadcast: MyBroadcast? = null
    private lateinit var ui: PlayFragmentUI
    private lateinit var animationRotate: Animation

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        ui = PlayFragmentUI()
        return ui.createView(AnkoContext.Companion.create(ctx, this, false))
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        animationRotate = AnimationUtils.loadAnimation(context, R.anim.anim_rotate_start)
        mSongs = getList()
        ui.tvNameMusic.text = mSongs[0].name
        ui.tvArtist.text = mSongs[0].artist
        ui.dateMusic.text = mSongs[0].date.toLong().showDate()

        // Register Broadcast
        mMyBroadcast = MyBroadcast()
        val intentFilter = IntentFilter()
        intentFilter.addAction(Action.SEND_POSITION.value)
        intentFilter.addAction(Action.SEEK.value)
        intentFilter.addAction(Action.CLOSE_ACTIVITY.value)
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF)
        (activity as? MainActivity)?.registerReceiver(mMyBroadcast, intentFilter)

        // Create RecyclerView
        mSongAdapter = SongAdapter(mSongs, context, this)
        val linearLayoutManager = LinearLayoutManager(context)
        ui.mRecyclerView.layoutManager = linearLayoutManager
        ui.mRecyclerView.adapter = mSongAdapter

        // Send list song to Service
        sendListSong()

        ui.mSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                // No-op
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // No-op
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                val intent = Intent(context, SongService::class.java)
                intent.action = Action.SEEK_TO.value
                intent.putExtra(TYPE_CHOOSE_TIME, seekBar.progress)
                (activity as? MainActivity)?.startService(intent)
            }
        })

//        // Hander when click into notification
//        if (intent.action == Action.CLICK_NOTIFICATION.value) {
//            val check = intent.getBooleanExtra(TYPE_ISPLAYING, false)
//            if (check) {
//                mImgBtnPlay!!.visibility = View.VISIBLE
//                mImgBtnPause!!.visibility = View.INVISIBLE
//            } else {
//                mImgBtnPlay!!.visibility = View.INVISIBLE
//                mImgBtnPause!!.visibility = View.VISIBLE
//            }
//            sendIntent(Action.CLICK_NOTIFICATION.value)
//            mCurrentPlay = intent.getIntExtra(TYPE_POSITION, 0)
//            mSongAdapter!!.setPosition(mCurrentPlay)
//            mRecyclerView!!.scrollToPosition(mCurrentPlay)
//        }
    }

    override fun onBindViewModel() {
        // Todo: Handle later
    }


    override fun onDetach() {
        super.onDetach()
        (activity as? MainActivity)?.unregisterReceiver(mMyBroadcast)
    }

    internal fun onButtonClick(v: ImageButton?) {
        when (v) {
            ui.mImgBtnPlay -> {
                animationRotate.cancel()
                ui.imgMusic.clearAnimation()
                ui.mImgBtnPlay!!.visibility = View.INVISIBLE
                ui.mImgBtnPause!!.visibility = View.VISIBLE
                sendIntent(Action.PLAY.value)
            }
            ui.mImgBtnPause -> {
                ui.imgMusic.startAnimation(animationRotate)
                ui.mImgBtnPlay!!.visibility = View.VISIBLE
                ui.mImgBtnPause!!.visibility = View.INVISIBLE
                sendIntent(Action.PAUSE.value)
            }
            ui.mImgBtnNext -> {
                sendIntent(Action.NEXT.value)
                ui.mImgBtnPlay!!.visibility = View.VISIBLE
                ui.mImgBtnPause!!.visibility = View.INVISIBLE
                mSongAdapter!!.setPosition(mCurrentPlay)
                mSongAdapter!!.notifyDataSetChanged()
                ui.mRecyclerView.scrollToPosition(mCurrentPlay)
            }

            ui.mImgBtnPrevious -> {
                ui.mImgBtnPlay!!.visibility = View.VISIBLE
                ui.mImgBtnPause!!.visibility = View.INVISIBLE
                sendIntent(Action.PREVIOUS.value)
                mSongAdapter!!.setPosition(mCurrentPlay)
                ui.mRecyclerView.scrollToPosition(mCurrentPlay)
                mSongAdapter!!.notifyDataSetChanged()
            }

            ui.mImgBtnShuffle -> {
                ui.mImgBtnShuffle!!.visibility = View.INVISIBLE
                ui.mImgBtnShuffleSelected!!.visibility = View.VISIBLE
                sendIntent(Action.SHUFFLE.value)
            }
            ui.mImgBtnShuffleSelected -> {
                ui.mImgBtnShuffle!!.visibility = View.VISIBLE
                ui.mImgBtnShuffleSelected!!.visibility = View.INVISIBLE
                sendIntent(Action.SHUFFLE_SELECTED.value)
            }
            ui.mImgBtnAutoNext -> {
                ui.mImgBtnAutoNext!!.visibility = View.INVISIBLE
                ui.mImgBtnAutoNextSelected!!.visibility = View.VISIBLE
                sendIntent(Action.AUTO_NEXT.value)
            }
            ui.mImgBtnAutoNextSelected -> {
                ui.mImgBtnAutoNext!!.visibility = View.VISIBLE
                ui.mImgBtnAutoNextSelected!!.visibility = View.INVISIBLE
                sendIntent(Action.AUTO_NEXT_SELETED.value)
            }
        }
    }

    private fun sendIntent(action: String) {
        val intent = Intent(context, SongService::class.java)
        intent.action = action
        (activity as? MainActivity)?.startService(intent)
    }

    private fun sendListSong() {
        val intent = Intent(context, SongService::class.java)
        intent.action = Action.SONGS.value
        intent.putParcelableArrayListExtra(TYPE_SONGS, mSongs as ArrayList<out Parcelable>)
        (activity as? MainActivity)?.startService(intent)
    }

    // Hander when click item on RecyclerView
    override fun onSongUpdate(positon: Int) {
        ui.imgMusic.startAnimation(animationRotate)
        ui.mImgBtnPlay!!.visibility = View.VISIBLE
        ui.mImgBtnPause!!.visibility = View.INVISIBLE
        ui.tvNameMusic.setText(mSongs.get(positon).name)
        ui.tvArtist.setText(mSongs.get(positon).artist)
        ui.dateMusic.text = mSongs[positon].date.toLong().showDate()
        mSongAdapter!!.setPosition(positon)
        mSongAdapter!!.notifyDataSetChanged()
        val intent = Intent(context, SongService::class.java)
        intent.action = Action.CHOOSE_PLAY.value
        intent.putExtra(PlayFragment.TYPE_POSITION, positon)
        (activity as? MainActivity)?.startService(intent)
        ui.mRecyclerView.scrollToPosition(mCurrentPlay)
    }

    /**
     * Create MyBroadcast
     */
    internal inner class MyBroadcast : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val s = intent.action
            if (s == Action.SEND_POSITION.value) {
                mCurrentPlay = intent.getIntExtra(PlayFragment.TYPE_POSITION, 0)
                mSongAdapter!!.setPosition(mCurrentPlay)
                mSongAdapter!!.notifyDataSetChanged()
                ui.mRecyclerView.scrollToPosition(mCurrentPlay)
            } else if (s == Action.SEEK.value) {
                processTime(intent)
            } else if (s == Action.CLOSE_ACTIVITY.value) {
                (activity as? MainActivity)?.finish()
            }
        }
    }

    // Process seekbar
    private fun processTime(intent: Intent) {
        if (mMyBroadcast != null) {
            val length = Integer.parseInt(intent.getStringExtra(TYPE_TIME))
            val position = Integer.parseInt(intent.getStringExtra(TYPE_SECOND))
            ui.mSeekBar.max = length
            ui.mSeekBar.progress = position
            mCurrentPlay = intent.getIntExtra(TYPE_POSITION, 0)
            ui.currentTime.setText(MusicUtil.showTime(position.toLong()))
            ui.totalTime.setText(MusicUtil.showTime(length.toLong()))
            ui.tvNameMusic.setText(mSongs.get(mCurrentPlay).name)
            ui.tvArtist.setText(mSongs.get(mCurrentPlay).artist)
            ui.dateMusic.text = mSongs[mCurrentPlay].date.toLong().showDate()
        }
    }

    internal fun getList(): MutableList<Song> {
        val listSong = mutableListOf<Song>()
        val contentResolver = this.context.getContentResolver()
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val cursor = contentResolver.query(uri, null, null, null, null);
        cursor.moveToFirst()
        if (cursor != null && cursor.moveToFirst()) {
            val id = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
            val titleID = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val artistID = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
            val dateId = cursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED)
            val dataID = cursor.getColumnIndex(MediaStore.Audio.Media.DATA)
            val timeId = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)
            do {
                val idSong = cursor.getLong(id)
                val title = cursor.getString(titleID)
                val artist = cursor.getString(artistID)
                val date = cursor.getString(dateId)
                val data = cursor.getString(dataID)
                val time = cursor.getInt(timeId)
                listSong.add(Song(0,title, artist, "", data, time, date))
            } while (cursor.moveToNext());
        }
        return listSong
    }

    companion object {
        val TYPE_SONGS = "Songs"
        val TYPE_POSITION = "Position"
        val TYPE_CHOOSE_TIME = "chooseTime"
        val TYPE_TIME = "time"
        val TYPE_SECOND = "second"
        val TYPE_ISPLAYING = "isPlaying"
    }
}
