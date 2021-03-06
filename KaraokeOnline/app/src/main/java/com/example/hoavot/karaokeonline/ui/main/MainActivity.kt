package com.example.hoavot.karaokeonline.ui.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log.d
import com.example.hoavot.karaokeonline.data.source.api.ApiClient
import com.example.hoavot.karaokeonline.ui.base.BaseActivity
import com.example.hoavot.karaokeonline.ui.extensions.showAlertError
import com.example.hoavot.karaokeonline.ui.extensions.showSettingPermissionAlert
import org.jetbrains.anko.setContentView

/**
 *
 * @author at-hoavo.
 */
class MainActivity : BaseActivity() {
    companion object {
        private const val REQUEST_GALLERY_PERMISSION_CODE = 10
        private const val REQUEST_CAMERA_PERMISSION_CODE = 11
        private const val REQUEST_ACCESS_RECORD_PERMISSION_CODE = 12
    }

    private lateinit var ui: MainActivityUI
    private var askedPermissionTime = 0L
    internal var isAccess = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        d("TAGGGG", "on create main")
        checkPermissionAccessRecord()
        val mainTabs = listOf(MainTab(MainTab.TabItemType.ITEM_HOME),
                MainTab(MainTab.TabItemType.ITEM_USER),
                MainTab(MainTab.TabItemType.ITEM_SEARCH))
        ui = MainActivityUI(mainTabs)
        ui.setContentView(this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == REQUEST_CAMERA_PERMISSION_CODE) {
                checkPermissionGallery()
            }
            if (requestCode == REQUEST_ACCESS_RECORD_PERMISSION_CODE) {
                checkPermissionCamera()
            }
        } else {
            showSettingPermissionAlert({
                startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:$packageName")))
            })
        }
    }

    override fun onBindViewModel() {
        // Todo: Handle later
    }

    internal fun getCurrentContainerFragment() = ui.mainPagerAdapter.getItem(ui.viewPager.currentItem)

    private fun checkPermissionGallery() {
        if (ContextCompat.checkSelfPermission(this
                , Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this
                , Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                askedPermissionTime = System.currentTimeMillis()
                ActivityCompat.requestPermissions(this
                        , arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_GALLERY_PERMISSION_CODE)
            }
        } else {
            isAccess = true
        }
    }

    private fun checkPermissionCamera() {
        if (ContextCompat.checkSelfPermission(this
                , Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(this
                        , arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION_CODE)
            }
        } else {
            checkPermissionGallery()
        }
    }

    private fun checkPermissionAccessRecord() {
        val storagePermissions = arrayOf(
                Manifest.permission.RECORD_AUDIO
        )
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(this, storagePermissions, REQUEST_ACCESS_RECORD_PERMISSION_CODE)
            }
        } else {
            checkPermissionCamera()
        }
    }
}
