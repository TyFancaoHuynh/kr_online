package com.example.hoavot.karaokeonline.data.source.response

import com.example.hoavot.karaokeonline.data.model.other.User
import com.google.gson.annotations.SerializedName

/**
 *
 * @author at-hoavo
 */
data class UserLikeResponse(@SerializedName("users") val users: MutableList<User>)
