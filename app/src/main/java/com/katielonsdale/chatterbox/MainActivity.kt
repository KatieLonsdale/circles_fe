package com.katielonsdale.chatterbox

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
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

        //Initialize Notifications Manager
        NotificationsManager.init(this)

            setContent {
                MaterialTheme {
                    MainScreen(
                        route = intent?.getStringExtra("route"),
                        circleId = intent?.getStringExtra("circleId"),
                        postId = intent?.getStringExtra("postId"),
                    )
                }
            }
    }
}