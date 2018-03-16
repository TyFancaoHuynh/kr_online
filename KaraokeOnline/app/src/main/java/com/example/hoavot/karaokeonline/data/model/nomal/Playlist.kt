package com.example.hoavot.karaokeonline.data.model.nomal

import com.google.gson.annotations.SerializedName

/**
 *  Copyright © 2017 AsianTech inc.
 * Created by rimoka on 10/08/2017.
 */
class Playlist(@SerializedName("id")val id: String,
               @SerializedName("snippet")val snippet: Snippet,
               @SerializedName("thumbnails")val thumbnails: Thumbnails)