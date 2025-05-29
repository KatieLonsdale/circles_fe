package com.katielonsdale.chatterbox.api.data

import com.google.gson.annotations.SerializedName

data class NotificationResponse (
    @SerializedName("data") val data: Notification
)
