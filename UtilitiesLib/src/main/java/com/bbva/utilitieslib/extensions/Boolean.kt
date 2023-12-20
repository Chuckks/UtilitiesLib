package com.bbva.utilitieslib.extensions

fun Boolean.toInt() = if (this) 1 else 0
fun Boolean.toByte() = toInt().toByte()