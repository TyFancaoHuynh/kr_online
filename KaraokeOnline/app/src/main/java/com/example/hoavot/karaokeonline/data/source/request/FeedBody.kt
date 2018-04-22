package com.example.hoavot.karaokeonline.data.source.request

import com.example.hoavot.karaokeonline.data.model.other.Comment
import com.google.gson.annotations.SerializedName

/**
 *
 * @author at-hoavo.
 */
data class FeedBody(@SerializedName("caption") val caption: String)
