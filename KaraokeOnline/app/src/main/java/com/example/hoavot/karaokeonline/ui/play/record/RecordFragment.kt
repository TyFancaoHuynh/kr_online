package com.example.hoavot.karaokeonline.ui.play.record

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hoavot.karaokeonline.R
import com.example.hoavot.karaokeonline.data.LocalRepository
import com.example.hoavot.karaokeonline.ui.base.BaseFragment
import com.example.hoavot.karaokeonline.ui.extensions.showAlertNotification
import com.example.hoavot.karaokeonline.ui.play.PlayActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import org.jetbrains.anko.AnkoContext

/**
 *
 * @author at-hoavo.
 */
class RecordFragment : BaseFragment() {

    companion object {
        private const val MY_PERMISSIONS_REQUEST_STORAGE = 1000
        private const val REQUEST_WRITE_PERMISSION_CODE = 1001
    }

    private lateinit var recordBottomSheetUI: RecordBottomSheetUI
    private lateinit var viewModel: RecordViewModel

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        recordBottomSheetUI = RecordBottomSheetUI()
        return recordBottomSheetUI.createView(AnkoContext.Companion.create(context, this, false))
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestAudioPermissions()
        viewModel = RecordViewModel(LocalRepository(context))
    }

    override fun onBindViewModel() {
        addDisposables(
                viewModel.updateTimeRecordObserver
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            recordBottomSheetUI.timeRecord.text = it
                        }, {

                        }),
                viewModel.lessMemoryRecordObserver
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::handleLessMemorySuccess),
                viewModel.errorWhenRecordObserver
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            context.showAlertNotification(getString(R.string.recordFragmentMesageAlertError), getString(R.string.recordFragmentTitleMessage)) {}
                        })
        )
    }

    override fun onDetach() {
        super.onDetach()
    }

//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
//        if (grantResults.isNotEmpty()
//                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            if (requestCode == MY_PERMISSIONS_REQUEST_STORAGE) {
//                (activity as? PlayActivity)?.youtubeProvider?.play()
//                recordBottomSheetUI.lnPlay.visibility = View.VISIBLE
//                (activity as PlayActivity).youtubeProvider.play()
//            }
//        }
//    }

    private fun requestAudioPermissions() {
        val storagePermissions = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        )
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                ) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
//                    && ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                    && ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.RECORD_AUDIO)) {
//            }
            requestPermissions(storagePermissions,
                    MY_PERMISSIONS_REQUEST_STORAGE)
        } else if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {

            //Go ahead with r     ecording audio now
            recordBottomSheetUI.lnPlay.visibility = View.VISIBLE
        }
        // /If permission is granted, then go ahead recording audio
    }

    internal fun eventPlayClicked() {
        recordBottomSheetUI.lnPlay.visibility = View.GONE
        recordBottomSheetUI.lnRecordings.visibility = View.GONE
        recordBottomSheetUI.lnPause.visibility = View.VISIBLE
        recordBottomSheetUI.lnStop.visibility = View.VISIBLE
        viewModel.startRecord()
    }

    internal fun eventPauseClicked() {
        recordBottomSheetUI.lnPlay.visibility = View.VISIBLE
        recordBottomSheetUI.lnPause.visibility = View.GONE
        viewModel.stopRecording(true)
    }

    internal fun eventStopClicked() {
        normalVisibleButton()
        viewModel.stopRecording(false)
    }

    private fun handleLessMemorySuccess(less: Boolean) {
        normalVisibleButton()
        context.showAlertNotification(getString(R.string.recordFragmentMesageLessMemory), getString(R.string.recordFragmentTitleMessage)) {
        }
    }

    private fun normalVisibleButton() {
        recordBottomSheetUI.lnPlay.visibility = View.VISIBLE
        recordBottomSheetUI.lnPause.visibility = View.GONE
        recordBottomSheetUI.lnRecordings.visibility = View.VISIBLE
        recordBottomSheetUI.lnStop.visibility = View.GONE
    }
}
