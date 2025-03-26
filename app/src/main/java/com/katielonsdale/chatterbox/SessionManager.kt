package com.katielonsdale.chatterbox

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.OffsetDateTime

object SessionManager {

    private const val PREF_NAME = "UserSession"
    private lateinit var sharedPreferences: SharedPreferences
    private var isInitialized = false
    private var _isUserLoggedIn = MutableStateFlow(false) // Reactive state
    private var _isTouUpToDate = MutableStateFlow(false) // Reactive state
    val isUserLoggedIn: StateFlow<Boolean> = _isUserLoggedIn
    val isTouUpToDate: StateFlow<Boolean> = _isTouUpToDate
    val latestTouDate = OffsetDateTime.parse("2024-12-21T14:30:00Z")

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
            _isUserLoggedIn.value = true
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
        return _isUserLoggedIn.value
    }

    fun isTouUpToDate(): Boolean {
        checkInitialization()
        return _isTouUpToDate.value
    }

    fun clearSession() {
        checkInitialization()
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
        _isUserLoggedIn.value = false
    }

    fun saveToken(token: String) {
        checkInitialization()
        val editor = sharedPreferences.edit()
        editor.putString("token", token)
        editor.apply()
        _isUserLoggedIn.value = true
    }

    fun getToken(): String? {
        checkInitialization()
        return sharedPreferences.getString("token", null)
    }

    fun setIsTouUpToDate(isUpToDate: Boolean) {
        _isTouUpToDate.value = isUpToDate
    }

    fun saveFcmToken(fcmToken: String) {
        checkInitialization()
        val editor = sharedPreferences.edit()
        editor.putString("notifications_token", fcmToken)
        editor.apply()
    }

    fun getFcmToken(): String? {
        checkInitialization()
        return sharedPreferences.getString("notifications_token", null)
    }
}
