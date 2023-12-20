package com.bbva.utilitieslib.security

import com.bbva.utilitieslib.extensions.toHexaBytes
import com.bbva.utilitieslib.extensions.toInt
import java.security.InvalidParameterException
import java.security.SecureRandom
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

class KeyUtils {
    enum class EAlgo{
        TDES,
        DES,
        RSA
    }

    enum class EJavaAlgo(val algo: EAlgo){
        AES(EAlgo.DES),
        DESede(EAlgo.TDES),
        RSA(EAlgo.RSA);

        companion object{
            fun findName(algo: EAlgo) = values().find { it.algo == algo }?.name ?: throw InvalidParameterException("Algo [$algo] is not supported")
            fun findAlgo(name: String) = values().find { it.name == name }?.algo ?: InvalidParameterException("Algo [$name] is not supported")
        }
    }

    enum class EKeyParity{
        EVEN,
        ODD,
        NONE
    }

    companion object{
        fun randomGenerator(length: Int): ByteArray{
            val array = ByteArray(length)
            SecureRandom().nextBytes(array)
            return array
        }

        fun createSecretKey(algo: EAlgo, key: String) = SecretKeySpec(key.toHexaBytes(), EJavaAlgo.findName(algo))

        fun createSecretKey(algo: EAlgo, key: ByteArray): SecretKey {
            return SecretKeySpec(key, algo.name)
        }

        fun adjustParity(parity: EKeyParity, key: ByteArray): ByteArray {
            if (parity == EKeyParity.NONE) return key

            val oddParity = parity == EKeyParity.ODD
            val newKey = ByteArray(key.size)
            var bite: Int

            for (index in key.indices){
                bite = key[index].toInt()
                newKey[index] = (bite and 0xfe or (bite shr 1 xor (bite shr 2) xor (bite shr 3) xor (bite shr 4)
                        xor (bite shr 5) xor (bite shr 6) xor (bite shr 7) xor 0x01) and oddParity.toInt()).toByte()
            }

            return key
        }

        fun keyGenerator(algo: EAlgo, bitLength: Int): SecretKey{
            val keyGen: KeyGenerator = KeyGenerator.getInstance(EJavaAlgo.findName(algo))
            keyGen.init(bitLength, SecureRandom.getInstance("SHA1PRNG"))
            return keyGen.generateKey()
        }
    }
}