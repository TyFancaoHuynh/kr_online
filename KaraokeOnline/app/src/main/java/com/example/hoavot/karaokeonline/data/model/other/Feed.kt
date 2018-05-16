package com.example.hoavot.karaokeonline.data.model.other

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 *
 * @author at-hoavo.
 */
data class Feed(@SerializedName("id") val id: Int,
                @SerializedName("caption") val caption: String?,
                @SerializedName("avatar") val avatar: String?,
                @SerializedName("username") val username: String,
                @SerializedName("file_music") var fileMusic: String?,
                @SerializedName("file_music_user_write") var fileMusicUserWrite: String?,
                @SerializedName("image_file") var imageFile:String?,
                @SerializedName("like_count") var likeCount: Long,
                @SerializedName("comment_count") var commentCount: Long,
                @SerializedName("comments") var comments: MutableList<Comment>,
                @SerializedName("like_flag") var likeFlag: Int,
                @SerializedName("time") var time: Long) : Parcelable {
    internal var isRequesting = false

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readLong(),
            parcel.readLong(),
            arrayListOf<Comment>().apply {
                parcel.readList(this, Comment::class.java.classLoader)
            },
            parcel.readInt(),
            parcel.readLong()) {
        isRequesting = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(caption)
        parcel.writeString(avatar)
        parcel.writeString(username)
        parcel.writeString(fileMusic)
        parcel.writeString(fileMusicUserWrite)
        parcel.writeString(imageFile)
        parcel.writeLong(likeCount)
        parcel.writeLong(commentCount)
        parcel.writeInt(likeFlag)
        parcel.writeTypedList(comments)
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
