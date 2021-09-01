package com.ikm.inventoryqrscanner.util

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.Timestamp
import com.google.type.DateTime
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.text.DateFormat
import java.time.format.DateTimeFormatter
import java.util.*

fun stringToDate(string: String?): Date? {
    return if (string != null ) {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        dateFormat.parse(string)
    } else null
}

fun amountFormat(number: Int): String{
    val decimalFormat: NumberFormat = DecimalFormat("#,###")
    return decimalFormat.format(number)
}

