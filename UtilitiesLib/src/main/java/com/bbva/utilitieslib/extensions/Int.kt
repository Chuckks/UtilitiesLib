package com.bbva.utilitieslib.extensions

fun Int.toBoolean() = when (this) {
    1    -> true
    0    -> false
    else -> throw IllegalArgumentException("To Boolean Fail")
}

fun Int.isOdd(): Boolean{
    return (this and 0x01).toBoolean()
}

fun Int.isEven() = !isOdd()

fun Int.toHexString(): String {
    val result = this.toString(16)
    return if (result.length.isOdd())
        "0$result"
    else
        result
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

fun Int.getDigitCount(): Int {
    val value = this.toUInt()
    var count: Int = 0

    var currentValue = value
    while (currentValue > 0u) {
        currentValue /= 10u
        count++
    }

    return count
}

//@ -> return: 0x12/0x34/0x56/0x78/0x90 _ len2Convert: 10 _ source: 1234567890
fun Int.toDecimalToBcd(lenToConvert: Int): ByteArray {
    val aucTab = ByteArray(5)
    var src: Long = this.toLong()

    var ulTmp2 = 100000000L
    val size = aucTab.size

    for (index in 0 until size) {
        aucTab[index] = (src / ulTmp2).toByte()
        aucTab[index] = (((aucTab[index] / 10) shl 4) + (aucTab[index] % 10)).toByte()

        src %= ulTmp2
        ulTmp2 /= 100
    }

    var len2Convert = lenToConvert
    if (len2Convert > 5) {
        len2Convert = 5
    }

    val indexDest = len2Convert
    val destination = ByteArray(len2Convert)

    for (index in 0 until len2Convert) {
        destination[indexDest - 1 - index] = aucTab[4 - index]
    }

    return destination
}

