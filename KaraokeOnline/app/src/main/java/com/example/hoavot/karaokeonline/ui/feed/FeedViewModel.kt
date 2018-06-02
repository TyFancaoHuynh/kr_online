package com.example.hoavot.karaokeonline.ui.feed

import android.util.Log.d
import com.example.hoavot.karaokeonline.data.LocalRepository
import com.example.hoavot.karaokeonline.data.model.other.Comment
import com.example.hoavot.karaokeonline.data.model.other.Feed
import com.example.hoavot.karaokeonline.data.model.other.User
import com.example.hoavot.karaokeonline.data.source.KaraRepository
import com.example.hoavot.karaokeonline.data.source.response.DeleteFeedResponse
import com.example.hoavot.karaokeonline.data.source.response.FeedsResponse
import com.example.hoavot.karaokeonline.ui.extensions.observeOnUiThread
import io.reactivex.Notification
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.io.File

/**
 *
 * @author at-hoavo.
 */
class FeedViewModel(private val localRepository: LocalRepository, internal val feeds: MutableList<Feed>) {
    internal val feedsObserverable = PublishSubject.create<Notification<MutableList<Feed>>>()
    internal val isLikeFromCommentScreenObserver = PublishSubject.create<Notification<Feed>>()
    internal val commentObserverable = PublishSubject.create<Notification<MutableList<Comment>>>()
    internal val progressDilogObserverable = BehaviorSubject.create<Boolean>()
    internal val userObserverable = PublishSubject.create<User>()
    internal val startObserverable = PublishSubject.create<Notification<Feed>>()
    private val karaRepository = KaraRepository()

    internal fun getMeInfor(userId: Int) {
        karaRepository.getInforUser(userId)
                .observeOnUiThread()
                .subscribe({
                    userObserverable.onNext(it)
                }, {
                    userObserverable.onError(it)
                })
    }

    internal fun updateAvatar(avatarFile: File) = karaRepository.updateAvatarUser(avatarFile)

    internal fun saveUser(user: User) {
        localRepository.saveMeInfor(user)
    }

    internal fun getFeeds() = karaRepository.getFeeds()
            .observeOnUiThread()
            .doOnSubscribe {
                d("NNNNNNNNN", "do onSub progress")
                progressDilogObserverable.onNext(true)
            }
            .doFinally {
                d("NNNNNNNNN", "do finallly progress")
                progressDilogObserverable.onNext(false)
            }
            .onErrorReturn {
                FeedsResponse(mutableListOf())
            }
            .subscribe({
                feeds.clear()
                feeds.addAll(it.feeds)
                d("NNNNNNNNNN", "feeds: size: ${feeds.size}")
                feedsObserverable.onNext(Notification.createOnNext(feeds))
            }, {
                feedsObserverable.onNext(Notification.createOnError(it))
            })

    internal fun deleteFeed(feedId: Int): Single<DeleteFeedResponse>
            = karaRepository.deleteFeed(feedId)

    internal fun getMeFeeds(userId: Int) {
        karaRepository.getFeedMe(userId)
                .observeOnUiThread()
                .doOnSubscribe {
                    progressDilogObserverable.onNext(true)
                }
                .doFinally {
                    progressDilogObserverable.onNext(false)
                }
                .subscribe({
                    feeds.clear()
                    feeds.addAll(it.feeds)
                    feedsObserverable.onNext(Notification.createOnNext(feeds))
                }, {
                    feedsObserverable.onNext(Notification.createOnError(it))
                })
    }


    internal fun addLike(position: Int) {
        if (feeds[position].isRequesting) {
            return
        }
        karaRepository
                .postLike(feeds[position].id)
                .observeOnUiThread()
                .map {
                    feeds[position].likeCount = it.likeCount
                    feeds[position].likeFlag = if (feeds[position].likeFlag == 0) 1 else 0
                    feeds[position]
                }
                .doOnSubscribe {
                    feeds[position].isRequesting = true
                }
                .doFinally {
                    feeds[position].isRequesting = false
                }
                .subscribe({
                    isLikeFromCommentScreenObserver.onNext(Notification.createOnNext(it))
                    startObserverable.onNext(Notification.createOnNext(it))
                }, {
                    isLikeFromCommentScreenObserver.onNext(Notification.createOnError(it))
                    startObserverable.onNext(Notification.createOnError(it))
                })
    }

    internal fun removeLike(position: Int) {
        if (feeds[position].isRequesting) {
            return
        }
        karaRepository
                .postUnLike(feeds[position].id)
                .observeOnUiThread()
                .map {
                    feeds[position].likeCount = it.likeCount
                    feeds[position].likeFlag = if (feeds[position].likeFlag == 0) 1 else 0
                    feeds[position]
                }
                .doOnSubscribe {
                    feeds[position].isRequesting = true
                }
                .doFinally {
                    feeds[position].isRequesting = false
                }
                .subscribe({
                    isLikeFromCommentScreenObserver.onNext(Notification.createOnNext(it))
                    startObserverable.onNext(Notification.createOnNext(feeds[position]))
                }, {
                    startObserverable.onNext(Notification.createOnError(it))
                    isLikeFromCommentScreenObserver.onNext(Notification.createOnError(it))
                })
    }

    internal fun addComment(position: Int, comment: String) {
        karaRepository.
                postComment(feeds[position].id, comment)
                .observeOnUiThread()
                .subscribe({
                    commentObserverable.onNext(Notification.createOnNext(it.comments))
                }, {
                    commentObserverable.onNext(Notification.createOnError(it))
                })

    }

    internal fun logout() {
        localRepository.logout()
    }
}
