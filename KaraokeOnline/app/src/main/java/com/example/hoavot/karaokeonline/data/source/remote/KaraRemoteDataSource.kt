package com.example.hoavot.karaokeonline.data.source.remote

import com.example.hoavot.karaokeonline.data.model.nomal.Channel
import com.example.hoavot.karaokeonline.data.model.nomal.Item
import com.example.hoavot.karaokeonline.data.model.nomal.Playlist
import com.example.hoavot.karaokeonline.data.model.nomal.Video
import com.example.hoavot.karaokeonline.data.model.other.User
import com.example.hoavot.karaokeonline.data.model.remote.PlaylistDetailFromApi
import com.example.hoavot.karaokeonline.data.source.KaraDataSource
import com.example.hoavot.karaokeonline.data.source.api.ApiClient
import com.example.hoavot.karaokeonline.data.source.api.ApiService
import com.example.hoavot.karaokeonline.data.source.request.UpdateUserBody
import com.example.hoavot.karaokeonline.data.source.response.*
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 *  Copyright © 2017 AsianTech inc.
 *  Created by hoavot on 10/12/2017.
 */
class KaraRemoteDataSource(private val api: ApiService) : KaraDataSource {

    constructor() : this(ApiClient.getInstance(null).service)

    override fun updateInforUser(avatar:MultipartBody.Part,age:RequestBody,gender:RequestBody): Single<User>
            = api.updateInforUser(avatar,age,gender)

    override fun getFeeds(): Single<FeedsResponse> = api.getFeeds()

    override fun getFeedMe(id: Int): Single<FeedsResponse> = api.getFeedMe(id)

    override fun postComment(feedId: Int, comment: String): Single<CommentResponse> = api.postComment(feedId, comment)

    override fun postLike(feedId: Int): Single<LikeResponse> = api.postLike(feedId)

    override fun postUnLike(feedId: Int): Single<LikeResponse> = api.postUnLike(feedId)

    override fun getComments(feedId: Int): Single<FeedsResponse> = api.getComments(feedId)

    override fun postFeed(imageFile: MultipartBody.Part, resultLimitRequestBody: RequestBody): Single<FeedResponse>
            = api.postFeed(imageFile, resultLimitRequestBody)

    override fun getInforUser(id: Int): Single<User> = api.getInforUser(id)

    override fun getChannelDetail(part: String, id: String): Observable<MutableList<Channel>> {
        return api.getChannelDetail("snippet", id).toObservable().map { it.items.toMutableList() }
    }

    override fun getPlaylistDetails(part: String, playlistId: String): Observable<PlaylistDetailFromApi> {
        return api.getPlaylistDetails(part, playlistId).toObservable()
    }

    override fun getVideoSearchFromApi(part: String, q: String, maxResult: Int): Observable<MutableList<Video>> {
        return api.getVideoSearch(part, q, maxResult).toObservable().map { it.items }
    }

    override fun getVideoDetail(part: String, id: String, maxResult: Int): Observable<Item> {
        return api.getVideoDetail(part, id, maxResult).toObservable().map { it.items[0] }
    }

    override fun getPlaylistSearch(part: String, id: String): Observable<MutableList<Playlist>> {
        return api.getPlaylistSearch(part, id).toObservable().map { it.playlists }
    }

    override fun getMoreVideos(part: String, eventType: String, maxResults: String, relatedToVideoId: String, type: String): Single<MutableList<Video>> {
        return api.getMoreVideos(part, eventType, maxResults, relatedToVideoId, type).map {
            it.items
        }
    }
}
