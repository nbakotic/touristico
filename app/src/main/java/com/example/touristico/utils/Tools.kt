package com.example.touristico.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.OpenableColumns
import java.io.ByteArrayOutputStream

class Tools {
    companion object {
        const val URL_PATH =
            "https://touristico-9aab0-default-rtdb.europe-west1.firebasedatabase.app"

        const val ADMIN_USERNAME = "admin"
        const val ADMIN_PASSWORD = "AdmiN321"

        fun gallery(): Intent {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            return intent
        }

        fun bitmapTosByteArray(bitmap : Bitmap) : ByteArray {
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream)
            return outputStream.toByteArray()
        }

        @SuppressLint("Range")
        fun getFileName (uri : Uri, context : Context) : String?{
            var result : String? = null
            if (uri.scheme.equals("content")) {
                val cursor = context.contentResolver.query(uri, null, null, null, null)
                try {
                    if (cursor != null && cursor.moveToFirst()) {
                        result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    }
                } finally {
                    cursor?.close()
                }
            }
            if (result == null) {
                result = uri.path
                val cut : Int? = result?.lastIndexOf('/')
                if (cut != null) {
                    result = result?.substring(cut + 1)
                }
            }
            return result
        }
    }
}