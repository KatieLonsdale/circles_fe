package com.katielonsdale.chatterbox.ui.notifications

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.katielonsdale.chatterbox.SampleData
import com.katielonsdale.chatterbox.SessionManager
import com.katielonsdale.chatterbox.api.data.Notification
import com.katielonsdale.chatterbox.api.data.NotificationAttributes
import com.katielonsdale.chatterbox.api.data.PostViewModel
import com.katielonsdale.chatterbox.api.data.UserUiState
import com.katielonsdale.chatterbox.api.data.viewModels.NotificationViewModel
import com.katielonsdale.chatterbox.theme.ChatterBoxTheme
import com.katielonsdale.chatterbox.ui.UserViewModel
import kotlinx.coroutines.launch


val TAG = "NotificationsScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    onRequestNotificationPermission: () -> Unit = {},
    userUiState: UserUiState,
    onNotificationEvent: (NotificationViewModel.MyEvent) -> Unit,
    onPostEvent: (PostViewModel.MyEvent) -> Unit,
    onUserEvent: (UserViewModel.MyEvent) -> Unit,
    onDone: (NotificationAttributes) -> Unit,
){
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        isLoading = false
        val permissionChecked = SessionManager.wasPushNotificationsPermissionChecked()
        if (!permissionChecked) {
            onRequestNotificationPermission()
        }
        onUserEvent(UserViewModel.MyEvent.UpdateMyNotifications)
    }

    val notifications = userUiState.myNotifications
    var isRefreshing = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val state = rememberPullToRefreshState()

    PullToRefreshBox(
        modifier = Modifier.fillMaxSize(),
        state = state,
        isRefreshing = isRefreshing.value,
        onRefresh = {
            scope.launch {
                isRefreshing.value = true
                onNotificationEvent(NotificationViewModel.MyEvent.UpdateNotifications(
                    isRefreshing = isRefreshing
                ))
            }
        },
        indicator = {
            Indicator(
                isRefreshing = isRefreshing.value,
                state = state,
                modifier = Modifier.align(
                    Alignment.TopCenter
                ),
                containerColor = MaterialTheme.colorScheme.background,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    ) {
        Column() {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                NotificationsFeed(
                    notifications,
                    onNotificationEvent = onNotificationEvent,
                    onPostEvent = onPostEvent,
                    onDone = onDone
                )
            }
        }
    }
}

@Composable
fun NotificationsFeed(
    notifications: MutableList<Notification>,
    onNotificationEvent: (NotificationViewModel.MyEvent) -> Unit,
    onPostEvent: (PostViewModel.MyEvent) -> Unit,
    onDone: (NotificationAttributes) -> Unit
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
                        onNotificationEvent = onNotificationEvent,
                        onPostEvent = onPostEvent,
                        onDone = onDone,
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
    onNotificationEvent: (NotificationViewModel.MyEvent) -> Unit,
    onPostEvent: (PostViewModel.MyEvent) -> Unit,
    onDone: (NotificationAttributes) -> Unit,

){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (notification.attributes.postId != null) {
                    onNotificationEvent(NotificationViewModel.MyEvent.ResetNotification)
                    onNotificationEvent(NotificationViewModel.MyEvent.SetCurrentNotification(notification.attributes))
                    onPostEvent(PostViewModel.MyEvent.GetPost(
                        postId = notification.attributes.postId,
                        circleId = notification.attributes.circleId ?: "",
                    ))
                    onNotificationEvent(NotificationViewModel.MyEvent.UpdateNotifications())
                    onDone(notification.attributes)
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
                .fillMaxWidth()
                .padding(
                    top = 1.dp,
                    bottom = 1.dp,
                ),
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
            notifications = exampleNotifications.toMutableStateList(),
            onNotificationEvent = {},
            onPostEvent = {},
            onDone = {},
        )
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun EmptyNotificationsFeedPreview(){
    val exampleNotifications = emptyList<Notification>()
    ChatterBoxTheme {
        NotificationsFeed(
            notifications = exampleNotifications.toMutableStateList(),
            onNotificationEvent = {},
            onPostEvent = {},
            onDone = {}
        )
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun NotificationCardPreview(){
    val exampleNotification = SampleData.returnSampleNotifications[0]
    ChatterBoxTheme {
        NotificationCard(
            notification = exampleNotification,
            onNotificationEvent = {},
            onPostEvent = {},
            onDone = {},
        )
    }
}

//todo: allow multiple notifications tokens for multiple devices under one user
//todo: refresh token on login