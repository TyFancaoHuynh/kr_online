package com.example.hoavot.karaokeonline.data.source

import com.example.hoavot.karaokeonline.data.model.nomal.Channel
import com.example.hoavot.karaokeonline.data.model.nomal.Item
import com.example.hoavot.karaokeonline.data.model.nomal.Playlist
import com.example.hoavot.karaokeonline.data.model.nomal.Video
import com.example.hoavot.karaokeonline.data.model.other.User
import com.example.hoavot.karaokeonline.data.model.remote.PlaylistDetailFromApi
import com.example.hoavot.karaokeonline.data.source.remote.KaraRemoteDataSource
import com.example.hoavot.karaokeonline.data.source.response.*
import io.reactivex.Observable
import io.reactivex.Single
import java.io.File

/**
 *  Copyright © 2017 AsianTech inc.
 *  Created by hoavot on 10/12/2017.
 */
class KaraRepository : KaraDataSource {

    private val karaRemoteDataSource = KaraRemoteDataSource()

    override fun getInforUser(id: Int): Single<User> = karaRemoteDataSource.getInforUser(id)

    override fun updateInforUser(user: User): Single<User> = karaRemoteDataSource.updateInforUser(user)

    override fun updateAvatarUser(avatarFile: File): Single<UserResponse>
            = karaRemoteDataSource.updateAvatarUser(avatarFile)

    override fun getFeeds(): Single<FeedsResponse> = karaRemoteDataSource.getFeeds()

    override fun getFeedMe(): Single<FeedsResponse> = karaRemoteDataSource.getFeedMe()

    override fun postComment(feedId: Int, comment: String): Single<CommentResponse>
            = karaRemoteDataSource.postComment(feedId, comment)

    override fun postLike(feedId: Int): Single<LikeResponse> = karaRemoteDataSource.postLike(feedId)

    override fun postUnLike(feedId: Int): Single<LikeResponse> = karaRemoteDataSource.postUnLike(feedId)

    override fun getComments(feedId: Int): Single<CommentResponse> = karaRemoteDataSource.getComments(feedId)

    override fun postFeed(fileName: String, audioFile: File?, caption: String): Single<FeedResponse>
            = karaRemoteDataSource.postFeed(fileName, audioFile, caption)

    override fun getChannelDetail(part: String, id: String): Observable<MutableList<Channel>> {
        return karaRemoteDataSource.getChannelDetail(part, id)
    }

    override fun getPlaylistDetails(part: String, playlistId: String): Observable<PlaylistDetailFromApi> {
        return karaRemoteDataSource.getPlaylistDetails(part, playlistId)
    }

    override fun getMoreVideos(part: String, eventType: String, maxResults: String, relatedToVideoId: String, type: String): Single<MutableList<Video>> {
        return karaRemoteDataSource.getMoreVideos(part, eventType, maxResults, relatedToVideoId, type)
    }

    override fun getVideoSearchFromApi(part: String, q: String, maxResult: Int): Observable<MutableList<Video>> {
        return karaRemoteDataSource.getVideoSearchFromApi(part, q, maxResult)
    }

    override fun getVideoDetail(part: String, id: String, maxResult: Int): Observable<Item> {
        return karaRemoteDataSource.getVideoDetail(part, id, maxResult)
    }

    override fun getPlaylistSearch(part: String, id: String): Observable<MutableList<Playlist>> {
        return karaRemoteDataSource.getPlaylistSearch(part, id)
    }

    override fun login(username: String, password: String): Single<LoginResponse> {
        return karaRemoteDataSource.login(username, password)
    }

    override fun register(username: String, password: String): Single<LoginResponse> {
        return karaRemoteDataSource.register(username, password)
    }
}
