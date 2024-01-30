package com.bbva.utilitieslib.extensions

import com.bbva.utilitieslib.utils.Convert
import kotlin.experimental.and

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

fun ByteArray.readBbcPair(offset: Int, nibbles: Int): Pair<Int, Int> {
    var adjustedNibbles = nibbles

    if (adjustedNibbles and 0x01 != 0) {
        ++adjustedNibbles
    }

    val count = adjustedNibbles / 2
    val result = copyOfRange(offset, offset + count).toBcdToDecimal(adjustedNibbles)
    //val result = Convert.toBcdToDecimal(adjustedNibbles, copyOfRange(offset, offset + count))

    return Pair(count, result)
}

//@ -> return: 0x01/0x23/0x45/  source: '12345'
fun ByteArray.toAsciiToBcd(): ByteArray {
    var lenToConvert = this.size

    if (lenToConvert % 2 != 0) {
        lenToConvert++
    }

    lenToConvert /= 2
    return this.toAsciiToBcd(lenToConvert, 0)
}

//@ -> return: 0x01/0x23/0x45/  source: '12345'  startIndex: 0
fun ByteArray.toAsciiToBcd(lenToConvert: Int, startIndex: Int): ByteArray {
   // var source = this
    var lenSrc = this.size
    var newStartIndex = startIndex

    if (lenSrc - newStartIndex <= 0) {
        throw IllegalArgumentException("( lenSrc [$lenSrc] - startIndex [$newStartIndex] ) <= 0")
    }

    lenSrc -= newStartIndex
    var indexDest: Int = if (lenToConvert > lenSrc / 2) {
        lenToConvert - (lenSrc + 1) / 2
    } else {
        0
    }

    var chDest: Byte
    val destination = ByteArray(lenToConvert)

    var index = 0
    while (index < (lenSrc + 1) / 2) {
        chDest = if ((lenSrc % 2 == 0 && index == 0) || index > 0) {
            (this[newStartIndex++].toInt() shl 4 and 0xF0).toByte()
        } else {
            0
        }

        chDest = (chDest + (this[newStartIndex++].toInt() and 0x0F)).toByte()
        destination[indexDest++] = chDest

        if (indexDest >= lenToConvert) {
            break
        }

        index++
    }

    return destination
}

//@ -> return: 1234  len2Convert:4  source: 0x12/0x34  startIndex: 0
fun ByteArray.toBcdToDecimal(lenToConvert: Int, startIndex: Int = 0): Int {
    val lenSrc = this.size

    if ((lenToConvert / 2) > (lenSrc - startIndex))
        throw Exception("lenToConvert/2 [${lenToConvert / 2}] > ( lenSrc [$lenSrc] - startIndex [$startIndex] )")

    var ulTmp1 = 0
    var ulTmp2 = 1
    var index = 0

    var localStartIndex = (startIndex + ((lenToConvert + 1) / 2)).toInt()

    repeat(lenToConvert) {
        val ucCh: Byte = if (index % 2 == 1) {
            (this[localStartIndex].toInt() shr 4 and 0x0F).toByte()
        } else {
            this[--localStartIndex] and 0x0F
        }

        ulTmp1 += ulTmp2 * ucCh.toInt()

        if (ulTmp2 == 1_000_000_000) {
            ulTmp2 = 0
        } else {
            ulTmp2 *= 10
        }

        index++
    }

    return ulTmp1
}

//@ -> return: "1234"  source: 0x12/0x34
fun ByteArray.toBcdToAscii(): String =
    this.toBcdToAscii(this.size * 2, 0)

//@ -> return: "1234"  len2Convert: 4  source: 0x12/0x34  startIndex: 0
fun ByteArray.toBcdToAscii(startIndex: Int): String =
    this.toBcdToAscii((this.size - startIndex) * 2, startIndex)

//@ -> return: "1234"  len2Convert: 4  source: 0x12/0x34  startIndex: 0
fun ByteArray.toBcdToAscii(len2Convert: Int, source: ByteArray): String =
    this.toBcdToAscii(len2Convert, 0)

//@ -> return: "1234"  len2Convert: 4  source: 0x12/0x34  startIndex: 0
fun ByteArray.toBcdToAscii(len2Convert: Int, startIndex: Int): String {
    val lenDest = len2Convert / 2
    val lenSrc = this.size
    var newStartIndex = startIndex

    if (lenDest > (lenSrc - newStartIndex)) {
        throw IllegalArgumentException("len2Convert [$lenDest] > ( lenSrc [$lenSrc] - startIndex [$newStartIndex] )")
    }

    var indexDest = 0
    val destination = StringBuilder(len2Convert)

    if (len2Convert % 2 != 0) {
        destination.append(((this[newStartIndex++] and 0x0F) + 0x30).toChar())
    }

    for (index in 0 until lenDest) {
        val ch = this[newStartIndex++].toInt()

        destination.append(((ch and 0xF0 ushr 4) + 0x30).toChar())
        destination.append(((ch and 0x0F) + 0x30).toChar())
    }

    return destination.toString()
}

fun ByteArray.toBcdToHexa(): ByteArray {
    val length = this.size
    val result = ByteArray(length)

    for (index in 0 until length) {
        result[index] = this[index].toBcdToHexa() //Convert.toBcdToHexa(this[index])
    }

    return result
}

fun ByteArray.toHexaToAscii(source: ByteArray): ByteArray =
    this.toHexaToAscii(this.size * 2)

//@ -> return: '1AC45'  source: 0x01/0xAC/0x45  uiLen 5
fun ByteArray.toHexaToAscii(len2Convert: Int): ByteArray {
    var indexSrc = 0
    var indexDest = 0
    val destination = ByteArray(len2Convert)

    if (len2Convert % 2 != 0) {
        destination[indexDest++] = ((this[indexSrc++] and 0x0F) + 0x30).toByte()
    }

    for (index in 0 until len2Convert / 2) {
        destination[indexDest++] = (((this[indexSrc] and 0xF0.toByte()).toInt() shr 4) + 0x30).toByte()
        destination[indexDest++] = ((this[indexSrc++] and 0x0F) + 0x30).toByte()
    }

    for (index in destination.indices) {
        var ch = destination[index]
        if (ch >= 0x3A.toByte()) {
            destination[index] = (ch + 7).toByte()
        }
    }

    return destination
}
