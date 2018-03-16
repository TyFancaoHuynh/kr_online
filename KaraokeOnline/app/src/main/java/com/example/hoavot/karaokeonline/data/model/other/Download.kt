package com.example.hoavot.karaokeonline.data.model.other

import com.google.gson.annotations.SerializedName

/**
 *
 * @author at-hoavo.
 */
data class Download(@SerializedName("id") val id: String,
                    @SerializedName("fileDownload") val fileDownload: String,
                    @SerializedName("idUser") val idUser: String)