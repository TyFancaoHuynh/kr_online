package com.example.hoavot.karaokeonline.ui.playmusic.model

import android.os.Parcel
import android.os.Parcelable

/**
 *
 * @author at-hoavo
 */
data class Song(val id: Int, val name: String, val artist: String, val image: String?, val url: String, val duration: Int, val date: String) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(artist)
        parcel.writeString(image)
        parcel.writeString(url)
        parcel.writeInt(duration)
        parcel.writeString(date)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Song> {
        override fun createFromParcel(parcel: Parcel): Song {
            return Song(parcel)
        }

        override fun newArray(size: Int): Array<Song?> {
            return arrayOfNulls(size)
        }
    }


}