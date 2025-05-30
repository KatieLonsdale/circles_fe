package com.katielonsdale.chatterbox

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.katielonsdale.chatterbox.api.data.UserRequest
import com.katielonsdale.chatterbox.ui.MainScreen
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.katielonsdale.chatterbox.api.RetrofitClient.apiService

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    
    // Permission request launcher
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        Log.d(TAG, "Permission request result received: $isGranted")
        if (isGranted) {
            // Permission is granted, get the FCM token
            Log.d(TAG, "Notification permission granted, getting FCM token with userId: $pendingUserId")
            getFcmToken(pendingUserId)
            Log.d(TAG, "Notification permission granted")
        } else {
            Log.d(TAG, "Notification permission denied")
            // If permission is denied, make sure we don't have any token saved
            SessionManager.saveFcmToken("")
            // If user ID is available, clear the token on the server as well
            pendingUserId?.let { userId ->
                clearTokenOnServer(userId)
            }
        }
        // Clear the pending userId
        Log.d(TAG, "Clearing pendingUserId: $pendingUserId")
        pendingUserId = null
    }

    private var pendingUserId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Log the device SDK version
        Log.d(TAG, "Device SDK version: ${Build.VERSION.SDK_INT} (Android 13 is 33)")
        
//        createNotificationChannels()
        // Initialize the SessionManager
        SessionManager.init(this)
        
        setContent {
            MaterialTheme {
                MainScreen(
                    mainActivity = this,
                    route = intent?.getStringExtra("route"),
                    circleId = intent?.getStringExtra("circleId"),
                    postId = intent?.getStringExtra("postId"),
                )
            }
        }
    }
    
    fun askNotificationPermission(userId: String? = null) {
        Log.d(TAG, "askNotificationPermission called with userId: $userId")
        
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= 33) { // Use the literal value in case TIRAMISU is not defined
            Log.d(TAG, "Device is Android 13+ (TIRAMISU), checking permission status")
            
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // Permission already granted, get token
                Log.d(TAG, "POST_NOTIFICATIONS permission already granted")
                getFcmToken(userId)
            } else {
                // Store the userId for later use after permission is granted
                Log.d(TAG, "POST_NOTIFICATIONS permission NOT granted, launching permission request")
                if (userId != null) {
                    this.pendingUserId = userId
                    Log.d(TAG, "Stored userId: $userId for later use")
                }
                // Request permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            // For devices below API level 33, no runtime permission is needed
            Log.d(TAG, "Device is below Android 13, no permission needed")
            getFcmToken(userId)
        }
    }
    
    private fun getFcmToken(userId: String? = null) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            
            // Log and save the token
            Log.d(TAG, "FCM Token: $token")
            SessionManager.saveFcmToken(token)
            
            // Send token to server only if userId is provided
            if (userId != null) {
                sendTokenToServer(token, userId)
            }
        })
    }
    
    private fun sendTokenToServer(token: String, userId: String) {
        try {
            // Use RetrofitClient which has already initialized the API service
            val apiService = com.katielonsdale.chatterbox.api.RetrofitClient.apiService
            val tokenRequest = UserRequest(notificationsToken = token)
            
            apiService.updateUser(userId, tokenRequest).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Log.d(TAG, "FCM token successfully sent to server")
                    } else {
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

    private fun clearTokenOnServer(userId: String) {
        try {
            // Send a blank token to clear it on the server
            val tokenRequest = UserRequest(notificationsToken = "")
            
            apiService.updateUser(userId, tokenRequest).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Log.d(TAG, "Empty FCM token successfully sent to server (permissions denied)")
                    } else {
                        Log.e(TAG, "Failed to send empty FCM token to server: ${response.code()}")
                    }
                }
                
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.e(TAG, "Error sending empty FCM token to server", t)
                }
            })
        } catch (e: Exception) {
            Log.e(TAG, "Error sending empty FCM token: ${e.message}", e)
        }
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "default_channel",
                "Default Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = "This is the default notification channel."

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }
}