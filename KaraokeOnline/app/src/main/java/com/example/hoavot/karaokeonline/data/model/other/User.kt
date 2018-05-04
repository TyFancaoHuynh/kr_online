package com.example.hoavot.karaokeonline.data.model.other

import com.google.gson.annotations.SerializedName

/**
 *
 * @author at-hoavo.
 */
data class User(@SerializedName("id") val id: String = "",
                @SerializedName("username") val username: String = "",
                @SerializedName("password") val password: String = "",
                @SerializedName("create_at") val createdAt: String = "",
                @SerializedName("update_at") val updatedAt: String = "",
                @SerializedName("avatar") val avatar: String = "",
                @SerializedName("age") val age: Int = 0,
                @SerializedName("gender") val gender: Int = 0)