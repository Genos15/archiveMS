package com.thintwice.archive.mbompay.service

import java.nio.charset.StandardCharsets
import java.security.spec.KeySpec
import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

class AES256 {

    fun encrypt(strToEncrypt: String, secretKey: String, salt: String): String? {
        try {
            val iv = byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
            val ivSpec = IvParameterSpec(iv)
            val factory: SecretKeyFactory = SecretKeyFactory.getInstance(kAlgoSignature)
            val spec: KeySpec = PBEKeySpec(
                secretKey.toCharArray(),
                salt.toByteArray(),
                kEncryptionIterationCount,
                kEncryptionKeyLength,
            )
            val tmp: SecretKey = factory.generateSecret(spec)
            val secretKeyBuilt = SecretKeySpec(tmp.encoded, kAlgoName)
            val cipher: Cipher = Cipher.getInstance(kTransformationKey)
            cipher.init(Cipher.ENCRYPT_MODE, secretKeyBuilt, ivSpec)
            return Base64.getEncoder()
                .encodeToString(cipher.doFinal(strToEncrypt.toByteArray(charset = StandardCharsets.UTF_8)))
        } catch (e: Exception) {
            println("Error while encrypting: $e")
        }
        return null
    }

    fun decrypt(strToDecrypt: String, secretKey: String, salt: String): String? {
        try {
            val iv = byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
            val ivSpec = IvParameterSpec(iv)
            val factory = SecretKeyFactory.getInstance(kAlgoSignature)
            val spec: KeySpec = PBEKeySpec(
                secretKey.toCharArray(),
                salt.toByteArray(),
                kEncryptionIterationCount,
                kEncryptionKeyLength,
            )
            val tmp = factory.generateSecret(spec)
            val secretKeyBuilt = SecretKeySpec(tmp.encoded, kAlgoName)
            val cipher = Cipher.getInstance(kTransformationKey)
            cipher.init(Cipher.DECRYPT_MODE, secretKeyBuilt, ivSpec)
            return String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)))
        } catch (e: java.lang.Exception) {
            println("Error while decrypting: $e")
        }
        return null
    }

    companion object {
        const val kAlgoName = "AES"
        const val kAlgoSignature = "PBKDF2WithHmacSHA256"
        const val kTransformationKey = "AES/CBC/PKCS5PADDING"
        const val kEncryptionKeyLength = 256
        const val kEncryptionIterationCount = 65536
    }
}