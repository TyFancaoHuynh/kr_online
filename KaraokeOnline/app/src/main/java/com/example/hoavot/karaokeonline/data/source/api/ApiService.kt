package com.example.hoavot.karaokeonline.data.source.api

import com.example.hoavot.karaokeonline.data.model.other.User
import com.example.hoavot.karaokeonline.data.model.remote.*
import com.example.hoavot.karaokeonline.data.source.response.*
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

/**
 *  Copyright © 2017 AsianTech inc.
 *  Created by hoavot on 10/12/2017.
 */
interface ApiService {
    companion object {
        const val URL_SEARCH_VIDEO = "/youtube/v3/search"
        const val URL_SEARCH_DETAIL_VIDEO = "/youtube/v3/videos"
        const val URL_SEARCH_CHANNEL = "/youtube/v3/channels"
        const val URL_SEARCH_PLAYLIST = "youtube/v3/playlists"
        const val URL_SEARCH_DETAIL_PLAYLIST = "/youtube/v3/playlistItems"
        const val URL_GET_FEED = "/feeds"
        const val URL_GET_FEED_ME = "/feed/me"
        const val URL_GET_COMMENT = "/feed/comments"
        const val URL_POST_FEED = "/feed"
        const val KEY_BROWSER = "AIzaSyATxFUmJOzuagSH_qs9jVZza7p6-Ycpo4k"
    }

    // Use get list video when search by keyword
    @GET(URL_SEARCH_VIDEO)
    fun getVideoSearch(
            @Query("part") part: String,
            @Query("q") q: String,
            @Query("maxResults") maxResult: Int,
            @Query("key") key: String = KEY_BROWSER
    ): Single<VideoSearchFromApi>

    // Use get detail of each video
    @GET(URL_SEARCH_DETAIL_VIDEO)
    fun getVideoDetail(@Query("part") part: String,
                       @Query("id") id: String,
                       @Query("maxResult") maxResult: Int,
                       @Query("key") key: String = KEY_BROWSER
    ): Single<VideoDetailFromApi>

    @GET(URL_SEARCH_CHANNEL)
    fun getChannelDetail(@Query("part") part: String,
                         @Query("id") id: String,
                         @Query("key") key: String = KEY_BROWSER
    ): Single<ChannelSearchFromApi>

    @GET(URL_SEARCH_PLAYLIST)
    fun getPlaylistSearch(@Query("part") part: String,
                          @Query("id") id: String,
                          @Query("key") key: String = KEY_BROWSER
    ): Single<PlaylistSearchFromApi>

    @GET(URL_SEARCH_DETAIL_PLAYLIST)
    fun getPlaylistDetails(@Query("part") part: String,
                           @Query("playlistId") playlistId: String,
                           @Query("key") key: String = KEY_BROWSER
    ): Single<PlaylistDetailFromApi>

    @GET(URL_SEARCH_VIDEO)
    fun getMoreVideos(
            @Query("part") part: String,
            @Query("eventType") eventType: String,
            @Query("maxResults") maxResults: String,
            @Query("relatedToVideoId") relatedToVideoId: String,
            @Query("type") type: String,
            @Query("key") key: String = KEY_BROWSER
    )
            : Single<VideoSearchFromApi>

    @GET("/user/me")
    fun getInforUser(
            @Query("id") id: Int
    ): Single<User>

    @PUT("/user/update")
    fun updateInforUser(
            @Part("avatar") avatar: MultipartBody.Part,
            @Part("age") age: RequestBody,
            @Part("gender") gender: RequestBody
    ): Single<User>

    @GET("/feeds")
    fun getFeeds(): Single<FeedsResponse>

    @GET("/feed/me")
    fun getFeedMe(@Query("id") id: Int): Single<FeedsResponse>

    @FormUrlEncoded
    @POST("/feed/{id}/comment")
    fun postComment(@Path("id") feedId: Int, @Field("comment") comment: String): Single<CommentResponse>

    @POST("/feed/{id}/like")
    fun postLike(@Path("id") feedId: Int): Single<LikeResponse>

    @DELETE("/feed/{id}/like")
    fun postUnLike(@Path("id") feedId: Int): Single<LikeResponse>

    @GET("/feed/comments")
    fun getComments(@Query("id_feed") feedId: Int): Single<FeedsResponse>

    @Multipart
    @POST("/feed/create")
    fun postFeed(@Part audio: MultipartBody.Part, @Part caption: RequestBody)
            : Single<FeedResponse>
}
