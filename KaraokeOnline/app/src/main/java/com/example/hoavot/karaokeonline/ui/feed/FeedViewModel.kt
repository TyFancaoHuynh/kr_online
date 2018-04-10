package com.example.hoavot.karaokeonline.ui.feed

import android.support.v7.util.DiffUtil
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
    internal val feedsCommentObserverable = PublishSubject.create<Notification<DiffUtil.DiffResult>>()
    internal val progressDilogObserverable = BehaviorSubject.create<Boolean>()
    private val karaRepository = KaraRepository()

    init {
        val comments = mutableListOf<Comment>()
        comments.add(Comment(1, "", "bfjavabkakbc", 1))
        comments.add(Comment(1, "", "bfjavabkakbc", 1))
        comments.add(Comment(1, "", "bfjavabkakbc", 1))
        comments.add(Comment(1, "", "bfjavabkakbc", 1))
        val feeds = mutableListOf<Feed>()
        feeds.add(Feed(0, "sjhha", "", "vchcahcxhx", "", 1436, 86, comments, false))
        feeds.add(Feed(1, "sjhha", "", "vchcahcxhx", "", 1436, 86, comments, false))
        feeds.add(Feed(2, "sjhha", "", "vchcahcxhx", "", 1436, 86, comments, true))
        feeds.add(Feed(3, "sjhha", "", "vchcahcxhx", "", 1436, 86, comments, false))
        feeds.add(Feed(4, "sjhha", "", "vchcahcxhx", "", 1436, 86, comments, true))
        feeds.add(Feed(5, "sjhha", "", "vchcahcxhx", "", 1436, 86, comments, false))
        feeds.add(Feed(6, "sjhha", "", "vchcahcxhx", "", 1436, 86, comments, true))
        feeds.add(Feed(7, "sjhha", "", "vchcahcxhx", "", 1436, 86, comments, false))
        feedsObserverable.onNext(Notification.createOnNext(updateFeedList(feeds)))
//        getFeeds()
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

    internal fun removeLike(position: Int) {
        if (feeds[position].isRequesting) {
            return
        }
        karaRepository
                .postUnLike(feeds[position].id)
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
        karaRepository.postComment(feeds[position].id, comment)
                .observeOnUiThread()
                .subscribe({
                    feedsCommentObserverable.onNext(Notification.createOnNext(updateCommentList(position, it.comments)))
                }, {
                    feedsCommentObserverable.onNext(Notification.createOnError(it))
                })
    }

    private fun updateFeedDiff(position: Int, response: LikeResponse): DiffUtil.DiffResult {
        val newList = mutableListOf<Feed>()
        newList.addAll(feeds)
        newList[position] = newList[position].copy(likeCount = response.likeCount, likeFlag = !newList[position].likeFlag)
        newList[position].isRequesting = false
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

    private fun updateCommentList(position: Int, newList: List<Comment>): DiffUtil.DiffResult {
        val diff = Diff(feeds[position].comments, newList)
                .areItemsTheSame { oldItem, newItem ->
                    oldItem.id == newItem.id
                }
                .calculateDiff()
        feeds[position].comments.clear()
        feeds[position].comments.addAll(newList)
        return diff
    }
}
