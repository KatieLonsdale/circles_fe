package com.katielonsdale.chatterbox

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.katielonsdale.chatterbox.api.ApiClient
import com.katielonsdale.chatterbox.api.data.UserRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val TAG = "MyFirebaseMessagingService"
    
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "Message received: ${remoteMessage.data}")

        val notification = remoteMessage.notification
        val data = remoteMessage.data

        if (notification != null) {
            showNotification(notification.title, notification.body, data)
        } else {
            // If no notification payload, still show a custom one from data
            showNotification(
                title = data["title"] ?: "New Notification",
                body = data["body"] ?: "",
                data = data
            )
        }
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed FCM token: $token")
        
        try {
            // Check if we have permission to send notifications and a valid userId
            val currentToken = SessionManager.getFcmToken()
            val userId = SessionManager.getUserId()
            
            // Only update the token if all of these are true:
            // 1. We already had a token (user already granted permission)
            // 2. The user is logged in (userId is not null)
            if (!currentToken.isNullOrEmpty()) {
                Log.d(TAG, "User previously granted notification permission, updating token")
                // Save the token locally
                SessionManager.saveFcmToken(token)
                // Send to backend
                sendTokenToServer(token, userId)
            } else {
                // Do not save the token locally if no permission or not logged in
                Log.d(TAG, "Token refresh ignored: User hasn't granted notification permission or isn't logged in")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error handling token refresh: ${e.message}", e)
        }
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

    private fun showNotification(title: String?, body: String?, data: Map<String, String>) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "default_channel"

        // Build intent for MainActivity with route and optional parameters
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("route", data["route"])
            putExtra("circleId", data["circle_id"])
            putExtra("postId", data["post_id"]) // you can pass anything you need
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Default Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title ?: "Notification")
            .setContentText(body ?: "")
            .setSmallIcon(R.drawable.chatter_box_logo_with_background)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}
