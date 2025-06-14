package com.katielonsdale.chatterbox.ui.notifications

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.katielonsdale.chatterbox.R
import com.katielonsdale.chatterbox.SampleData
import com.katielonsdale.chatterbox.SessionManager
import com.katielonsdale.chatterbox.api.RetrofitClient
import com.katielonsdale.chatterbox.api.data.Notification
import com.katielonsdale.chatterbox.api.data.NotificationAttributes
import com.katielonsdale.chatterbox.api.data.NotificationsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


val TAG = "NotificationsScreen"

@Composable
fun NotificationsScreen(
    onClickPostNotification: (NotificationAttributes) -> Unit = {},
    onRequestNotificationPermission: () -> Unit = {}
){
    val userNotifications = remember { mutableStateListOf<Notification>() }
    var isLoading by remember { mutableStateOf(true) }
    val userId = SessionManager.getUserId()

    LaunchedEffect(Unit) {
        getNotifications(userId, userNotifications)  // Await the result of getPosts
        isLoading = false    // Set loading to false after fetching the posts
        val permissionChecked = SessionManager.wasPushNotificationsPermissionChecked()
        if (!permissionChecked) {
            onRequestNotificationPermission()
        }
    }

    Column() {
        if (isLoading) {
            // Show loading indicator while posts are being fetched
            CircularProgressIndicator()
        } else {
            NotificationsFeed(
                userNotifications,
                onClickPostNotification
            )
        }
    }
}

@Composable
fun NotificationsFeed(
    notifications: List<Notification>,
    onClickPostNotification: (NotificationAttributes) -> Unit = {}
){
    if (notifications.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "No notifications.",
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(16.dp)
            )
        }
    } else {
        val sortedNotifications = notifications.sortedByDescending { it.attributes.createdAt }
        LazyColumn {
            items(sortedNotifications) { notification ->
                NotificationCard(
                    notification,
                    onClickPostNotification
                )
                Spacer(modifier = Modifier.padding(
                    top = 1.dp,
                    bottom = 1.dp,
                    start = 2.dp,
                    end = 2.dp
                )
                )
            }
        }
    }
}

@Composable
fun NotificationCard(
    notification: Notification,
    onClickPostNotification: (NotificationAttributes) -> Unit = {}
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (notification.attributes.postId != null) {
                    onClickPostNotification(notification.attributes)
                } else {
                    Log.e(TAG, "Notification type not supported: ${notification.attributes.notifiableType}")
                }
            }
            .background(Color.LightGray)
            .border(
                width = 1.dp,
                color = Color.DarkGray,
                shape = RoundedCornerShape(4.dp)
            )
    ){
        if (!notification.attributes.read) {
            Icon(
                painter = painterResource(id = R.drawable.unread_notification),
                contentDescription = "Unread",
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 4.dp)
            )

            Text(
                text = notification.attributes.message,
                modifier = Modifier.padding(
                    start = 4.dp,
                    end = 7.dp,
                    top = 10.dp,
                    bottom = 10.dp
                ),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        } else {
            Text(
                text = notification.attributes.message,
                modifier = Modifier.padding(
                    start = 7.dp,
                    end = 7.dp,
                    top = 10.dp,
                    bottom = 10.dp
                ),
                fontSize = 15.sp
            )
        }
    }
}

private fun getNotifications(
    userId: String,
    userNotifications: MutableList<Notification>
){
    RetrofitClient.apiService.getUserNotifications(userId).enqueue(object :
        Callback<NotificationsResponse> {
        override fun onResponse(call: Call<NotificationsResponse>, response: Response<NotificationsResponse>) {
            if (response.isSuccessful) {
                val notifications = response.body()?.data ?: emptyList()
                if (notifications.isNotEmpty()) { userNotifications.addAll(notifications) }
            } else {
                Log.e(TAG, "Failed to fetch notifications: ${response.errorBody()?.string()}")
            }
        }

        override fun onFailure(call: Call<NotificationsResponse>, t: Throwable) {
            Log.e(TAG, "Error fetching notifications", t)
        }
    })
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun NotificationsScreenPreview(){
    NotificationsScreen ()
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun NotificationsFeedPreview(){
    val exampleNotifications = SampleData.returnSampleNotifications
    NotificationsFeed(
        notifications = exampleNotifications
    )
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun EmptyNotificationsFeedPreview(){
    val exampleNotifications = emptyList<Notification>()
    NotificationsFeed(
        notifications = exampleNotifications
    )
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun NotificationCardPreview(){
    val exampleNotification = SampleData.returnSampleNotifications[0]
    NotificationCard(
        notification = exampleNotification
    )
}

//todo: allow multiple notifications tokens for multiple devices under one user
//todo: refresh token on login