package com.bbva.utilitieslib.extensions

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun Date.format(dateFormat: String, timeZone: TimeZone = TimeZone.getTimeZone("UTC")): String {
    val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
    formatter.timeZone = timeZone
    return formatter.format(this)
}