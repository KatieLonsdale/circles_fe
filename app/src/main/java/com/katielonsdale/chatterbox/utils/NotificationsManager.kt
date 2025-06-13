package com.katielonsdale.chatterbox.utils

import android.Manifest
import android.app.Activity
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
import androidx.core.app.ActivityCompat
import com.katielonsdale.chatterbox.MyFirebaseMessagingService

class NotificationsManager(
    private val activity: ComponentActivity
) {
    private val TAG = "Notifications Manager"
    private var requestsDenied = 0

    private val permissionLauncher =
        activity.registerForActivityResult(ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            val sessionManager = SessionManager
            val pendingUserId = sessionManager.getUserId()
            val service = MyFirebaseMessagingService()

            if (isGranted) {
                Log.d(TAG, "Notification permission granted, getting FCM token with userId: $pendingUserId")
                createFcmToken(
                    service = service,
                    pendingUserId = pendingUserId
                )
            } else {
                requestsDenied ++
                // Check if we should show retries
                val requestsAllowed = activity.let {
                    ActivityCompat.shouldShowRequestPermissionRationale(
                        it,
                        Manifest.permission.POST_NOTIFICATIONS
                    )
                }
                if (requestsAllowed && requestsDenied < 2) {
                    showRetryDialog()
                } else if (requestsAllowed || requestsDenied == 2) {
                    showSettingsDialog()
                    //todo: clear token
                }
                if (sessionManager.getFcmToken() != null) {
                    service.clearToken(pendingUserId)
                    sessionManager.clearFcmToken()
                }
            }
        }

    fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permissionGranted = ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
            if (!permissionGranted) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    fun createFcmToken(
        service: MyFirebaseMessagingService = MyFirebaseMessagingService(),
        pendingUserId: String = SessionManager.getUserId()
    ) {
        getFcmToken()
        val token = SessionManager.getFcmToken()
        if (token != null) {
            service.sendTokenToServer(token, pendingUserId)
        } else {
            Log.e(TAG, "FCM token is null")
        }
    }

    private fun getFcmToken() {
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