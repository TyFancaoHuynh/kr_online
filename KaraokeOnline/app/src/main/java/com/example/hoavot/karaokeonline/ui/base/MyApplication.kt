package com.example.hoavot.karaokeonline.ui.base

import android.app.Application
import android.content.pm.PackageManager
import android.util.Base64
import android.util.Log.d
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


/**
 *
 * @author at-hoavo
 */
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        printHashKey()
    }

    /**
     * Method that prints hash key.
     */
    fun printHashKey() {
        try {
            val info = packageManager.getPackageInfo(
                    "com.tutorialwing.androidfacebookshareimagetutorial",
                    PackageManager.GET_SIGNATURES)

            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                d("TAGGGGGG", "KeyHash:${Base64.encodeToString(md.digest(), Base64.DEFAULT)}")
            }
        } catch (e: PackageManager.NameNotFoundException) {
        } catch (e: NoSuchAlgorithmException) {
        }
    }

}
