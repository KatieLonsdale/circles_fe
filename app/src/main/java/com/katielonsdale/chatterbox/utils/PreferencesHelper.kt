package com.katielonsdale.chatterbox.utils

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

class PreferencesHelper(context: Context) {

    private val prefsFileName = "secure_prefs"
    private val userIdKey = "USER_ID"
    private val sharedPreferences = EncryptedSharedPreferences.create(
        prefsFileName,
        MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveUserIdSecurely(userId: String) {
        sharedPreferences.edit().putString(userIdKey, userId).apply()
    }

    fun getUserId(): String? {
        return sharedPreferences.getString(userIdKey, null)
    }
}