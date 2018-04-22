package com.example.hoavot.karaokeonline.data.model.other

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 *
 * @author at-hoavo.
 */
data class Feed(@SerializedName("id") val id: Int,
                @SerializedName("caption") val caption: String,
                @SerializedName("avatar") val avatar: String,
                @SerializedName("userName") val userName: String,
                @SerializedName("name_music") var musicName: String,
                @SerializedName("likeCount") var likeCount: Long,
                @SerializedName("commentCount") var commentCount: Long,
                @SerializedName("comments") val comments: MutableList<Comment>,
                @SerializedName("like_flag") var likeFlag: Boolean,
                @SerializedName("time") var time: Long) : Parcelable {
    internal var isRequesting = false

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readLong(),
            parcel.readLong(),
            TODO("comments"),
            parcel.readByte() != 0.toByte(),
            parcel.readLong()) {
        isRequesting = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(caption)
        parcel.writeString(avatar)
        parcel.writeString(userName)
        parcel.writeString(musicName)
        parcel.writeLong(likeCount)
        parcel.writeLong(commentCount)
        parcel.writeByte(if (likeFlag) 1 else 0)
        parcel.writeLong(time)
        parcel.writeByte(if (isRequesting) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Feed> {
        override fun createFromParcel(parcel: Parcel): Feed {
            return Feed(parcel)
        }

        override fun newArray(size: Int): Array<Feed?> {
            return arrayOfNulls(size)
        }
    }

}
