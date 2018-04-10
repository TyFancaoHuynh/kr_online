package com.example.hoavot.karaokeonline.data.model.other

import com.google.gson.annotations.SerializedName

/**
 *
 * @author at-hoavo.
 */
data class Feed(@SerializedName("id") val id: Int,
                @SerializedName("caption") val caption: String,
                @SerializedName("avatar") val avatar: String,
                @SerializedName("userName") val userName: String,
                @SerializedName("music") val music: String,
                @SerializedName("likeCount") val likeCount: Long,
                @SerializedName("commentCount") val commentCount: Long,
                @SerializedName("comments") val comments: MutableList<Comment>,
                @SerializedName("like_flag") val likeFlag: Boolean) {
    internal var isRequesting = false
}
