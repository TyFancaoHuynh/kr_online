package com.example.hoavot.karaokeonline.ui.feed.comment

import android.support.v7.util.DiffUtil
import com.example.hoavot.karaokeonline.data.model.nomal.FeedEvent
import com.example.hoavot.karaokeonline.data.model.other.Comment
import com.example.hoavot.karaokeonline.data.model.other.Feed
import com.example.hoavot.karaokeonline.data.source.KaraRepository
import com.example.hoavot.karaokeonline.ui.base.Diff
import com.example.hoavot.karaokeonline.ui.extensions.RxBus
import com.example.hoavot.karaokeonline.ui.extensions.observeOnUiThread
import io.reactivex.Notification
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

/**
 *
 * @author at-hoavo.
 */
class CommentViewModel(private val feed: Feed, private val position: Int) {

    internal val feedsCommentObserverable = PublishSubject.create<Notification<DiffUtil.DiffResult>>()
    internal val progressDilogObserverable = BehaviorSubject.create<Boolean>()
    private val karaRepository = KaraRepository()
    private val comments = mutableListOf<Comment>()

    internal fun addComment(position: Int, comment: String) {
        karaRepository.postComment(feed.id, comment)
                .observeOnUiThread()
                .subscribe({
                    feedsCommentObserverable.onNext(Notification.createOnNext(updateCommentList(position, it.comments)))
                }, {
                    feedsCommentObserverable.onNext(Notification.createOnError(it))
                })
    }

    private fun updateCommentList(position: Int, newList: List<Comment>): DiffUtil.DiffResult {
        val diff = Diff(comments, newList)
                .areItemsTheSame { oldItem, newItem ->
                    oldItem.id == newItem.id
                }
                .calculateDiff()
        comments.clear()
        comments.addAll(newList)
        RxBus.publish(FeedEvent(position, comments))
        return diff
    }
}
