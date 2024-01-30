package com.bbva.utilitieslib.extensions

fun Byte.toBcdToHexa(): Byte {
    return ((this.toInt() shr 4) * 10 + (this.toInt() and 0x0F)).toByte()
}