package com.example.innercircles.utils

import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import org.bouncycastle.internal.asn1.bsi.BSIObjectIdentifiers.algorithm
import java.io.IOException
import java.security.GeneralSecurityException
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object UploadManager {
//    TODO: potentially don't need this class
    //    when upload manager is initiated, will need to pass it the shared preferences
    private lateinit var sharedPreferences: SharedPreferences

    fun initialize(sharedPrefs: SharedPreferences) {
        sharedPreferences = sharedPrefs
    }

    //    generate key for encoding/decoding images and videos

}