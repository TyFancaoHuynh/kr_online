package com.example.hoavot.karaokeonline.ui.login

import com.example.hoavot.karaokeonline.data.LocalRepository
import com.example.hoavot.karaokeonline.data.model.other.User
import com.example.hoavot.karaokeonline.data.source.KaraRepository
import com.example.hoavot.karaokeonline.data.source.response.LoginResponse
import io.reactivex.Single

/**
 * Created by TienHuynh3 on 12/05/2018.
 * Copyright © AsianTech inc...
 */
class LoginViewModel(private val localRepository: LocalRepository, private val karaRepository: KaraRepository) {

    internal fun hasAppToken(): String {
        return localRepository.getApiToken()
    }

    internal fun login(username: String, password: String): Single<LoginResponse> {
        return karaRepository.login(username, password)
    }

    internal fun register(username: String, password: String): Single<LoginResponse> {
        return karaRepository.register(username, password)
    }

    internal fun saveInfor(token: String, user: User) {
        localRepository.saveApiToken(token)
        localRepository.saveMeInfor(user)
    }
}