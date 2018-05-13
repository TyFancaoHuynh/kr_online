package com.example.hoavot.karaokeonline.data.source.api

import com.google.gson.annotations.SerializedName

/**
 * Use this file to handle error from api
 */
data class ApiException(
        @SerializedName("message") val messageError: String) : Throwable(messageError) {
    var statusCode: Int? = null
}
