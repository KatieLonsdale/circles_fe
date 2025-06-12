package com.katielonsdale.chatterbox.utils

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.katielonsdale.chatterbox.SessionManager
import android.net.Uri
import com.katielonsdale.chatterbox.MyFirebaseMessagingService

class NotificationsManager(
    private val activity: ComponentActivity
) {
    val TAG = "Notifications Manager"

    private var retryRequested = false

    private val permissionLauncher =
        activity.registerForActivityResult(ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            val pendingUserId = SessionManager.getUserId()
            if (isGranted) {
                Log.d(TAG, "Notification permission granted, getting FCM token with userId: $pendingUserId")
                getFcmToken(pendingUserId)
                val token = SessionManager.getFcmToken()
                if (token != null) {
                    val service = MyFirebaseMessagingService()
                    service.sendTokenToServer(token, pendingUserId)
                } else {
                    Log.e(TAG, "FCM token is null")
                }
            } else {
                if (!retryRequested) {
                    showRetryDialog()
                } else {
                    showSettingsDialog()
                }
            }
        }

    fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    fun getFcmToken(userId: String) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e(com.katielonsdale.chatterbox.ui.notifications.TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            val token = task.result
            SessionManager.saveFcmToken(token)
        })
    }

    private fun showRetryDialog() {
        AlertDialog.Builder(activity)
            .setTitle("Are you sure?")
            .setMessage("You won't be notified when your friends post or comment if push notifications are not enabled.")
            .setPositiveButton("OK") { _, _ ->
                retryRequested = true
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                // ignore this warning, this is a part of a flow that has excluded
                // users on versions lower than 33
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showSettingsDialog() {
        AlertDialog.Builder(activity)
            .setTitle("Notifications Disabled")
            .setMessage("You’ve disabled push notifications. You can re-enable them in Settings.")
            .setPositiveButton("Open Settings") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", activity.packageName, null)
                }
                activity.startActivity(intent)
            }
            .setNegativeButton("Dismiss", null)
            .show()
    }
}