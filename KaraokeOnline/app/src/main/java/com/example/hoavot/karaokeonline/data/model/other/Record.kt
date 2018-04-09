package com.example.hoavot.karaokeonline.data.model.other

import com.google.gson.annotations.SerializedName

/**
 *
 * @author at-hoavo.
 */
data class Record(@SerializedName("id") val id: String,
                  @SerializedName("fileRecord") val fileRecord: String,
                  @SerializedName("idUser") val idUser: String,
                  @SerializedName("nameRecord") val nameRecord: String,
                  @SerializedName("timeRecord") val timeRecord: Long,
                  @SerializedName("dateRecord") val dateRecord: String)