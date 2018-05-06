package com.example.hoavot.karaokeonline.ui.profile

import com.example.hoavot.karaokeonline.data.LocalRepository
import com.example.hoavot.karaokeonline.data.model.other.User
import com.example.hoavot.karaokeonline.data.source.KaraRepository
import java.io.File

/**
 *
 * @author at-hoavo.
 */
class ProfileViewModel(private val localRepository: LocalRepository) {
    private val karaRepository = KaraRepository()
    internal fun getMeInfor() = localRepository.getMeInfor()

    internal fun updateAvatar(avatarFile: File) = karaRepository.updateAvatarUser(avatarFile)

    internal fun saveUser(user: User) {
        localRepository.saveMeInfor(user)
    }
}