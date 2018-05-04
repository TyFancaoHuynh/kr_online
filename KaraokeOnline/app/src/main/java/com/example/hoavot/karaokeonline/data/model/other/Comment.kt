package com.example.hoavot.karaokeonline.data.model.other

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 *
 * @author at-hoavo.
 */
data class Comment(@SerializedName("id") val id: Int,
                   @SerializedName("time") val time: Long,
                   @SerializedName("avatar") val avatarUser: String,
                   @SerializedName("username") val username: String,
                   @SerializedName("comment") val comment: String,
                   @SerializedName("idFeed") val idFeed: Int) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readLong(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeLong(time)
        parcel.writeString(avatarUser)
        parcel.writeString(username)
        parcel.writeString(comment)
        parcel.writeInt(idFeed)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Comment> {
        override fun createFromParcel(parcel: Parcel): Comment {
            return Comment(parcel)
        }

        override fun newArray(size: Int): Array<Comment?> {
            return arrayOfNulls(size)
        }
    }

}
