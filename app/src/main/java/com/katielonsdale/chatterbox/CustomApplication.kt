package com.katielonsdale.chatterbox

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging

class CustomApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        
        // Disable Firebase auto-initialization to prevent automatic token generation
        FirebaseMessaging.getInstance().isAutoInitEnabled = false
        Log.d("CustomApplication", "Firebase auto-initialization disabled")
        
//        // Initialize the SessionManager
//        SessionManager.init(this)
//
//        // Create notification channels
//        createNotificationChannels()
    }
//
//    private fun createNotificationChannels() {
//        val channel = NotificationChannel(
//            "default_channel",
//            "Default Notifications",
//            NotificationManager.IMPORTANCE_DEFAULT
//        ).apply {
//            description = "This is the default notification channel for all app notifications"
//        }
//
//        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        notificationManager.createNotificationChannel(channel)
//    }
}
