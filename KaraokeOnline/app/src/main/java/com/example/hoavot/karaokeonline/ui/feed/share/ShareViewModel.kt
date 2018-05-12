package com.example.hoavot.karaokeonline.ui.feed.share

import com.example.hoavot.karaokeonline.data.LocalRepository

/**
 *
 * @author at-hoavo
 */
class ShareViewModel(private val localRepository: LocalRepository) {

    fun getUser() = localRepository.getMeInfor()
}