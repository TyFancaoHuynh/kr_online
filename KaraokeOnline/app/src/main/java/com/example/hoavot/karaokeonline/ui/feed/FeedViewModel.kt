package com.example.hoavot.karaokeonline.ui.feed

import android.support.v7.util.DiffUtil
import android.util.Log.d
import com.example.hoavot.karaokeonline.data.model.other.Comment
import com.example.hoavot.karaokeonline.data.model.other.Feed
import com.example.hoavot.karaokeonline.data.source.KaraRepository
import com.example.hoavot.karaokeonline.data.source.response.LikeResponse
import com.example.hoavot.karaokeonline.ui.base.Diff
import com.example.hoavot.karaokeonline.ui.extensions.observeOnUiThread
import io.reactivex.Notification
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

/**
 *
 * @author at-hoavo.
 */
class FeedViewModel(private val feeds: MutableList<Feed>) {
    internal val feedsObserverable = PublishSubject.create<Notification<DiffUtil.DiffResult>>()
    internal val isLikeFromCommentScreenObserver = PublishSubject.create<Notification<Feed>>()
    internal val feedsCommentObserverable = PublishSubject.create<Notification<DiffUtil.DiffResult>>()
    internal val commentObserverable = PublishSubject.create<Notification<MutableList<Comment>>>()
    internal val progressDilogObserverable = BehaviorSubject.create<Boolean>()
    private val karaRepository = KaraRepository()

    init {
        getFeeds()
    }

    private fun getFeeds() = karaRepository.getFeeds()
            .observeOnUiThread()
            .doOnSubscribe {
                progressDilogObserverable.onNext(true)
            }
            .doFinally {
                progressDilogObserverable.onNext(false)
            }
            .subscribe({
                feedsObserverable.onNext(Notification.createOnNext(updateFeedList(it.feeds)))
            }, {
                feedsObserverable.onNext(Notification.createOnError(it))
            })

    internal fun addLike(position: Int) {
        if (feeds[position].isRequesting) {
            return
        }
        karaRepository
                .postLike(feeds[position].id)
                .observeOnUiThread()
                .map {
                    updateFeedDiff(position, it)
                }
                .doOnSubscribe {
                    feeds[position].isRequesting = true
                }
                .subscribe({
                    feedsObserverable.onNext(Notification.createOnNext(it))
                }, {
                    d("TAGGGG", "error 1:${it.message}")
                    isLikeFromCommentScreenObserver.onNext(Notification.createOnError(it))
                    feeds[position].isRequesting = false
                    feedsObserverable.onNext(Notification.createOnError(it))
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
                    updateFeedDiff(position, it)
                }
                .doOnSubscribe {
                    feeds[position].isRequesting = true
                }
                .subscribe({
                    feedsObserverable.onNext(Notification.createOnNext(it))
                }, {

                    feeds[position].isRequesting = false
                    feedsObserverable.onNext(Notification.createOnError(it))
                })
    }

    internal fun addComment(position: Int, comment: String) {
        d("TAGGG", "feedId:${feeds[position].id}  comment:${comment}")
        karaRepository.
                postComment(feeds[position].id, comment)
                .observeOnUiThread()
                .subscribe({
                    commentObserverable.onNext(Notification.createOnNext(it.comments))
                }, {
                    commentObserverable.onNext(Notification.createOnError(it))
                    d("TAGGG", "error at post comment: ${it.message}")
                })

    }

    private fun updateFeedDiff(position: Int, response: LikeResponse): DiffUtil.DiffResult {
        val newList = mutableListOf<Feed>()
        newList.addAll(feeds)
        newList[position] = newList[position].copy(likeCount = response.likeCount, likeFlag = if (newList[position].likeFlag == 0) 1 else 0)
        newList[position].isRequesting = false
        isLikeFromCommentScreenObserver.onNext(Notification.createOnNext(newList[position]))
        d("TAGGG", "done ${newList[position].likeFlag}")
        return updateFeedList(newList)
    }

    private fun updateFeedList(newList: List<Feed>): DiffUtil.DiffResult {
        val diff = Diff(feeds, newList)
                .areItemsTheSame { oldItem, newItem ->
                    oldItem.id == newItem.id
                }
                .calculateDiff()
        feeds.clear()
        feeds.addAll(newList)
        return diff
    }
}
