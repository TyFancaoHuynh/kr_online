package com.example.hoavot.karaokeonline.ui.base

import android.content.Context
import android.graphics.Bitmap
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*

/**
 *
 * @author at-hoavo
 */
object Image {

    internal fun convertBitmapToFile(bitmap: Bitmap, context: Context): File {
        val f = File(context.cacheDir, "avatar" + Date().time + ".jpg")
        f.createNewFile()

        //Convert bitmap to byte array
        val bos = ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0 /*ignored for PNG*/, bos);
        val bitmapdata = bos.toByteArray()

        //write the bytes in file
        val fos = FileOutputStream(f);
        fos.write(bitmapdata);
        fos.flush()
        fos.close()
        return f
    }

}