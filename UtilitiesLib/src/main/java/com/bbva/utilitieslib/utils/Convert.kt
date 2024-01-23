package com.bbva.utilitieslib.utils

import kotlin.experimental.and

class Convert {

    companion object{

        //@ -> return: 1234  len2Convert:4  source: 0x12/0x34  startIndex: 0
        fun toBcdToDecimal(lenToConvert: Int, source: ByteArray, startIndex: Int = 0): Int {
            val lenSrc = source.size

            if ((lenToConvert / 2) > (lenSrc - startIndex))
                throw Exception("lenToConvert/2 [${lenToConvert / 2}] > ( lenSrc [$lenSrc] - startIndex [$startIndex] )")

            var ulTmp1 = 0
            var ulTmp2 = 1
            var index = 0

            var localStartIndex = (startIndex + ((lenToConvert + 1) / 2)).toInt()

            repeat(lenToConvert) {
                val ucCh: Byte = if (index % 2 == 1) {
                    (source[localStartIndex].toInt() shr 4 and 0x0F).toByte()
                } else {
                    source[--localStartIndex] and 0x0F
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

        //@ -> return: 0x01/0x23/0x45/  source: '12345'
        fun toAsciiToBcd(source: ByteArray): ByteArray {
            var lenToConvert = source.size

            if (lenToConvert % 2 != 0) {
                lenToConvert++
            }

            lenToConvert /= 2
            return toAsciiToBcd(lenToConvert, source, 0)
        }

        //@ -> return: 0x01/0x23/0x45/  source: '12345'  startIndex: 0
        fun toAsciiToBcd(lenToConvert: Int, source: ByteArray, startIndex: Int): ByteArray {
            var lenSrc = source.size
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
                    (source[newStartIndex++].toInt() shl 4 and 0xF0).toByte()
                } else {
                    0
                }

                chDest = (chDest + (source[newStartIndex++].toInt() and 0x0F)).toByte()
                destination[indexDest++] = chDest

                if (indexDest >= lenToConvert) {
                    break
                }

                index++
            }

            return destination
        }

        //@ -> return: 0x12/0x34/0x56/0x78/0x90 _ len2Convert: 10 _ source: 1234567890
        fun toDecimalToBcd(lenToConvert: Int, source: Int): ByteArray {
            val aucTab = ByteArray(5)
            var src: Long = source.toLong()

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

        //@ -> return: "1234"  source: 0x12/0x34
        fun toBcdToAscii(source: ByteArray): String {
            return toBcdToAscii(source.size * 2, source, 0)
        }

        //@ -> return: "1234"  len2Convert: 4  source: 0x12/0x34  startIndex: 0
        fun toBcdToAscii(source: ByteArray, startIndex: Int): String {
            return toBcdToAscii((source.size - startIndex) * 2, source, startIndex)
        }

        //@ -> return: "1234"  len2Convert: 4  source: 0x12/0x34  startIndex: 0
        fun toBcdToAscii(len2Convert: Int, source: ByteArray): String {
            return toBcdToAscii(len2Convert, source, 0)
        }

        //@ -> return: "1234"  len2Convert: 4  source: 0x12/0x34  startIndex: 0
        fun toBcdToAscii(len2Convert: Int, source: ByteArray, startIndex: Int): String {
            val lenDest = len2Convert / 2
            val lenSrc = source.size
            var newStartIndex = startIndex

            if (lenDest > (lenSrc - newStartIndex)) {
                throw IllegalArgumentException("len2Convert [$lenDest] > ( lenSrc [$lenSrc] - startIndex [$newStartIndex] )")
            }

            var indexDest = 0
            val destination = StringBuilder(len2Convert)

            if (len2Convert % 2 != 0) {
                destination.append(((source[newStartIndex++] and 0x0F) + 0x30).toChar())
            }

            for (index in 0 until lenDest) {
                val ch = source[newStartIndex++].toInt()

                destination.append(((ch and 0xF0 ushr 4) + 0x30).toChar())
                destination.append(((ch and 0x0F) + 0x30).toChar())
            }

            return destination.toString()
        }

        fun toBcdToHexa(value: Byte): Byte {
            return ((value.toInt() shr 4) * 10 + (value.toInt() and 0x0F)).toByte()
        }

        fun toBcdToHexa(value: ByteArray): ByteArray {
            val length = value.size
            val result = ByteArray(length)

            for (index in 0 until length) {
                result[index] = toBcdToHexa(value[index])
            }

            return result
        }

        //@ -> return: '1AC45'  source: 0x01/0xAC/0x45  uiLen 5
        fun toHexa2Ascii(len2Convert: Int, source: ByteArray): ByteArray {
            var indexSrc = 0
            var indexDest = 0
            val destination = ByteArray(len2Convert)

            if (len2Convert % 2 != 0) {
                destination[indexDest++] = ((source[indexSrc++] and 0x0F) + 0x30).toByte()
            }

            for (index in 0 until len2Convert / 2) {
                destination[indexDest++] = (((source[indexSrc] and 0xF0.toByte()).toInt() shr 4) + 0x30).toByte()
                destination[indexDest++] = ((source[indexSrc++] and 0x0F) + 0x30).toByte()
            }

            for (index in destination.indices) {
                var ch = destination[index]
                if (ch >= 0x3A.toByte()) {
                    destination[index] = (ch + 7).toByte()
                }
            }

            return destination
        }

        fun toHexa2Ascii(source: ByteArray): ByteArray {
            return toHexa2Ascii(source.size * 2, source)
        }

    }
}