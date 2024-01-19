package com.bbva.utilitieslib.utils

import kotlin.experimental.and

class Convert {

    companion object{

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
    }
}