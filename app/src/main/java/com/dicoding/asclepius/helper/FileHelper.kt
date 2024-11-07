package com.dicoding.asclepius.helper

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object FileHelper {
    fun saveCroppedImage(context: Context, croppedBitmap: Bitmap, fileName: String): Uri? {
        val fileName = "cropped_${System.currentTimeMillis()}_${fileName}"
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName)

        try {
            FileOutputStream(file).use { outputStream ->
                croppedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.flush()
            }
            return FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    fun getFileNameFromUri(uri: Uri, context: Context): String? {
        var fileName: String? = null
        if (uri.scheme == "content") {
            val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    fileName = it.getString(it.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
                }
            }
        } else if (uri.scheme == "file") {
            fileName = File(uri.path ?: "").name
        }
        return fileName
    }

    fun uriToBitmap(uri: Uri, context: Context): Bitmap? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        } else {
            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        }
    }
}
