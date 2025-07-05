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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextOverflow
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
import com.katielonsdale.chatterbox.theme.ChatterBoxTheme
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
        getNotifications(userId, userNotifications)
        isLoading = false
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
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (notifications.isEmpty()) {
            Text(
                text = "No notifications.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
            )
        } else {
            val sortedNotifications = notifications.sortedByDescending { it.attributes.createdAt }
            LazyColumn {
                items(sortedNotifications) { notification ->
                    NotificationCard(
                        notification,
                        onClickPostNotification
                    )
                    Spacer(
                        modifier = Modifier.padding(
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
            .background(MaterialTheme.colorScheme.background),
        verticalAlignment = Alignment.CenterVertically

    ){
        if (!notification.attributes.read) {
            NewNotificationIcon()
            Spacer(Modifier.width(5.dp))
            Text(
                text = notification.attributes.message,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = Bold
                ),
                color = MaterialTheme.colorScheme.primary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(
                        top = 5.dp,
                        bottom = 5.dp,
                        start = 2.dp,
                        end = 2.dp,
                    )
            )
        } else {
            Text(
                text = notification.attributes.message,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(
                        top = 5.dp,
                        bottom = 5.dp,
                        start = 2.dp,
                        end = 2.dp,
                    )
            )
        }
    }
    Row(
        modifier = Modifier.padding(
            top = 10.dp,
            bottom = 10.dp
        )
            .fillMaxWidth()
    ){
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth(),
            thickness = 0.5.dp,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5F)
        )
    }
}

@Composable
fun NewNotificationIcon(){
    val canvasColor = MaterialTheme.colorScheme.secondary
    Text(
        text = "NEW",
        style = MaterialTheme.typography.titleSmall.copy(
            fontWeight = Bold,
            fontSize = 10.sp
        ),
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier
            .drawBehind {
                drawRoundRect(
                    color = canvasColor,
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(6.dp.toPx())
                )
            }
            .padding(
                start = 3.dp,
                end = 3.dp,
                top = 5.dp,
                bottom = 5.dp,
            )
    )
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

//@Preview(apiLevel = 34, showBackground = true)
//@Composable
//fun NotificationsScreenPreview(){
//    ChatterBoxTheme {
//        NotificationsScreen()
//    }
//}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun NotificationsFeedPreview(){
    val exampleNotifications = SampleData.returnSampleNotifications
    ChatterBoxTheme {
        NotificationsFeed(
            notifications = exampleNotifications
        )
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun EmptyNotificationsFeedPreview(){
    val exampleNotifications = emptyList<Notification>()
    ChatterBoxTheme {
        NotificationsFeed(
            notifications = exampleNotifications
        )
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun NotificationCardPreview(){
    val exampleNotification = SampleData.returnSampleNotifications[0]
    ChatterBoxTheme {
        NotificationCard(
            notification = exampleNotification
        )
    }
}

//todo: allow multiple notifications tokens for multiple devices under one user
//todo: refresh token on login