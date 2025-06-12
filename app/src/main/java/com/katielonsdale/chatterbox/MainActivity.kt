package com.katielonsdale.chatterbox

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
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

        createNotificationChannels()
        val notificationsPermissionManager = NotificationsManager(this)

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
}