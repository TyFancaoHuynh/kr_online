package com.example.hoavot.karaokeonline.data.model.other

import com.google.gson.annotations.SerializedName

/**
 *
 * @author at-hoavo.
 */
data class Feed(@SerializedName("id") val id: String,
                @SerializedName("caption") val caption: String,
                @SerializedName("avatar") val avatar: String,
                @SerializedName("userName") val userName: String,
                @SerializedName("music") val music: String,
                @SerializedName("likeCount") val likeCount: Double,
                @SerializedName("commentCount") val commentCount: Double,
                @SerializedName("comments") val comments: MutableList<Comment>)
