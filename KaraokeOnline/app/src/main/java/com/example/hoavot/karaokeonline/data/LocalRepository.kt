package com.example.hoavot.karaokeonline.data

import android.content.Context
import android.util.Log.d
import com.bumptech.glide.Glide
import com.example.hoavot.karaokeonline.BuildConfig
import com.example.hoavot.karaokeonline.data.source.LocalDataSource
import com.example.hoavot.karaokeonline.data.source.api.ApiClient
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.SingleSubject
import java.io.*


/**
 *
 * @author at-hoavo.
 */
class LocalRepository(private val context: Context) : LocalDataSource {
    companion object {
        private const val KEY_API_TOKEN = "KEY_API_TOKEN"
    }

    private val pref by lazy {
        context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)
    }

    private fun deleteDir(dir: File): Boolean = when {
        dir.isDirectory -> {
            val children = dir.list()
            children.forEach {
                deleteDir(File(dir, it))
            }
            dir.delete()
        }
        dir.isFile -> dir.delete()
        else -> false
    }

    private fun deleteCache(): Boolean = try {
        val dir = context.cacheDir
        deleteDir(dir)
    } catch (e: IOException) {
        false
    }

    override fun saveApiToken(deviceToken: String) {
        pref.edit().putString(KEY_API_TOKEN, deviceToken).apply()
    }

    override fun getApiToken(): String = pref.getString(KEY_API_TOKEN, "")

    override fun logout(): Single<Boolean> {
        pref.edit().clear().apply()
        ApiClient.getInstance(null).token = null
        Runnable {
            Glide.get(context).clearDiskCache()
        }
        return SingleSubject.fromCallable {
            deleteCache()
        }
    }

    private val checkMemoryObserver = BehaviorSubject.create<Boolean>()

    override fun checkMemoryUsage(): BehaviorSubject<Boolean> {
        try {
            val reader = BufferedReader(InputStreamReader(FileInputStream("/proc/stat")), 1000)
            val load = reader.readLine()
            reader.close()

            val toks = load.split(" ".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()

            val currTotal = java.lang.Long.parseLong(toks[2]) + java.lang.Long.parseLong(toks[3]) + java.lang.Long.parseLong(toks[4])
            val currIdle = java.lang.Long.parseLong(toks[5])
            val check = (currIdle / currTotal.toFloat()) * 100
            d("TAGG", "currIdle: $currIdle" + "total: $currTotal" + "check: $check")
            // Handle current memory idle < 5%
            if (check < 5) {
                checkMemoryObserver.onNext(true)
            }
        } catch (ex: IOException) {
            checkMemoryObserver.onError(ex)
        }
        return checkMemoryObserver
    }


}