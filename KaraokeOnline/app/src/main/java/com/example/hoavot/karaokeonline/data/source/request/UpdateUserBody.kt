package com.example.hoavot.karaokeonline.data.source.request

import com.google.gson.annotations.SerializedName

/**
 *
 * @author at-hoavo.
 */
class UpdateUserBody(
        @SerializedName("id") val userId: Int,
        @SerializedName("username") val username: String,
        @SerializedName("password") val password: String,
        @SerializedName("age") val age: Int,
        @SerializedName("email") val email: String
)
