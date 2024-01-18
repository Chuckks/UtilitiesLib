package com.bbva.utilitieslib.extensions

fun ByteArray.toHexaString(): String {
    val strBuilder = StringBuilder()
    for (ch in this) {
        strBuilder.append(String.format("%02X", ch))
    }
    return strBuilder.toString()
}

fun ByteArray.toHexBcd(): ByteArray {
    require(size % 2 == 0) { "ByteArray length must be even." }

    val result = ByteArray(size / 2)
    var resultIndex = 0

    for (i in indices step 2) {
        val highNibble = Character.digit(this[i].toChar(), 16)
        val lowNibble = Character.digit(this[i + 1].toChar(), 16)

        result[resultIndex] = ((highNibble shl 4) or lowNibble).toByte()
        resultIndex++
    }

    return result
}

fun ByteArray.readBcd(startIndex: Int, counter: Int): Int {
    require(startIndex >= 0 && counter >= 0 && startIndex + counter <= size) {
        "Invalid startIndex or counter for ByteArray readBCD operation."
    }

    var result = 0
    var factor = 1

    for (i in startIndex + counter - 1 downTo startIndex) {
        val byteValue = this[i].toInt() and 0xFF
        result += (byteValue % 10) * factor
        result += ((byteValue / 10) % 10) * factor * 10
        factor *= 100
    }

    return result
}