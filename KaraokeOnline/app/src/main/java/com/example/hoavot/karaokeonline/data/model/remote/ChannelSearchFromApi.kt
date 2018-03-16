package com.example.hoavot.karaokeonline.data.model.remote

import com.example.hoavot.karaokeonline.data.model.nomal.Channel
import com.google.gson.annotations.SerializedName

/**
 *  Copyright © 2017 AsianTech inc.
 * Created by rimoka on 10/08/2017.
 */
class ChannelSearchFromApi(@SerializedName("items") val items: List<Channel>)
