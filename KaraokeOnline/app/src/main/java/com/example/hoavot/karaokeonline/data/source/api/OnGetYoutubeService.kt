package com.example.hoavot.karaokeonline.data.source.api

import com.example.hoavot.karaokeonline.data.model.remote.*
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 *  Copyright © 2017 AsianTech inc.
 *  Created by hoavot on 10/12/2017.
 */
interface OnGetYoutubeService {
    companion object {
        const val URL_SEARCH_VIDEO = "/youtube/v3/search"
        const val URL_SEARCH_DETAIL_VIDEO = "/youtube/v3/videos"
        const val URL_SEARCH_CHANNEL = "/youtube/v3/channels"
        const val URL_SEARCH_PLAYLIST = "youtube/v3/playlists"
        const val URL_SEARCH_DETAIL_PLAYLIST = "/youtube/v3/playlistItems"
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
}
