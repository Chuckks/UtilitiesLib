package com.bbva.utilitieslib.extensions

private fun calcHexa(ch1: Char, ch2: Char) =
    ((ch1.toHexa() and 0xFF) shl 4 or (ch2.toHexa() and 0xFF)).toByte()

fun String.toHexaByte(startIndex: Int = 0): Byte{
    if (this.isEmpty())
        return 0x00

    if (startIndex > length - 1)
        throw IllegalArgumentException("startIndex [$startIndex] >= length -1 [$length-1]")

    return calcHexa(this[startIndex], this[startIndex + 1])
}

fun String.toHexaBytes(): ByteArray{
    if(this.isEmpty())
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