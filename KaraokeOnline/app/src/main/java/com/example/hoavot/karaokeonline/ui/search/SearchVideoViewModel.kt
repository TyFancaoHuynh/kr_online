package com.example.hoavot.karaokeonline.ui.search

import android.text.TextUtils
import com.example.hoavot.karaokeonline.data.model.nomal.*
import com.example.hoavot.karaokeonline.data.source.KaraRepository
import com.example.hoavot.karaokeonline.ui.extensions.observeOnUiThread
import io.reactivex.Notification
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.SingleSubject
import java.util.concurrent.TimeUnit

/**
 *
 * @author at-hoavo.
 */
class SearchVideoViewModel {
    companion object {
        private const val WAITING_TIME_FOR_SEARCH_FUNCTION = 300L
    }

    internal var items = mutableListOf<Item>()
    internal val triggerSearchObserver = PublishSubject.create<String>()
    internal var videoPopulars = PublishSubject.create<Notification<MutableIterable<Item>>>()
    internal val itemsObserver = BehaviorSubject.create<MutableList<Item>>()
    internal val queryObserver = SingleSubject.create<String>()
    internal val progressDialogObserverable = BehaviorSubject.create<Notification<Boolean>>()
    private val karaRepository = KaraRepository()

    init {
        triggerOnSearchButtonClick()
    }

    internal fun getVideoPopular(part: String = "snippet,contentDetails", chart: String = "mostPopular", regionCode: String = "vn") {
        karaRepository.getPopularVideo(part, chart, regionCode)
                .observeOnUiThread()
                .toObservable()
                .flatMap { Observable.fromIterable(it) }
                .map {
                    val video = Video(Id(it.id))
                    it.video = video
                    it
                }
                .toList()
                .subscribe({
                    videoPopulars.onNext(Notification.createOnNext(it))
                }, {
                    videoPopulars.onNext(Notification.createOnError(it))
                })


    }

    private fun loadMoreVideo(part: String, q: String, page: Int = 1): Observable<MutableList<Item>> {
        return karaRepository
                .getVideoSearchFromApi(part, q)
                .observeOn(Schedulers.io())
                .doOnSubscribe {
                    progressDialogObserverable.onNext(Notification.createOnNext(true))
                }
                .flatMap { Observable.fromIterable(it) }
                .flatMap {
                    if (!TextUtils.isEmpty(it.id.videoId)) {
                        searchDetailVideo("snippet,contentDetails,statistics", it)
                    } else if (!TextUtils.isEmpty(it.id.playlistId)) {
                        getPlayListSearch("snippet", it)
                    } else {
                        getChannelDetail("snippet", null, it, false)
                    }
                }
                .onErrorReturn {
                    progressDialogObserverable.onNext(Notification.createOnError(it))
                    Item("", Video(Id("", "", "")),
                            Snippet("", "", "", "", Thumbnails(Medium("")),
                                    "", ResourceId(""), 0),
                            null, null, null)
                }
                .filter {
                    it.video.id.videoId != "" ||
                            it.video.id.playlistId != "" ||
                            it.video.id.channelId != ""
                }
                .toList()
                .toObservable()
    }

    internal fun searchDetailVideo(part: String, video: Video): Observable<Item> {
        return karaRepository.getVideoDetail(part, video.id.videoId).doOnNext {
            it.video = video
            getChannelDetail("snippet", it, null, true)
        }
    }

    private fun getPlayListSearch(part: String, video: Video): Observable<Item> {
        var totalsPlayList = 0
        return karaRepository
                .getPlaylistSearch(part, video.id.playlistId)
                .doOnNext {
                    karaRepository
                            .getPlaylistDetails("snippet,contentDetails", video.id.playlistId)
                            .doOnNext {
                                totalsPlayList = it.pageInfo.totalResults
                            }
                            .subscribe()
                }
                .map {
                    it[0].snippet.totalPlaylist = totalsPlayList
                    Item(video.id.videoId, video, it[0].snippet, null, null, null)
                }
    }

    private fun getChannelDetail(part: String, item: Item?, video: Video?, check: Boolean): Observable<Item> {
        var id = ""
        if (check) {
            id = item?.snippet?.channelId!!
        } else {
            id = video?.id?.channelId!!
        }
        return karaRepository
                .getChannelDetail(part, id)
                .map {
                    if (check) {
                        item?.channel = it[0]
                        item
                    } else {
                        Item(video?.id?.videoId!!, video!!, it[0].snippet, null, null, null)
                    }
                }
    }

    private fun triggerOnSearchButtonClick() {
        triggerSearchObserver
                .debounce(WAITING_TIME_FOR_SEARCH_FUNCTION, TimeUnit.MILLISECONDS)
                .filter { it != "" }
                .flatMap {
                    items.clear()
                    queryObserver.onSuccess(it)
                    loadMoreVideo("snippet", it)
                }
                .subscribe({
                    progressDialogObserverable.onNext(Notification.createOnNext(false))
//                    items.add(it)
                    itemsObserver.onNext(it)
                }, {})
    }
}
