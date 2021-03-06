package com.example.hoavot.karaokeonline.data.source.remote

import android.util.Log.d
import com.example.hoavot.karaokeonline.data.model.nomal.Channel
import com.example.hoavot.karaokeonline.data.model.nomal.Item
import com.example.hoavot.karaokeonline.data.model.nomal.Playlist
import com.example.hoavot.karaokeonline.data.model.nomal.Video
import com.example.hoavot.karaokeonline.data.model.other.Feed
import com.example.hoavot.karaokeonline.data.model.other.User
import com.example.hoavot.karaokeonline.data.model.remote.PlaylistDetailFromApi
import com.example.hoavot.karaokeonline.data.source.KaraDataSource
import com.example.hoavot.karaokeonline.data.source.api.ApiClient
import com.example.hoavot.karaokeonline.data.source.api.ApiService
import com.example.hoavot.karaokeonline.data.source.request.LoginBody
import com.example.hoavot.karaokeonline.data.source.response.*
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

    override fun updateInforUser(user: User): Single<LoginResponse> {
        return karaApi.updateInforUser(user.username, user.password, user.email)
    }

    override fun updateAvatarUser(avatarFile: File): Single<UserResponse> {
        val requestFile = RequestBody.create(MediaType.parse("image/jpeg"), avatarFile)
        d("TAGGG", "file name:${avatarFile.name} ")
        val requestFileBody = MultipartBody.Part.createFormData("avatar", avatarFile.name!!, requestFile)
        return karaApi.updateAvatarUser(requestFileBody)
    }

    override fun getFeeds(): Single<FeedsResponse> {
        return karaApi.getFeeds()
    }

    override fun getFeedMe(userId: Int): Single<FeedsResponse> = karaApi.getFeedMe(userId)

    override fun postComment(feedId: Int, comment: String): Single<CommentResponse> = karaApi.postComment(feedId, comment)

    override fun postLike(feedId: Int): Single<LikeResponse> = karaApi.postLike(feedId)

    override fun postUnLike(feedId: Int): Single<LikeResponse> = karaApi.postUnLike(feedId)

    override fun getComments(feedId: Int): Single<CommentResponse> = karaApi.getComments(feedId)

    override fun postFeed(fileName: String, audioFile: File?, caption: String, imageFile: File?): Single<FeedResponse> {
        var requestFileBody: MultipartBody.Part? = null
        audioFile?.let {
            val requestFile = RequestBody.create(MediaType.parse("audio/x-m4a"), it)
            d("TAGGG", "file name:${fileName} ")
            requestFileBody = MultipartBody.Part.createFormData("audio", it.name!!, requestFile)
        }
        var requestFileImageBody: MultipartBody.Part? = null
        imageFile?.let {
            val requestImageFile = RequestBody.create(MediaType.parse("image/jpeg"), it)
            d("TAGGG", "file name:${fileName} ")
            requestFileImageBody = MultipartBody.Part.createFormData("image", it.name!!, requestImageFile)
        }
        val fileNameRequestBody = createNonNullPartFromString(fileName)
        val captionBody = createNonNullPartFromString(caption)
        return karaApi.postFeed(requestFileBody, fileNameRequestBody, captionBody, requestFileImageBody)
    }

    override fun getInforUser(id: Int): Single<User> = karaApi.getMeUser(id)

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

    override fun login(username: String, password: String): Single<LoginResponse> {
        val loginBody = LoginBody(username, password)
        return karaApi.login(loginBody)
    }

    override fun register(username: String, password: String): Single<LoginResponse> {
        val registerBody = LoginBody(username, password)
        return karaApi.register(registerBody)
    }

    override fun getPopularVideo(part: String, chart: String, regionCode: String): Single<MutableList<Item>> {
        return youtubeApi.getPopularVideo(part, chart, regionCode).map { it.items }
    }

    override fun deleteFeed(feedId: Int): Single<DeleteFeedResponse> {
        return karaApi.deleteFeed(feedId)
    }

    override fun updateFeed(feed: Feed, fileName: String, audioFile: File?, caption: String, imageFile: File?): Single<FeedResponse> {
        var requestFileBody: MultipartBody.Part? = null
        audioFile?.let {
            val requestFile = RequestBody.create(MediaType.parse("audio/x-m4a"), audioFile)
            d("TAGGG", "file name:${fileName} ")
            requestFileBody = MultipartBody.Part.createFormData("audio", audioFile.name!!, requestFile)
        }
        var requestImageFileBody: MultipartBody.Part? = null
        imageFile?.let {
            val requestFileImage = RequestBody.create(MediaType.parse("image/*"), it)
            d("TAGGG", "file name:${fileName} ")
            requestImageFileBody = MultipartBody.Part.createFormData("image", it.name!!, requestFileImage)
        }
        val fileNameRequestBody = createNonNullPartFromString(fileName)
        val captionBody = createNonNullPartFromString(caption)
        val id = createNonNullPartFromString(feed.id.toString())
        return karaApi.updateFeed(requestFileBody, fileNameRequestBody, captionBody, id, requestImageFileBody)
    }

    override fun getListUserLike(feedId: Int): Single<UserLikeResponse> {
        return karaApi.getUsersLike(feedId)
    }
}
