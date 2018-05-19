package com.example.hoavot.karaokeonline.ui.feed.like

import com.example.hoavot.karaokeonline.data.source.KaraRepository
import com.example.hoavot.karaokeonline.data.source.response.UserLikeResponse
import io.reactivex.Single

/**
 *
 * @author at-hoavo
 */
class LikeViewModel(private val karaRepository: KaraRepository) {

    internal fun getListUserLike(feedId: Int): Single<UserLikeResponse> {
        return karaRepository.getListUserLike(feedId)
    }
}