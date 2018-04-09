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
            ApiClient.getInstance(null).token = localRepository.getApiToken()
        }
    }
}