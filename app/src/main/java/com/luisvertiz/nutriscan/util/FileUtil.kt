package com.luisvertiz.nutriscan.util

import android.content.Context
import android.graphics.Bitmap
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

object FileUtil {

    fun saveBitmapToTempFile(context: Context, bitmap: Bitmap): String? = try {
        val fileName = "food_image_${UUID.randomUUID()}.jpg"
        val file = File(context.cacheDir, fileName)
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
        outputStream.flush()
        outputStream.close()
        return file.absolutePath
    } catch (_: Exception) {
        null
    }
}
