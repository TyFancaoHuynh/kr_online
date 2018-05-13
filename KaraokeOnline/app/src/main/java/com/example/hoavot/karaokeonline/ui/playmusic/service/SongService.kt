package com.example.hoavot.karaokeonline.ui.playmusic.service

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.CountDownTimer
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.util.Log.d
import com.example.hoavot.karaokeonline.ui.playmusic.PlayFragment
import com.example.hoavot.karaokeonline.ui.playmusic.model.Song

import java.io.IOException
import java.util.IllegalFormatException
import java.util.Random

/**
 * Copyright © 2017 AsianTech inc.
 * Created by at-hoavo on 01/07/2017.
 */
class SongService : Service(), MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    private var mMediaPlayer: MediaPlayer? = null
    private var mSongs: List<Song>? = null
    private var mCurrentPosition: Int = 0
    private var mCheck: Boolean = false
    private var mCheckAutoNext: Boolean = false
    private var mCheckShuffle: Boolean = false
    private var mCountDownTimer: CountDownTimer? = null
    private val randomPosition: Int
        get() = Random().nextInt(mSongs!!.size)

    override fun onBind(intent: Intent): IBinder? {
        // No-op
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            if (intent.action == Action.SONGS.value) {
                d("TAGGGGGG", "action song value")
                mSongs = intent.getParcelableArrayListExtra<Song>(PlayFragment.TYPE_SONGS).toList()
            } else if (intent.action == Action.CHOOSE_PLAY.value) {
                mCurrentPosition = intent.getIntExtra(PlayFragment.TYPE_POSITION, 0)
                setSongPlay()
            } else if (intent.action == Action.PAUSE.value) {
                if (!mCheck) {
                    setSongPlay()
                    mCheck = true
                } else {
                    if (mMediaPlayer != null) {
                        mMediaPlayer!!.start()
                    }
                }
            } else if (intent.action == Action.PLAY.value) {
                d("TAGGGGGG", "action play")
                if (mMediaPlayer != null) {
                    if (mMediaPlayer!!.isPlaying) {
                        mMediaPlayer!!.pause()
                    }
                }
            } else if (intent.action == Action.NEXT.value) {
                if (mCheckShuffle) {
                    mCurrentPosition = randomPosition
                } else {
                    mCurrentPosition++
                    if (mCurrentPosition == mSongs!!.size) {
                        mCurrentPosition = 0
                    }
                }
                setSongPlay()
                sendPositionToActivity()
            } else if (intent.action == Action.PREVIOUS.value) {
                if (mCheckShuffle) {
                    mCurrentPosition = randomPosition
                } else {
                    mCurrentPosition--
                    if (mCurrentPosition < 0) {
                        mCurrentPosition = mSongs!!.size - 1
                    }
                }
                setSongPlay()
                sendPositionToActivity()
            } else if (intent.action == Action.SEEK_TO.value) {
                val time = intent.getIntExtra(PlayFragment.TYPE_CHOOSE_TIME, 0)
                mMediaPlayer!!.seekTo(time)
                mMediaPlayer!!.start()
            } else if (intent.action == Action.AUTO_NEXT.value) {
                mCheckAutoNext = true
            } else if (intent.action == Action.AUTO_NEXT_SELETED.value) {
                mCheckAutoNext = false
            } else if (intent.action == Action.SHUFFLE.value) {
                mCheckShuffle = true
            } else if (intent.action == Action.SHUFFLE_SELECTED.value) {
                mCheckShuffle = false
            } else if (intent.action == Action.STOP_MEDIA.value) {
                releaseMedia()
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
    }

    override fun onCompletion(mp: MediaPlayer) {
        if (mCheckAutoNext) {
            if (mCheckShuffle) {
                mCurrentPosition = randomPosition
            } else {
                if (mCurrentPosition == mSongs!!.size - 1) {
                    mCurrentPosition = 0
                } else {
                    mCurrentPosition++
                }
            }
            setSongPlay()
            sendPositionToActivity()
        }
    }

    override fun onPrepared(mp: MediaPlayer) {
        if (mMediaPlayer != null) {
            mMediaPlayer!!.start()
            setProgress()
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
            mMediaPlayer!!.setDataSource(applicationContext, Uri.parse(mSongs!![mCurrentPosition].url))
            mMediaPlayer!!.prepare()
            Log.d(TAG, "onStartCommand: " + mCurrentPosition)
        } catch (e: IOException) {
            Log.d(TAG, "IOException of MediaPlayer ")
        } catch (e: IllegalFormatException) {
            Log.d(TAG, " IllegalFormatException of MediaPlayer")
        }

    }

    // Update current position of Activity
    private fun sendPositionToActivity() {
        val i = Intent()
        i.action = Action.SEND_POSITION.value
        i.putExtra(PlayFragment.TYPE_POSITION, mCurrentPosition)
        sendBroadcast(i)
    }

    // Update Seekbar
    private fun setProgress() {
        val timeIntent = Intent(Action.SEEK.value)
        mCountDownTimer = object : CountDownTimer(mMediaPlayer!!.duration.toLong(), 1000) {
            override fun onTick(l: Long) {
                if (mMediaPlayer != null) {
                    timeIntent.putExtra(PlayFragment.TYPE_TIME, mMediaPlayer!!.duration.toString() + "")
                    timeIntent.putExtra(PlayFragment.TYPE_SECOND, mMediaPlayer!!.currentPosition.toString() + "")
                    timeIntent.putExtra(PlayFragment.TYPE_POSITION, mCurrentPosition)
                    d("TAGGGG", "time: ${mMediaPlayer!!.duration} current:${mMediaPlayer!!.currentPosition}")
                    sendBroadcast(timeIntent)
                }
            }

            override fun onFinish() {
                // No-op
            }
        }
        mCountDownTimer!!.start()
    }

    private fun releaseMedia() {
        if (mMediaPlayer != null) {
            mMediaPlayer!!.release()
            mMediaPlayer = null
        }
    }

    companion object {
        private val TAG = SongService::class.java.name
    }
}
