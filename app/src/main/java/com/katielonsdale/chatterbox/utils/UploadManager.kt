package com.katielonsdale.chatterbox.utils

import android.content.SharedPreferences

object UploadManager {
//    TODO: potentially don't need this class
    //    when upload manager is initiated, will need to pass it the shared preferences
    private lateinit var sharedPreferences: SharedPreferences

    fun initialize(sharedPrefs: SharedPreferences) {
        sharedPreferences = sharedPrefs
    }

    //    generate key for encoding/decoding images and videos

}