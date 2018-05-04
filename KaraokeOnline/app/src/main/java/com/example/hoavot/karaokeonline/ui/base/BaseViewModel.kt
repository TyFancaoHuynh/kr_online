package com.example.hoavot.karaokeonline.ui.base

import com.example.hoavot.karaokeonline.data.LocalRepository
import com.example.hoavot.karaokeonline.data.source.api.ApiClient

/**
 *
 * @author at-hoavo.
 */
class BaseViewModel(private val localRepository: LocalRepository) : BaseVMContract {
    override fun logOut() = localRepository.logout()

    override fun reInitApiToken() {
        if (ApiClient.getInstance(null).token == null) {
//            ApiClient.getInstance(null).token = localRepository.getApiToken()
            ApiClient.getInstance(null).token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6InJpbW1tbSIsInBhc3N3b3JkIjoiaDU4NTgiLCJpYXQiOjE1MjQ0OTg5MTcsImV4cCI6MTUyNDU4NTMxN30.C9G0S1kVhlFDpVFAizIWJiS2Xr2W87NZLP5M6XIgMzM"
        }
    }
}