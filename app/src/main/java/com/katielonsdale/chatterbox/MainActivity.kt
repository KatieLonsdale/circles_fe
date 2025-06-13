package com.katielonsdale.chatterbox

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.katielonsdale.chatterbox.ui.MainScreen
import com.katielonsdale.chatterbox.utils.NotificationsManager

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Log the device SDK version
        Log.d(TAG, "Device SDK version: ${Build.VERSION.SDK_INT} (Android 13 is 33)")

        // Initialize the SessionManager
        SessionManager.init(this)

        // Notifications
        createNotificationChannels()
        val notificationsPermissionManager = NotificationsManager(this)
        checkPushNotificationPermissions()

        setContent {
            MaterialTheme {
                MainScreen(
                    route = intent?.getStringExtra("route"),
                    circleId = intent?.getStringExtra("circleId"),
                    postId = intent?.getStringExtra("postId"),
                    onRequestNotificationPermission = {
                        notificationsPermissionManager.requestNotificationPermissionIfNeeded()
                    }
                )
            }
        }
    }

    private fun createNotificationChannels() {
        val channel = NotificationChannel(
            "default_channel",
            "Default Notifications",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.description = "This is the default notification channel."

        val notificationManager =
            ContextCompat.getSystemService(this, NotificationManager::class.java)
        notificationManager?.createNotificationChannel(channel)
    }

    // we want to catch if a user has enabled permission outside the app
    // because we won't have created an FCM token for them yet
    private fun checkPushNotificationPermissions(){
        //for older versions
        val manager = NotificationManagerCompat.from(this)
        var permissionGranted = manager.areNotificationsEnabled()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionGranted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
            if (permissionGranted) {
                val fcmToken = SessionManager.getFcmToken()
                if (fcmToken == null) {
                    val notificationsManager = NotificationsManager(
                        this
                    )
                    notificationsManager.createFcmToken()
                }
            }
        }
    }
}