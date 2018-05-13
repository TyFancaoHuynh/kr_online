package com.example.hoavot.karaokeonline.data.source.response

import com.example.hoavot.karaokeonline.data.model.other.User
import com.google.gson.annotations.SerializedName

/**
 *
 * @author at-hoavo
 */
data class LoginResponse(@SerializedName("token") val token: String,@SerializedName("success") val success:Boolean,@SerializedName("message") val message:String,@SerializedName("user") val user:User)
