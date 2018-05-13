package com.example.hoavot.karaokeonline.ui.feed

import android.app.Service
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.IBinder
import android.util.Log.d
import com.example.hoavot.karaokeonline.ui.base.feed.BaseFeedFragment
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
    private var mSongs: List<Song> = listOf()
    private var mCurrentPosition: Int = 0
    private var isPlayed = false

    override fun onBind(intent: Intent): IBinder? {
        // No-op
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            when (intent.action) {
                Action.SONGS.value -> {
                    mSongs = intent.getParcelableArrayListExtra<Song>(BaseFeedFragment.TYPE_SONGS).toList()
                    d("TAGGGG","song size feed: ${mSongs.size}")
                    isPlayed = false
                }
                Action.ID_FEED.value -> {
                    for (i in 0..mSongs.size - 1) {
                        if (mSongs[i].id == intent.getIntExtra(Action.ID_FEED.value, 0)) {
                            mCurrentPosition = i
                        }
                    }
                    isPlayed = false
                }
                Action.PLAY.value -> {
                    if (!isPlayed) {
                        isPlayed = true
                        setSongPlay()
                    } else if (mMediaPlayer != null) {
                        mMediaPlayer!!.start()
                    }
                }
                Action.PAUSE.value -> {
                    pauseMedia()
                }
                Action.NEXT.value -> {
                    mCurrentPosition++
                    if (mCurrentPosition == mSongs.size) {
                        mCurrentPosition = 0
                    }
                    setSongPlay()
                    sendAutoNextToFeed()
                }
                Action.PREVIOUS.value -> {

                    mCurrentPosition--
                    if (mCurrentPosition < 0) {
                        mCurrentPosition = 0
                    }
                    setSongPlay()
                    sendAutoNextToFeed()
                }
                Action.STOP_MEDIA.value -> {
                    releaseMedia()
                }
            }
        }
        return Service.START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        releaseMedia()
    }

    override fun onCompletion(mp: MediaPlayer) {
        if (mCurrentPosition == mSongs.size - 1) {
            releaseMedia()
            sendPauseToFeed()
        } else {
            mCurrentPosition++
            setSongPlay()
            sendAutoNextToFeed()
        }
    }

    override fun onPrepared(mp: MediaPlayer) {
        if (mMediaPlayer != null) {
            mMediaPlayer!!.start()
        }
    }

    private fun releaseMedia() {
        if (mMediaPlayer != null) {
            mMediaPlayer!!.release()
            mMediaPlayer = null
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
            d("TAGGGGGG", "set Song play: ${mSongs[mCurrentPosition].url}")
            mMediaPlayer!!.setDataSource(applicationContext, Uri.parse(mSongs[mCurrentPosition].url))
            mMediaPlayer!!.prepare()
        } catch (e: IOException) {
        } catch (e: IllegalFormatException) {
        }
    }

    private fun pauseMedia() {
        if (mMediaPlayer != null) {
            if (mMediaPlayer!!.isPlaying) {
                mMediaPlayer!!.pause()
            }
        }
    }

    private fun sendPauseToFeed() {
        val i = Intent()
        i.action = Action.PAUSE.value
        sendBroadcast(i)
    }

    private fun sendAutoNextToFeed() {
        d("TAGGGGG", "cur servei:${mCurrentPosition}")
        val i = Intent()
        i.action = Action.AUTO_NEXT.value
        i.putExtra(Action.AUTO_NEXT.value, mCurrentPosition)
        sendBroadcast(i)
    }
}
