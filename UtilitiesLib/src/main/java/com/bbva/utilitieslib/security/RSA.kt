package com.bbva.utilitieslib.security

import android.content.Context
import android.util.Base64
import com.bbva.utilitieslib.extensions.getAssetToByteArray
import java.nio.charset.StandardCharsets
import java.security.Key
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import javax.crypto.Cipher

class RSA {

    lateinit var privateKey: PrivateKey
    lateinit var publicKey: PublicKey

    private fun getFileData(context: Context, fileName: String) =
        context.getAssetToByteArray(fileName)

    private fun readKey(context: Context, fileName: String, startKey: String, endKey: String): ByteArray {
        return base64Decoder(getFormatKey(String(getFileData(context, fileName), StandardCharsets.ISO_8859_1)
            , startKey, endKey))
    }

    enum class EKeyType {
        PRIVATE,
        PUBLIC
    }

    fun loadPrivateKey(context: Context, fileName: String) =
        setPrivateKey(readKey(context, fileName, PEM_RSA_PRIVATE_START, PEM_RSA_PRIVATE_END))

    fun loadPublicKey(context: Context, fileName: String) =
        setPublicKey(readKey(context, fileName, PEM_RSA_PUBLIC_START, PEM_RSA_PUBLIC_END))

    fun loadKeyFile(context: Context, fileName: String, keyType: EKeyType){
        when(keyType){
            EKeyType.PRIVATE -> loadPrivateKey(context, fileName)
            EKeyType.PUBLIC -> loadPublicKey(context, fileName)
        }
    }

    fun genKeyPair(size: Int){
        val(pubKey, privKey) = Companion.genKeyPair(size)
        publicKey = pubKey
        privateKey = privKey
    }

    private fun setPrivateKey(key: ByteArray) {
        privateKey = encodePrivateKey(key)
    }

    private fun setPrivateKey(key: String) {
        privateKey = encodePrivateKey(key)
    }

    private  fun setPublicKey(key: ByteArray) {
        publicKey = encodePublicKey(key)
    }

    private fun setPublicKey(key: String){
        publicKey = encodePublicKey(key)
    }

    fun getPrivateKeyData() = decode(privateKey)
    fun getPublicKeyData() = decode(publicKey)

    fun encrypt(data: String) = encrypt(data.toByteArray())
    fun encrypt(data: ByteArray) = baseCrypt(Cipher.ENCRYPT_MODE, publicKey, data )

    fun decrypt(data: String) = decrypt(data.toByteArray())
    fun decrypt(data: ByteArray) = baseCrypt(Cipher.DECRYPT_MODE, privateKey, data)

    private fun baseCrypt(mode: Int, key: Key, data: ByteArray): ByteArray{
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(mode, key)
        return cipher.doFinal(data)
    }
    companion object{
        private const val ALGORITHM = "RSA"
        private const val TRANSFORMATION = "RSA/ECB/PKCS1Padding"
        private const val PEM_RSA_PRIVATE_START = "-----BEGIN RSA PRIVATE KEY-----"
        private const val PEM_RSA_PRIVATE_END = "-----END RSA PRIVATE KEY-----"
        private const val PEM_RSA_PUBLIC_START = "-----BEGIN PUBLIC KEY-----"
        private const val PEM_RSA_PUBLIC_END = "-----END PUBLIC KEY-----"
        private const val REPLACE_STRING = ""
        private const val END_LINE_AND_RETURN = "\r\n"
        private const val END_LINE = "\n"

        private fun getFormatKey(data: String, startKey: String, endKey: String) =
            data.replace(startKey, REPLACE_STRING)
                .replace(endKey, REPLACE_STRING)
                .replace(END_LINE_AND_RETURN, REPLACE_STRING)
                .replace(END_LINE, REPLACE_STRING)

        private fun base64Decoder(data: String) = Base64.decode(data, Base64.DEFAULT)

        fun genKeyPair(size: Int): Pair<PublicKey, PrivateKey>{
            val kpg = KeyPairGenerator.getInstance(ALGORITHM)
            kpg.initialize(size)

            val kp = kpg.genKeyPair()
            return Pair(kp.public, kp.private)
        }

        fun encodePrivateKey(key: ByteArray) =
            KeyFactory.getInstance(ALGORITHM).generatePrivate(PKCS8EncodedKeySpec(key))

        fun encodePrivateKey(key: String): PrivateKey {
            val privateKey = getFormatKey(key, PEM_RSA_PRIVATE_START, PEM_RSA_PUBLIC_END)
            return encodePrivateKey(base64Decoder(privateKey))
        }

        fun encodePublicKey(key: ByteArray) =
            KeyFactory.getInstance(ALGORITHM).generatePublic(PKCS8EncodedKeySpec(key))

        fun encodePublicKey(key: String): PublicKey{
            val publickey = getFormatKey(key, PEM_RSA_PUBLIC_START, PEM_RSA_PUBLIC_END)
            return  encodePublicKey(base64Decoder(publickey))
        }

        fun decode(privateKey: PrivateKey) = PKCS8EncodedKeySpec(privateKey.encoded).encoded
            ?: throw Exception("Private Key -> Null Pkcs8 Value")
        fun decode(publicKey: PublicKey) = PKCS8EncodedKeySpec(publicKey.encoded).encoded
            ?: throw Exception("Publiv Key -> Null Pkcs8 Value")
    }
}