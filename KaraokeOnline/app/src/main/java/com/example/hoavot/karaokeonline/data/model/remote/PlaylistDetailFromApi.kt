package com.example.hoavot.karaokeonline.data.model.remote

import com.example.hoavot.karaokeonline.data.model.nomal.PageInfor
import com.example.hoavot.karaokeonline.data.model.nomal.PlaylistDetails
import com.google.gson.annotations.SerializedName

/**
 *  Copyright © 2017 AsianTech inc.
 * Created by rimoka on 11/08/2017.
 */
class PlaylistDetailFromApi(@SerializedName("pageInfo") val pageInfo: PageInfor,
                            @SerializedName("items") val items: List<PlaylistDetails>)
