package com.example.hoavot.karaokeonline.data.source

import com.example.hoavot.karaokeonline.data.model.nomal.Channel
import com.example.hoavot.karaokeonline.data.model.nomal.Item
import com.example.hoavot.karaokeonline.data.model.nomal.Playlist
import com.example.hoavot.karaokeonline.data.model.nomal.Video
import com.example.hoavot.karaokeonline.data.model.other.Feed
import com.example.hoavot.karaokeonline.data.model.other.User
import com.example.hoavot.karaokeonline.data.model.remote.PlaylistDetailFromApi
import com.example.hoavot.karaokeonline.data.source.response.*
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.MultipartBody
import java.io.File

/**
 *  Copyright © 2017 AsianTech inc.
 *  Created by hoavot on 10/12/2017.
 */
interface KaraDataSource {

    /**
     *  Fun to search Video from Youtube
     *  @param part : part you want to display list video,default is  "snippet"
     *  @param q: query to search video
     *  @param maxResult: must to be less 25
     */
    fun getVideoSearchFromApi(part: String, q: String, maxResult: Int = 25): Observable<MutableList<Video>>

    /**
     * Use get detail of each video
     * @param part : part you want to display list video,default is "snippet,contentDetails,statistics""
     * @param id : id of video
     * @param maxResult: must to be less 25
     *
     */
    fun getVideoDetail(part: String, id: String, maxResult: Int = 25): Observable<Item>

    /**
     *  Fun to get PlayList search from Youtube
     *  @param part :part you want to display list video,default is  "snippet"
     *  @param id : id of video??
     *  @param maxResult: must to be less 25
     */
    fun getPlaylistSearch(part: String, id: String): Observable<MutableList<Playlist>>

    /**
     *  Fun to get channel detail
     *  @param part:
     *  @param id: id of channel
     */
    fun getChannelDetail(part: String, id: String): Observable<MutableList<Channel>>

    /**
     *  Fun to get channel detail
     *  @param part:
     *  @param id: id of playlist
     */
    fun getPlaylistDetails(part: String, playlistId: String): Observable<PlaylistDetailFromApi>

    /**
     *  Fun to get more video related to present video
     *  @param part:
     *  @param eventType:
     *  @param maxResults: default=25
     *  @param relatedToVideoId: id of present video
     *  @param type:
     */
    fun getMoreVideos(part: String, eventType: String, maxResults: String, relatedToVideoId: String, type: String): Single<MutableList<Video>>

    fun getPopularVideo(part: String,
                        chart: String,
                        regionCode: String): Single<MutableList<Item>>

    fun getInforUser(id: Int): Single<User>

    fun updateInforUser(user: User): Single<User>

    fun updateAvatarUser(avatarFile: File): Single<UserResponse>

    fun getFeeds(): Single<FeedsResponse>

    fun getFeedMe(): Single<FeedsResponse>

    fun postComment(feedId: Int, comment: String): Single<CommentResponse>

    fun postLike(feedId: Int): Single<LikeResponse>

    fun postUnLike(feedId: Int): Single<LikeResponse>

    fun getComments(feedId: Int): Single<CommentResponse>

    fun postFeed(fileName: String, audioFile: File? = null, caption: String, imageFile: File?)
            : Single<FeedResponse>

    fun login(username: String, password: String): Single<LoginResponse>

    fun register(username: String, password: String): Single<LoginResponse>

    fun deleteFeed(feedId: Int): Single<DeleteFeedResponse>

    fun updateFeed(feed: Feed, fileName: String, audioFile: File? = null, caption: String, imageFile: File?): Single<FeedResponse>
}
