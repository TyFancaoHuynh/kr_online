package com.example.hoavot.karaokeonline.data.source.remote

import android.util.Log.d
import com.example.hoavot.karaokeonline.data.model.nomal.Channel
import com.example.hoavot.karaokeonline.data.model.nomal.Item
import com.example.hoavot.karaokeonline.data.model.nomal.Playlist
import com.example.hoavot.karaokeonline.data.model.nomal.Video
import com.example.hoavot.karaokeonline.data.model.other.User
import com.example.hoavot.karaokeonline.data.model.remote.PlaylistDetailFromApi
import com.example.hoavot.karaokeonline.data.source.KaraDataSource
import com.example.hoavot.karaokeonline.data.source.api.ApiClient
import com.example.hoavot.karaokeonline.data.source.api.ApiService
import com.example.hoavot.karaokeonline.data.source.response.CommentResponse
import com.example.hoavot.karaokeonline.data.source.response.FeedResponse
import com.example.hoavot.karaokeonline.data.source.response.FeedsResponse
import com.example.hoavot.karaokeonline.data.source.response.LikeResponse
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

/**
 *  Copyright © 2017 AsianTech inc.
 *  Created by hoavot on 10/12/2017.
 */
class KaraRemoteDataSource(private val youtubeApi: ApiService, private val karaApi: ApiService) : KaraDataSource {

    constructor() : this(ApiClient.getInstance(null).youtTubeService, ApiClient.getInstance(null).karaService)

    override fun updateInforUser(user: User): Single<User> {
        val username = createNonNullPartFromString(user.username)
        val password = createNonNullPartFromString(user.password)
        val email = createNonNullPartFromString(user.email)
        val age = createPartFromString(user.age.toString())
        val gender = createPartFromString(user.gender.toString())
        return karaApi.updateInforUser(username, password, email, age, gender)
    }

    override fun updateAvatarUser(avatarFile: File): Single<User> {
        val requestFile = RequestBody.create(MediaType.parse("image/*"), avatarFile)
        d("TAGGG", "file name:${avatarFile.name} ")
        val requestFileBody = MultipartBody.Part.createFormData("audio", avatarFile.name!!, requestFile)
        return karaApi.updateAvatarUser(requestFileBody)
    }

    override fun getFeeds(): Single<FeedsResponse> = karaApi.getFeeds()

    override fun getFeedMe(): Single<FeedsResponse> = karaApi.getFeedMe()

    override fun postComment(feedId: Int, comment: String): Single<CommentResponse> = karaApi.postComment(feedId, comment)

    override fun postLike(feedId: Int): Single<LikeResponse> = karaApi.postLike(feedId)

    override fun postUnLike(feedId: Int): Single<LikeResponse> = karaApi.postUnLike(feedId)

    override fun getComments(feedId: Int): Single<CommentResponse> = karaApi.getComments(feedId)

    override fun postFeed(audioFile: File?, caption: String): Single<FeedResponse> {
        val requestFile = RequestBody.create(MediaType.parse("audio/x-m4a"), audioFile!!)
        d("TAGGG", "file name:${audioFile.name} ")
        val requestFileBody = MultipartBody.Part.createFormData("audio", audioFile.name!!, requestFile)
        val captionBody = createNonNullPartFromString(caption)
        return karaApi.postFeed(requestFileBody, captionBody)
    }

    override fun getInforUser(id: Int): Single<User> = karaApi.getInforUser(id)

    override fun getChannelDetail(part: String, id: String): Observable<MutableList<Channel>> {
        return youtubeApi.getChannelDetail("snippet", id).toObservable().map { it.items.toMutableList() }
    }

    override fun getPlaylistDetails(part: String, playlistId: String): Observable<PlaylistDetailFromApi> {
        return youtubeApi.getPlaylistDetails(part, playlistId).toObservable()
    }

    override fun getVideoSearchFromApi(part: String, q: String, maxResult: Int): Observable<MutableList<Video>> {
        return youtubeApi.getVideoSearch(part, q, maxResult).toObservable().map { it.items }
    }

    override fun getVideoDetail(part: String, id: String, maxResult: Int): Observable<Item> {
        return youtubeApi.getVideoDetail(part, id, maxResult).toObservable().map { it.items[0] }
    }

    override fun getPlaylistSearch(part: String, id: String): Observable<MutableList<Playlist>> {
        return youtubeApi.getPlaylistSearch(part, id).toObservable().map { it.playlists }
    }

    override fun getMoreVideos(part: String, eventType: String, maxResults: String, relatedToVideoId: String, type: String): Single<MutableList<Video>> {
        return youtubeApi.getMoreVideos(part, eventType, maxResults, relatedToVideoId, type).map {
            it.items
        }
    }

    private fun createPartFromString(partString: String?) = if (partString == null || partString == "null") {
        null
    } else {
        RequestBody.create(MediaType.parse("multipart/form-data"), partString)
    }

    private fun createNonNullPartFromString(partString: String)
            = RequestBody.create(MediaType.parse("multipart/form-data"), partString)
}
