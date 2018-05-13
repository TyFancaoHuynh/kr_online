package com.example.hoavot.karaokeonline.data.source.request

import com.google.gson.annotations.SerializedName

/**
 *
 * @author at-hoavo
 */
data class LoginBody(@SerializedName("username") val username: String, @SerializedName("password") val password: String)
