package com.example.hoavot.karaokeonline.data.source.response

import com.google.gson.annotations.SerializedName

/**
 *
 * @author at-hoavo.
 */
class UserInforResponse(
        @SerializedName("id") val id: Int,
        @SerializedName("username") val username: String,
        @SerializedName("password") val password: String,
        @SerializedName("age") val age: Int,
        @SerializedName("email") val email: String,
        @SerializedName("create_at") val createdAt: String,
        @SerializedName("update_at") val updatedAt: String
)
