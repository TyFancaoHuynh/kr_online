package com.example.hoavot.karaokeonline.data.source.remote

import com.example.hoavot.karaokeonline.data.model.nomal.Channel
import com.example.hoavot.karaokeonline.data.model.nomal.Item
import com.example.hoavot.karaokeonline.data.model.nomal.Playlist
import com.example.hoavot.karaokeonline.data.model.nomal.Video
import com.example.hoavot.karaokeonline.data.model.remote.PlaylistDetailFromApi
import com.example.hoavot.karaokeonline.data.source.KaraDataSource
import com.example.hoavot.karaokeonline.data.source.api.ApiClient
import io.reactivex.Observable
import io.reactivex.Single

/**
 *  Copyright © 2017 AsianTech inc.
 *  Created by hoavot on 10/12/2017.
 */
class KaraRemoteDataSource : KaraDataSource {
    override fun getChannelDetail(part: String, id: String): Observable<MutableList<Channel>> {
        return ApiClient.instance.getChannelDetail("snippet", id).toObservable().map { it.items.toMutableList() }
    }

    override fun getPlaylistDetails(part: String, playlistId: String): Observable<PlaylistDetailFromApi> {
        return ApiClient.instance.getPlaylistDetails(part, playlistId).toObservable()
    }

    override fun getVideoSearchFromApi(part: String, q: String, maxResult: Int): Observable<MutableList<Video>> {
        return ApiClient.instance.getVideoSearch(part, q, maxResult).toObservable().map { it.items }
    }

    override fun getVideoDetail(part: String, id: String, maxResult: Int): Observable<Item> {
        return ApiClient.instance.getVideoDetail(part, id, maxResult).toObservable().map { it.items[0] }
    }

    override fun getPlaylistSearch(part: String, id: String): Observable<MutableList<Playlist>> {
        return ApiClient.instance.getPlaylistSearch(part, id).toObservable().map { it.playlists }
    }

    override fun getMoreVideos(part: String, eventType: String, maxResults: String, relatedToVideoId: String, type: String): Single<MutableList<Video>> {
        return ApiClient.instance.getMoreVideos(part, eventType, maxResults, relatedToVideoId, type).map {
            it.items
        }
    }
}
