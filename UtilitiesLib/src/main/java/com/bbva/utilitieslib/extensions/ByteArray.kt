package com.bbva.utilitieslib.extensions

fun ByteArray.toHexaString(): String {
    val strBuilder = StringBuilder()
    for (ch in this) {
        strBuilder.append(String.format("%02X", ch))
    }
    return strBuilder.toString()
}