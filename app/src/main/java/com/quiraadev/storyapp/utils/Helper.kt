package com.quiraadev.storyapp.utils

import android.content.Context
import android.graphics.Bitmap
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

fun File.buildImageBodyPart(): MultipartBody.Part {
    val imageReq = this.asRequestBody("image/*".toMediaTypeOrNull())
    return MultipartBody.Part.createFormData(
        "photo",
        this.name,
        imageReq
    )
}

fun Bitmap.convertToFile(context: Context, fileName: String): File {
    val file = File(context.cacheDir, fileName)
    file.createNewFile()
    val byteArrayOutputStream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayOutputStream)
    val bitMapData = byteArrayOutputStream.toByteArray()
    var fileOutputStream: FileOutputStream? = null
    try {
        fileOutputStream = FileOutputStream(file)
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    }
    try {
        fileOutputStream?.write(bitMapData)
        fileOutputStream?.flush()
        fileOutputStream?.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return file
}

fun String.setRequestBody(): RequestBody {
    return this.toRequestBody("multipart/form-data".toMediaTypeOrNull())
}