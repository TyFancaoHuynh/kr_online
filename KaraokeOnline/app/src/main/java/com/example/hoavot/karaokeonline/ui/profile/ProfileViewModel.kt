package com.example.hoavot.karaokeonline.ui.profile

import com.example.hoavot.karaokeonline.data.LocalRepository
import com.example.hoavot.karaokeonline.data.model.other.Feed
import com.example.hoavot.karaokeonline.data.model.other.User
import com.example.hoavot.karaokeonline.data.source.KaraRepository
import com.example.hoavot.karaokeonline.ui.extensions.observeOnUiThread
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.io.File

/**
 *
 * @author at-hoavo.
 */
class ProfileViewModel(private val localRepository: LocalRepository) {
    private val karaRepository = KaraRepository()
    internal val feedMeObserver = PublishSubject.create<MutableList<Feed>>()
    internal val progressDialog = BehaviorSubject.create<Boolean>()


    internal fun getMeInfor() = localRepository.getMeInfor()

    internal fun updateAvatar(avatarFile: File) = karaRepository.updateAvatarUser(avatarFile)

    internal fun saveUser(user: User) {
        localRepository.saveMeInfor(user)
    }

    internal fun getMeFeeds() {
        karaRepository.getFeedMe()
                .observeOnUiThread()
                .doOnSubscribe {
                    progressDialog.onNext(true)
                }
                .doFinally {
                    progressDialog.onNext(false)
                }
                .subscribe({
                    feedMeObserver.onNext(it.feeds)
                }, {
                    feedMeObserver.onError(it)
                })
    }
}