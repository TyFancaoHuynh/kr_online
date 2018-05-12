package com.example.hoavot.karaokeonline.ui.feed

import android.app.Service
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.IBinder
import android.util.Log.d
import com.example.hoavot.karaokeonline.ui.playmusic.model.Song
import com.example.hoavot.karaokeonline.ui.playmusic.service.Action
import java.io.IOException
import java.util.*

/**
 *
 * @author at-hoavo
 */
class SongFeedService : Service(), MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    private var mMediaPlayer: MediaPlayer? = null
    private var mSongs: List<Song>? = null
    private var mCurrentPosition: Int = 0
    private var mCheck: Boolean = false

    override fun onBind(intent: Intent): IBinder? {
        // No-op
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            if (intent.action == Action.SONGS.value) {
                mSongs = intent.getParcelableArrayListExtra<Song>(FeedFragment.TYPE_SONGS).toList()
                mSongs!!.forEach {
                    d("TAGGGGG", "mySongs: ${mSongs?.size} first: ${it.url}")
                }
            } else if (intent.action == Action.PAUSE.value) {
                d("TAGGGGG", "pause: ${mSongs?.size}")
                if (!mCheck) {
                    setSongPlay()
                    mCheck = true
                } else {
                    if (mMediaPlayer != null) {
                        mMediaPlayer!!.start()
                    }
                }
            } else if (intent.action == Action.PLAY.value) {
                d("TAGGGGG","Play service")
                if (mMediaPlayer != null) {
                    if (mMediaPlayer!!.isPlaying) {
                        mMediaPlayer!!.pause()
                    }
                }
            } else if (intent.action == Action.NEXT.value) {
                mCurrentPosition++
                if (mCurrentPosition == mSongs!!.size) {
                    mCurrentPosition = 0
                }
                setSongPlay()
                sendPositionToActivity()
            } else if (intent.action == Action.PREVIOUS.value) {

                mCurrentPosition--
                if (mCurrentPosition < 0) {
                    mCurrentPosition = mSongs!!.size - 1
                }
                setSongPlay()
                sendPositionToActivity()
            }
        }
        return Service.START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mMediaPlayer != null) {
            mMediaPlayer!!.release()
            mMediaPlayer = null
        }
//        unregisterReceiver(mNotificationBroadcast)
    }

    override fun onCompletion(mp: MediaPlayer) {
    }

    override fun onPrepared(mp: MediaPlayer) {
        if (mMediaPlayer != null) {
            mMediaPlayer!!.start()
        }
    }

    // Init MediaPlayer
    private fun createSongIfNeed() {
        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer()
            mMediaPlayer!!.setOnPreparedListener(this)
            mMediaPlayer!!.setOnCompletionListener(this)
        } else {
            mMediaPlayer!!.reset()
        }
    }

    private fun setSongPlay() {
        try {
            createSongIfNeed()
            mMediaPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
            d("TAGGGGGG","set Song play: ")
            mMediaPlayer!!.setDataSource(applicationContext, Uri.parse(mSongs!![mCurrentPosition].url))
            mMediaPlayer!!.prepare()
        } catch (e: IOException) {
        } catch (e: IllegalFormatException) {
        }
    }

    // Update current position of Activity
    private fun sendPositionToActivity() {
        val i = Intent()
        i.action = Action.SEND_POSITION.value
        i.putExtra(FeedFragment.TYPE_POSITION, mCurrentPosition)
        sendBroadcast(i)
    }
}
