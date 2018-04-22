package com.example.hoavot.karaokeonline.data.model.other

import com.google.gson.annotations.SerializedName

/**
 *
 * @author at-hoavo.
 */
data class Comment(@SerializedName("id") val id: Int,
                   @SerializedName("time") val time: Long,
                   @SerializedName("avatarUser") val avatarUser: String,
                   @SerializedName("comment") val comment: String,
                   @SerializedName("idFeed") val idFeed: Int)
