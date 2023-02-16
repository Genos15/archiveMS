package com.thintwice.archive.mbompay.configuration.security

import com.thintwice.archive.mbompay.configuration.bundle.RB
import org.springframework.stereotype.Component
import java.util.*
import javax.annotation.PostConstruct
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

@Component
class CryptoAES(sr: RB) {

    private val cryptoKey = sr.l("stripe.secret.aes.crypto.key")

    @PostConstruct
    fun init() {
        println("-- initializing the CRYPTO AES Component")

        val encrypted =
            "bO9ICgv4OZmIkyx/4c8nAXsVoVfgYty9GwlsADXSeqCidie0tB3e2HHMtfprG0NbRZE3GGvfF3iCMBI4vnHbHatW+BnhZCc4JisTvD7kxSfFR1DNfntoF7lTSX+DD0HGvvZNBPu0sMRMSTTWBnDu6//65KPKVklnxe4t7ZZu1zk="
        val decrypted = decrypt(encrypted = encrypted)

        println("--decrypted = $decrypted")

        val json = """
            { "name": "Arnaud", "number": "4000000560000004", "expiredMonth": 12, "expiredYear": 2026  }
        """.trimIndent()

        val encryptedJson = encrypt(source = json)
        println("Encrypt -> $encryptedJson")



        val jsonCvc = """
            { "id": "3abb11f4-6c03-4929-a172-912966296935", "cvc": "123" }
        """.trimIndent()

        val encryptedJsonCvc = encrypt(source = jsonCvc)
        println("Encrypt - CVC -> $encryptedJsonCvc")
    }

    fun encrypt(source: String): String? {
        try {
            val cipher = Cipher.getInstance(kTransformationKey)
            val iv = IvParameterSpec(cryptoKey.substring(0, 16).toByteArray(charset("UTF-8")))
            val keySpec = SecretKeySpec(cryptoKey.toByteArray(charset("UTF-8")), kAlgoName)
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv)
            val encrypted = cipher.doFinal(source.toByteArray())
            return String(Base64.getEncoder().encode(encrypted))
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return null
    }

    fun decrypt(encrypted: String): String? {
        try {
            val iv = IvParameterSpec(cryptoKey.substring(0, 16).toByteArray(charset("UTF-8")))
            val keySpec = SecretKeySpec(cryptoKey.toByteArray(charset("UTF-8")), kAlgoName)
            val cipher = Cipher.getInstance(kTransformationKey)
            cipher.init(Cipher.DECRYPT_MODE, keySpec, iv)
            val original = cipher.doFinal(Base64.getDecoder().decode(encrypted))
            return String(original)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return null
    }

    companion object {
        const val kAlgoName = "AES"
        const val kTransformationKey = "AES/CBC/PKCS5PADDING"
    }
}