package com.katielonsdale.chatterbox

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.json.JSONObject
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
        checkUserLogInStatus()
    }

    private fun checkInitialization() {
        if (!isInitialized) {
            throw IllegalStateException("SessionManager is not initialized. Call init() once in your Application class.")
        }
    }

    fun getUserId(): String {
        checkInitialization()
        return sharedPreferences.getString("USER_ID", null) ?: ""
    }

    private fun checkUserLogInStatus(){
        checkInitialization()
        isJwtTokenUpToDate()
        if (_isUserLoggedIn.value) {
            setIsTouUpToDate(true)
        }
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

    fun saveSession(
        userId: String,
        jwtToken: String
    ) {
        checkInitialization()
        val editor = sharedPreferences.edit()
        editor.putString("JWT_TOKEN", jwtToken)
            .putString("USER_ID", userId)
            .apply()
        _isUserLoggedIn.value = true
    }

    fun getJwtToken(): String? {
        checkInitialization()
        return sharedPreferences.getString("JWT_TOKEN", null)
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

    private fun isJwtTokenUpToDate() {
        val token = getJwtToken()
        val isValid = isJwtStillValid(token)
        if (isValid) {
            // Proceed to main screen without login
            _isUserLoggedIn.value = true
        } else {
            clearSession()
        }
    }

    private fun isJwtStillValid(token: String?): Boolean {
        if (token == null) {
            return false
        }
        return try {
            val parts = token.split(".")
            val payload = String(android.util.Base64.decode(parts[1], android.util.Base64.URL_SAFE))
            val exp = JSONObject(payload).getLong("exp")
            val now = System.currentTimeMillis() / 1000
            now < exp
        } catch (e: Exception) {
            false
        }
    }
}
