package com.example.hoavot.karaokeonline.data.model.other

import com.google.gson.annotations.SerializedName

/**
 *
 * @author at-hoavo.
 */
data class User(@SerializedName("id") val id: String,
                @SerializedName("username") val username: String,
                @SerializedName("password") val password: String,
                @SerializedName("avatar") val avatar: String,
                @SerializedName("address") val address: String,
                @SerializedName("age") val age: Int,
                @SerializedName("email") val email: String)