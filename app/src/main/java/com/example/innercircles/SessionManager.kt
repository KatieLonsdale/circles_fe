package com.example.innercircles

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import java.time.LocalDateTime

object SessionManager {

    private const val PREF_NAME = "UserSession"
    private lateinit var sharedPreferences: SharedPreferences
    private var isInitialized = false
    private var isSignedIn = false
    val latestTouDate = LocalDateTime.parse("2024-12-21T14:30:00Z")

    fun init(context: Context) {
        if (!isInitialized) {
            val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

            sharedPreferences = EncryptedSharedPreferences.create(
                PREF_NAME,
                masterKeyAlias,
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
            isInitialized = true
        }
    }

    private fun checkInitialization() {
        if (!isInitialized) {
            throw IllegalStateException("SessionManager is not initialized. Call init() once in your Application class.")
        }
    }

    fun saveUserId(userId: String?) {
        if (userId != null) {
            checkInitialization()
            val editor = sharedPreferences.edit()
            editor.putString("userId", userId)
            editor.apply()
            isSignedIn = true
        } else {
             Log.d("Session Manager.saveUserId()", "userId null")
        }
    }

    fun getUserId(): String? {
        checkInitialization()
        return sharedPreferences.getString("userId", null)
    }

    fun isUserLoggedIn(): Boolean {
        checkInitialization()
        return isSignedIn
    }

    fun clearSession() {
        checkInitialization()
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
        isSignedIn = false
    }

    fun saveToken(token: String) {
        checkInitialization()
        val editor = sharedPreferences.edit()
        editor.putString("token", token)
        editor.apply()
    }

    fun getToken(): String? {
        checkInitialization()
        return sharedPreferences.getString("token", null)
    }
}
