package com.example.hoavot.karaokeonline.data.source.response

import com.example.hoavot.karaokeonline.data.model.other.Comment
import com.google.gson.annotations.SerializedName

/**
 *
 * @author at-hoavo.
 */
class CommentResponse(@SerializedName("comments") val comments: MutableList<Comment>)
