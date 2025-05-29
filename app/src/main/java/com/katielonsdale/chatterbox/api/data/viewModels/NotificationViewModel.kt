package com.katielonsdale.chatterbox.api.data.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.katielonsdale.chatterbox.SessionManager
import com.katielonsdale.chatterbox.api.RetrofitClient
import com.katielonsdale.chatterbox.api.data.NotificationAttributes
import com.katielonsdale.chatterbox.api.data.NotificationRequest
import com.katielonsdale.chatterbox.api.data.NotificationRequestAttributes
import com.katielonsdale.chatterbox.api.data.NotificationResponse
import com.katielonsdale.chatterbox.api.data.States.NotificationUiState
import com.katielonsdale.chatterbox.ui.InnerCirclesScreen
import com.katielonsdale.chatterbox.ui.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationViewModel : ViewModel() {
    val TAG = "NotificationViewModel"

    private val _uiState = MutableStateFlow(NotificationUiState())
    val uiState: StateFlow<NotificationUiState> = _uiState.asStateFlow()

    fun setCurrentNotification(notification: NotificationAttributes) {
        val notificationUiState = NotificationUiState(
            notification.id,
            notification.message,
            notification.read,
            notification.action,
            notification.createdAt,
            notification.updatedAt,
            notification.circleId,
            notification.circleName,
            notification.notifiableType,
            notification.notifiableId,
            notification.postId
        )
        _uiState.value = notificationUiState

        // we assume that if the notification is being set as the current, it has been read
        if (!notification.read) {
            markNotificationAsRead(notification.id)
        }
    }

    fun getNavigationScreen(
        notification: NotificationAttributes,
    ): String {
        if (notification.postId != null) {
            return InnerCirclesScreen.DisplayPost.name
        } else {
            //todo: add friendship type of notification
            return Screen.Notifications.route
        }
    }

    private fun markNotificationAsRead(notificationId: String) {
        updateNotification(notificationId)
        _uiState.value = _uiState.value.copy(read = true)
    }

    private fun updateNotification(
        notificationId: String,
    ) {
        val userId = SessionManager.getUserId()
        val notificationRequest = NotificationRequest(
            NotificationRequestAttributes(read = true),
        )

        RetrofitClient.apiService.updateNotification(userId, notificationId, notificationRequest)
            .enqueue(object : Callback<NotificationResponse> {
                override fun onResponse(
                    call: Call<NotificationResponse>,
                    response: Response<NotificationResponse>
                ) {
                    if (response.isSuccessful) {
                        Log.e(TAG, "Notification marked as read")
                    } else {
                        Log.e(
                            TAG,
                            "Failed to update notification: ${response.errorBody()?.string()}"
                        )
                    }
                }

                override fun onFailure(call: Call<NotificationResponse>, t: Throwable) {
                    Log.e(TAG, "Error updating notification", t)
                }
            })
    }

    fun resetNotification() {
        _uiState.value = NotificationUiState()
    }
}