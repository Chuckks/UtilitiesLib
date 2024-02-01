package com.bbva.utilitieslib.extensions

fun String.toBoolean(): Boolean =
    when (this) {
        "0"  -> false
        "1"  -> true
        else -> throw IllegalArgumentException("ilegal value $this")
    }

fun String.formatAmount(symbol: String, decimal: Int): String {
    val length = this.length
    val zeros = "00000000"
    var amountAux = StringBuilder()

    if (length < decimal) {
        val decimalsAdd = decimal - length
        amountAux.append("0.")
            .append(zeros.substring(0 until decimalsAdd))
            .append(this)
    }
    else {
        val calc = length - decimal
        amountAux.append(this.substring(0, calc))
            .append(".")
            .append(this.substring(calc))
    }
    return amountAux.toString().toDouble().formatAmount(symbol)
}

fun String.isAlphabetic(): Boolean {
    for (ch in this) {
        if (!ch.isLetter())
            return false
    }
    return true
}

fun String.isAlphanumeric(): Boolean {
    for (ch in this) {
        if (!ch.isLetterOrDigit())
            return false
    }
    return true
}

private fun calcHexa(ch1: Char, ch2: Char) =
    ((ch1.toHexa() and 0xFF) shl 4 or (ch2.toHexa() and 0xFF)).toByte()

fun String.toHexaByte(startIndex: Int = 0): Byte {

    if (this.isEmpty())
        return 0x00

    if (startIndex >= length - 1)
        throw IllegalArgumentException("startIndex [$startIndex] >= length -1 [$length-1]")

    return calcHexa(this[startIndex], this[startIndex + 1])
}

fun String.toHexaChar(startIndex: Int = 0): Char {
    if (this.isEmpty())
        return Char.MIN_VALUE

    if (startIndex >= length - 1)
        throw IllegalArgumentException("startIndex [$startIndex] >= length -1 [$length-1]")

    return ((Character.digit(this[startIndex], 16) shl 4) + Character.digit(this[startIndex + 1], 16)).toChar()
}

fun String.toHexaAscii(): String {
    var value = StringBuilder()
    for (i in 0 until this.length step 2) {
        value.append(this.toHexaChar(i))
    }
    return value.toString()
}

fun String.toHexaBytes(): ByteArray {

    if (this.isEmpty())
        return byteArrayOf()

    val value = if (length.isOdd())
        "0$this"
    else
        this

    var pos = 0
    val result = ByteArray(value.length shr 1)

    for (index in value.indices step 2)
        result[pos++] = value.toHexaByte(index)

    return result
}

fun String.isNumber(): Boolean {
    for (ch in this) {
        if (!ch.isDigit())
            return false
    }
    return true
}

fun String.substring(char: String, count: Int): String {
    if (this.length < count)
        return this

    return if (this.contains(char))
        this.substringAfter(char).substring(0, count)
    else
        this
}