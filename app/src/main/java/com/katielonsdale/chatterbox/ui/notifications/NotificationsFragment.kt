package com.katielonsdale.chatterbox.ui.notifications

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.katielonsdale.chatterbox.SessionManager
import com.katielonsdale.chatterbox.databinding.FragmentNotificationsBinding
import com.katielonsdale.chatterbox.ui.Screen
import com.katielonsdale.chatterbox.utils.NotificationsManager

class NotificationsFragment : Fragment() {
    private val tag = "Notifications Fragment"

    val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        val pendingUserId = SessionManager.getUserId()
        if (isGranted) {
            Log.d(tag, "Notification permission granted, getting FCM token with userId: $pendingUserId")
            NotificationsManager.getFcmToken(pendingUserId)
        } else {
            Log.d(tag, "Notification permission denied")
            // If permission is denied, make sure we don't have any token saved
            SessionManager.saveFcmToken("")
            // If user ID is available, clear the token on the server as well
            pendingUserId.let { userId ->
                NotificationsManager.clearTokenOnServer(userId)
            }
        }
    }
}