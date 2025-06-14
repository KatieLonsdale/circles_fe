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
import com.katielonsdale.chatterbox.SessionManager
import android.net.Uri
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import com.katielonsdale.chatterbox.MyFirebaseMessagingService

class NotificationsManager(
    private val activity: ComponentActivity
) {
    private val TAG = "Notifications Manager"
    private var requestsDenied = 0

    private val permissionLauncher =
        activity.registerForActivityResult(ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            val pendingUserId = SessionManager.getUserId()
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
                    if (SessionManager.getFcmToken() != null) {
                        service.clearToken(pendingUserId)
                        SessionManager.clearFcmToken()
                    }
                }
            }
        }

    fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permissionGranted = ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if (!permissionGranted) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
        // we want to catch if a user has enabled permission outside the app
        // because we won't have created an FCM token for them yet
        checkPushNotificationPermissions()
        SessionManager.setPushNotificationsPermissionChecked(true)
    }

    private fun createFcmToken(
        service: MyFirebaseMessagingService = MyFirebaseMessagingService(),
        pendingUserId: String = SessionManager.getUserId()
    ) {
        service.getFcmToken() { success ->
            if(success) {
                val token = SessionManager.getFcmToken()
                if (token != null) {
                    service.sendTokenToServer(token, pendingUserId)
                } else {
                    Log.e(TAG, "FCM token is null")
                }
            }
        }
    }

    private fun checkPushNotificationPermissions(){
        //for older versions
        val manager = NotificationManagerCompat.from(activity)
        var permissionGranted = manager.areNotificationsEnabled()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionGranted = ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        }
        if (permissionGranted) {
            val fcmToken = SessionManager.getFcmToken()
            Log.d(TAG, "Permission granted.")
            if (fcmToken == null) {
                Log.d(TAG, "permission granted, but no FCM token exists. Generating new FCM token.")
                createFcmToken()
            }
        }
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