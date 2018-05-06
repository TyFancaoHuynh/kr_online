package com.example.hoavot.karaokeonline.data.model.other

import com.google.gson.annotations.SerializedName

/**
 *
 * @author at-hoavo.
 */
data class User(@SerializedName("id") val id: String = "",
                @SerializedName("username") var username: String = "",
                @SerializedName("password") var password: String = "",
                @SerializedName("create_at") val createdAt: String = "",
                @SerializedName("update_at") var updatedAt: String = "",
                @SerializedName("avatar") var avatar: String = "",
                @SerializedName("email") var email: String = "",
                @SerializedName("age") var age: Int = 0,
                @SerializedName("gender") var gender: Int = 0)