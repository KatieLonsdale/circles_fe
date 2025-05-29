package com.katielonsdale.chatterbox.api.data

import com.google.gson.annotations.SerializedName

data class NotificationRequest (
    @SerializedName("notification") val notification: NotificationRequestAttributes
)

data class NotificationRequestAttributes(
    @SerializedName("read") val read: Boolean?,
)
