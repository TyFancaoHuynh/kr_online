package com.example.hoavot.karaokeonline.ui.profile.edit

import com.example.hoavot.karaokeonline.data.LocalRepository
import com.example.hoavot.karaokeonline.data.model.other.User
import com.example.hoavot.karaokeonline.data.source.KaraRepository
import com.example.hoavot.karaokeonline.ui.extensions.observeOnUiThread
import io.reactivex.subjects.PublishSubject

/**
 *
 * @author at-hoavo.
 */
class EditProfileViewModel(private val localRepository: LocalRepository) {
    private val karaRepository = KaraRepository()
    internal val updateUserObserver = PublishSubject.create<User>()

    internal fun getMeInfor() = localRepository.getMeInfor()

    internal fun updateProfile(user: User) {
        karaRepository
                .updateInforUser(user)
                .observeOnUiThread()
                .subscribe({
                    updateUserObserver.onNext(it)
                }, {
                    updateUserObserver.onError(it)
                })
    }

    internal fun saveUser(user: User) {
        localRepository.saveMeInfor(user)
    }
}
