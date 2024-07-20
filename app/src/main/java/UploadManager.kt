package com.example.innercircles

import android.content.SharedPreferences
import android.util.Base64
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec
import javax.crypto.KeyGenerator
import javax.crypto.Cipher

object UploadManager {
//    when upload manager is initiated, will need to pass it the shared preferences
    private lateinit var sharedPreferences: SharedPreferences

    fun initialize(sharedPrefs: SharedPreferences) {
        sharedPreferences = sharedPrefs
    }

//    generate key for encoding/decoding images and videos
    fun generateSecretKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator.init(256) // 256-bit AES key
        return keyGenerator.generateKey()
    }

    // Store the key
    fun storeKey(key: ByteArray) {
        sharedPreferences.edit().putString("encryption_key", Base64.encodeToString(key, Base64.DEFAULT)).apply()
    }


    // Retrieve the key
    fun getKey(): ByteArray {
        val storedKey = sharedPreferences.getString("encryption_key", null) ?: throw IllegalStateException("Key not found")
        return Base64.decode(storedKey, Base64.DEFAULT)
    }

//    functions to encrypt and decrypt photos and videos
    fun encryptData(data: ByteArray, secretKey: SecretKey): ByteArray {
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        return cipher.doFinal(data)
    }

    fun decryptData(encryptedData: ByteArray, secretKey: SecretKey): ByteArray {
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.DECRYPT_MODE, secretKey)
        return cipher.doFinal(encryptedData)
    }
}