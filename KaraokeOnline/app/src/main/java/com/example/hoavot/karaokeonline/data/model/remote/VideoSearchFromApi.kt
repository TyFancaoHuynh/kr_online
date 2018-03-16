package com.example.hoavot.karaokeonline.data.model.remote

import com.example.hoavot.karaokeonline.data.model.nomal.Video
import com.google.gson.annotations.SerializedName

/**
 *  Copyright © 2017 AsianTech inc.
 * Created by rimoka on 01/08/2017.
 */
class VideoSearchFromApi(@SerializedName("items") val items: MutableList<Video>)
