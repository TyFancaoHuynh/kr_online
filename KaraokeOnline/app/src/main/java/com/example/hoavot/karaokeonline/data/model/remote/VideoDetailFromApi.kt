package com.example.hoavot.karaokeonline.data.model.remote

import com.example.hoavot.karaokeonline.data.model.nomal.Item
import com.google.gson.annotations.SerializedName

/**
 *  Copyright © 2017 AsianTech inc.
 * Created by rimoka on 03/08/2017.
 */
class VideoDetailFromApi(@SerializedName("items") val items: MutableList<Item>)
