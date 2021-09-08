package com.ikm.inventoryqrscanner.util


import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

fun timestampToString(timestamp: Timestamp?): String {
    return if (timestamp != null ) {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        dateFormat.format(timestamp.toDate())
    } else ""
}

fun stringToTimestamp(date: String?): Timestamp?{

    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    return if (!date.isNullOrEmpty()) {
        Timestamp(dateFormat.parse(date))
    } else null
}

fun dateFormat(string: String): Boolean {
    val day = string.substring(0, 2)
    val month = string.substring(3, 5)
    val year = string.substring(6, 10)

    return !(day.toInt() > 31 || month.toInt() > 12 || year.toInt() > 2100)
}

