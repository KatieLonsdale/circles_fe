package com.katielonsdale.chatterbox.utils

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.katielonsdale.chatterbox.SessionManager
import com.katielonsdale.chatterbox.api.data.UserRequest
import com.katielonsdale.chatterbox.ui.notifications.NotificationsFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.katielonsdale.chatterbox.api.RetrofitClient.apiService

object NotificationsManager {
    const val TAG = "NotificationsManager"
    private var permissionGranted = false

    fun init(
        notificationPermissionGranted: Boolean,
    ){
        permissionGranted = notificationPermissionGranted
    }

    fun askNotificationPermission(
        userId: String
    ) {
        Log.d(TAG, "askNotificationPermission called with userId: $userId")
        val fragment = NotificationsFragment()

        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= 33) { // Use the literal value in case TIRAMISU is not defined
            Log.d(TAG, "Device is Android 13+ (TIRAMISU), checking permission status")
            if (permissionGranted) {
                // Permission already granted, get token
                getFcmToken(userId)
            } else {
                Log.d(
                    TAG,
                    "POST_NOTIFICATIONS permission NOT granted, launching permission request"
                )
                // Request permission and handle result
                fragment.requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            // For devices below API level 33, no runtime permission is needed
            Log.d(TAG, "Device is below Android 13, no permission needed")
            getFcmToken(userId)
        }
    }

    fun getFcmToken(userId: String) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            val token = task.result
            SessionManager.saveFcmToken(token)
            sendTokenToServer(token, userId)
        })
    }

    private fun sendTokenToServer(token: String, userId: String) {
        try {
            val tokenRequest = UserRequest(notificationsToken = token)

            apiService.updateUser(userId, tokenRequest).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (!response.isSuccessful) {
                        Log.e(TAG, "Failed to send FCM token to server: ${response.code()}")
                    }
                }
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.e(TAG, "Error sending FCM token to server", t)
                }
            })
        } catch (e: Exception) {
            Log.e(TAG, "Error sending FCM token: ${e.message}", e)
        }
    }

    fun clearTokenOnServer(userId: String) {
        try {
            val tokenRequest = UserRequest(notificationsToken = "")

            apiService.updateUser(userId, tokenRequest).enqueue(object :
                Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (!response.isSuccessful) {
                        Log.e(TAG, "Failed to clear FCM token on server: ${response.code()}")
                    }
                }
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.e(TAG, "Error clearing FCM token to server", t)
                }
            })
        } catch (e: Exception) {
            Log.e(TAG, "Error clearing FCM token: ${e.message}", e)
        }
    }
}