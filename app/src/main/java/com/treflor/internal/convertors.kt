package com.treflor.internal

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.widget.DatePicker
import java.io.ByteArrayOutputStream
import java.util.*


fun imageToBase64(path: String): String {
    val bitmap = BitmapFactory.decodeFile(path)
    val baos = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
    val bytes = baos.toByteArray()
    return Base64.encodeToString(bytes, Base64.DEFAULT)
}

fun drawableToBase64(resources: Resources, resourceId: Int): String {
    val bitmap = BitmapFactory.decodeResource(resources, resourceId)
    val baos = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
    val bytes = baos.toByteArray()
    return Base64.encodeToString(bytes, Base64.DEFAULT)
}

fun getTimeInMillisFromDatePicker(datePicker: DatePicker): Long {
    val day = datePicker.dayOfMonth
    val month = datePicker.month
    val year = datePicker.year
    val calendar = Calendar.getInstance()
    calendar.set(year, month, day)
    return calendar.timeInMillis
}