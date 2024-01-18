package com.bbva.utilitieslib.extensions

fun Int.toBoolean() = when (this) {
    1    -> true
    0    -> false
    else -> throw IllegalArgumentException("To Boolean Fail")
}

fun Int.isOdd(): Boolean{
    return (this and 0x01).toBoolean()
}

fun Int.toBcd(): ByteArray {
    require(this >= 0) { "Input value must be non-negative." }

    val stringValue = toString()
    val result = ByteArray((stringValue.length + 1) / 2)

    var resultIndex = 0
    var tempByte = 0
    var shift = 0

    for (i in stringValue.length - 1 downTo 0) {
        val digit = Character.digit(stringValue[i], 10)
        tempByte = tempByte or (digit shl shift)

        if (shift == 4 || i == 0) {
            result[resultIndex] = tempByte.toByte()
            resultIndex++
            tempByte = 0
            shift = 0
        } else {
            shift += 4
        }
    }

    result.reverse()
    return result
}