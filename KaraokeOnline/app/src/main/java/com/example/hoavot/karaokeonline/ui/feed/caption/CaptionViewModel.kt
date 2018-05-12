package com.example.hoavot.karaokeonline.ui.feed.caption

import android.util.Log.d
import com.example.hoavot.karaokeonline.data.LocalRepository
import com.example.hoavot.karaokeonline.data.model.other.User
import com.example.hoavot.karaokeonline.data.source.KaraRepository
import com.example.hoavot.karaokeonline.data.source.response.FeedResponse
import com.example.hoavot.karaokeonline.ui.extensions.observeOnUiThread
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.SingleSubject
import java.io.File

/**
 *
 * @author at-hoavo.
 */
class CaptionViewModel(private val localRepository: LocalRepository) {
    private val karaRepository = KaraRepository()
    internal val userInforObserver = SingleSubject.create<User>()
    internal val progressObserver = BehaviorSubject.create<Boolean>()
    internal val postFeedObserver = PublishSubject.create<FeedResponse>()

    internal fun getUserFromSharePrefrence() {
        userInforObserver.onSuccess(localRepository.getMeInfor())
    }

    internal fun postFeed(fileName: String, file: File?, caption: String) {
        d("TAGGGGG", "post feed")
        karaRepository.postFeed(fileName,file, caption)
                .observeOnUiThread()
                .doOnSubscribe {
                    d("TAGGGG", "do On suscribe")
                    progressObserver.onNext(true)
                }
                .doFinally {
                    d("TAGGGG", "do finally")
                    progressObserver.onNext(false)
                }
                .subscribe({
                    postFeedObserver.onNext(it)
                }, {
                    // Todo: Handle later
                    d("TAGGGG", "error: ${it.message}")
                })
    }
}