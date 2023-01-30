package com.example.touristico.utils

import android.content.Intent
import android.graphics.Bitmap
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
    }
}