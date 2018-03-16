package com.example.hoavot.karaokeonline.data.source

import android.util.Log
import android.util.Log.d
import com.example.hoavot.karaokeonline.data.model.nomal.Channel
import com.example.hoavot.karaokeonline.data.model.nomal.Item
import com.example.hoavot.karaokeonline.data.model.nomal.Playlist
import com.example.hoavot.karaokeonline.data.model.nomal.Video
import com.example.hoavot.karaokeonline.data.model.remote.PlaylistDetailFromApi
import com.example.hoavot.karaokeonline.data.source.remote.KaraRemoteDataSource
import io.reactivex.Observable
import io.reactivex.Single

/**
 *  Copyright © 2017 AsianTech inc.
 *  Created by hoavot on 10/12/2017.
 */
class KaraRepository : KaraDataSource {
    override fun getChannelDetail(part: String, id: String): Observable<MutableList<Channel>> {
        return karaRemoteDataSource.getChannelDetail(part, id)
    }

    override fun getPlaylistDetails(part: String, playlistId: String): Observable<PlaylistDetailFromApi> {
        return karaRemoteDataSource.getPlaylistDetails(part, playlistId)
    }

    override fun getMoreVideos(part: String, eventType: String, maxResults: String, relatedToVideoId: String, type: String): Single<MutableList<Video>> {
        return karaRemoteDataSource.getMoreVideos(part, eventType, maxResults, relatedToVideoId, type)
    }

    private val karaRemoteDataSource = KaraRemoteDataSource()

    override fun getVideoSearchFromApi(part: String, q: String, maxResult: Int): Observable<MutableList<Video>> {
        return karaRemoteDataSource.getVideoSearchFromApi(part, q, maxResult)
    }

    override fun getVideoDetail(part: String, id: String, maxResult: Int): Observable<Item> {
        return karaRemoteDataSource.getVideoDetail(part, id, maxResult)
    }

    override fun getPlaylistSearch(part: String, id: String): Observable<MutableList<Playlist>> {
        return karaRemoteDataSource.getPlaylistSearch(part, id)
    }
}
