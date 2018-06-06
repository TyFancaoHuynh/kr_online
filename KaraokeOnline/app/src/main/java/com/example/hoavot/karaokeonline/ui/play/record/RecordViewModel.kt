package com.example.hoavot.karaokeonline.ui.play.record

import android.media.MediaRecorder
import android.os.Build
import android.os.Environment
import android.os.SystemClock
import android.support.annotation.RequiresApi
import android.util.Log
import android.util.Log.d
import android.util.Log.e
import com.example.hoavot.karaokeonline.data.LocalRepository
import com.example.hoavot.karaokeonline.ui.extensions.observeOnUiThread
import com.example.hoavot.karaokeonline.ui.extensions.toTimer
import io.reactivex.Notification
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


/**
 *
 * @author at-hoavo.
 */
class RecordViewModel(private val localRepository: LocalRepository) {
    internal val updateTimeRecordObserver = BehaviorSubject.create<String>()
    internal val lessMemoryRecordObserver = BehaviorSubject.create<Boolean>()
    internal val errorWhenRecordObserver = BehaviorSubject.create<Notification<Boolean>>()
    private val handleTimeDispose: CompositeDisposable = CompositeDisposable()
    private lateinit var startDispose: Disposable
    private var mRecorder: MediaRecorder? = null
    private var isPause = false
    internal var mOutputFile: File? = null
    private var mStartTime = 0L
    private var timeWait = 0L

    private fun initRecord() {
        if (mRecorder == null) {
            d("MMMMMMMMM", "init ")
            mRecorder = MediaRecorder()
            mRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
            mRecorder?.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.HE_AAC)
                mRecorder?.setAudioEncodingBitRate(48000)
            } else {
                mRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                mRecorder?.setAudioEncodingBitRate(64000)
            }
            mRecorder?.setAudioSamplingRate(16000)
            setOutputType()
            mRecorder?.setOutputFile(mOutputFile?.getAbsolutePath())
            mRecorder?.prepare()
        }
    }

    private fun setOutputType() {
        val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmssSSS", Locale.getDefault())
        if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
            //handle case of no SDCARD present
        } else {
            val dir = Environment.getExternalStorageDirectory().toString() + File.separator + "/Voice Recorder/RECORDING"
            //create folder
            val folder = File(dir)
            if (!folder.exists()) {
                folder.mkdirs()
            }
            mOutputFile = File(dir, "Recording" + dateFormat.format(Date()) +
                    ".m4a")
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    internal fun startRecord() {
        handleTimeDispose.clear()
        startDispose = Observable
                .fromCallable {
                    initRecord()
                }
                .doOnNext {
                    if (isPause) {
                        mRecorder?.resume()
                    } else {
                        mRecorder?.start()
                    }
                    val time = Observable.interval(1, TimeUnit.SECONDS)
                            .doOnSubscribe {
                                d("MMMMMMMMM", "dOS with time")
                                if (!isPause) {
                                    mStartTime = SystemClock.elapsedRealtime()
                                } else {
                                    mStartTime += timeWait * 1000+3000
                                }
                            }
                            .subscribe({
                                d("MMMMMMMMM", "do on subscribe start Time: ${mStartTime}")
                                tick()
                            }, {
                                d("MMMMMMMM", "error at time:${it.message}")
                            })
                    handleTimeDispose.add(time)
                }
//                .flatMap {
//                    checkMemoryUsage()
//                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    //                    stopRecording(true)
//                    lessMemoryRecordObserver.onNext(true)
                }, {
                    errorWhenRecordObserver.onNext(Notification.createOnError(it))
                    Log.e("TAGGGGG", "error record: ${it.message}")
                })
    }

    private fun checkMemoryUsage() = localRepository.checkMemoryUsage()

    private fun tick() {
        d("TAGGGGGGGG", "timeWait: ${timeWait}")
        d("TAGGGGGGGG", "curent : ${SystemClock.elapsedRealtime()} start: ${mStartTime}")

        val time = (if (mStartTime < 0) 0 else SystemClock.elapsedRealtime() - mStartTime).toLong()
        val minutes = (time / 60000).toInt()
        val seconds = (time / 1000).toInt() % 60
        d("TAGGGGGGG", "start tick: ${minutes.toTimer(seconds)}   + time: ${time}")
        updateTimeRecordObserver.onNext(minutes.toTimer(seconds))
    }

    internal fun stopRecording(saveFile: Boolean) {
        try {
            mRecorder?.stop()
            mRecorder?.release()
        } catch (e: RuntimeException) {
            e("TAGGG", "runtime error: ${e.message}")
        }
        mRecorder = null
        isPause = false
        handleTimeDispose.clear()
        startDispose.dispose()
        mStartTime = 0
        timeWait = 0L
        if (!saveFile && mOutputFile != null) {
            mOutputFile?.delete()
        }
        updateTimeRecordObserver.onNext("00:00")
    }

    @RequiresApi(Build.VERSION_CODES.N)
    internal fun pauseRecording() {
        isPause = true
        handleTimeDispose.clear()
        val time = Observable.interval(1, TimeUnit.SECONDS)
                .observeOnUiThread()
                .subscribe({
                    timeWait = it
                    d("TAGGGGGGGGGG", "timeWait at pause: ${timeWait}")
                })
        handleTimeDispose.add(time)
        d("MMMMMMMMM", "record start time: ${mStartTime}")
        mRecorder?.pause()
    }
}
