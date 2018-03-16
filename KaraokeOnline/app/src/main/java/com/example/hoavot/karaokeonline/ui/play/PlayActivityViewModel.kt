package com.example.hoavot.karaokeonline.ui.play

import android.text.TextUtils
import android.util.Log.d
import com.example.hoavot.karaokeonline.data.model.nomal.*
import com.example.hoavot.karaokeonline.data.source.KaraRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

/**
 *
 * @author at-hoavo.
 */
class PlayActivityViewModel(internal var videoId: String, private val karaRepository: KaraRepository) {
    companion object {
        private const val INVISIBLE_THRESHOLD_FOR_LOAD_MORE = 100
    }

    constructor(videoId: String) : this(videoId, KaraRepository()) {
        loadMoreVideo()
    }

    internal var items = mutableListOf<Item>()
    internal val itemsObserver = PublishSubject.create<MutableList<Item>>()
    private var isVideoLastItem = false
    private var isVideoLoading = false
    private var currentPage = 1

    internal fun loadMoreVideo(page: Int = 1) {
        items.clear()
        karaRepository
                .getMoreVideos("snippet", "completed", "25", videoId, "video")
                .subscribeOn(Schedulers.io())
                .toObservable()
                .flatMap {
                    Observable.fromIterable(it)
                }
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
                    d("TAGG","error: ${it.message}")
                    Item(Video(Id("", "", "")),
                            Snippet("", "", "", "", Thumbnails(Medium("")),
                                    "", ResourceId(""), 0),
                            null, null, null)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .filter {
                    it.video.id.videoId != "" ||
                            it.video.id.playlistId != "" ||
                            it.video.id.channelId != ""
                }
                .subscribe({
                    items.add(it)
                    itemsObserver.onNext(items)
                }, {})

    }

    private fun searchDetailVideo(part: String, video: Video): Observable<Item> {
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
                    Item(video, it[0].snippet, null, null, null)
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
                        Item(video!!, it[0].snippet, null, null, null)
                    }
                }
    }
}
