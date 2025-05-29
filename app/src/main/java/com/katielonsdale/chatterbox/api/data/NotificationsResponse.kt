package com.katielonsdale.chatterbox.api.data

import com.google.gson.annotations.SerializedName

data class NotificationsResponse(
    @SerializedName("data") val data: List<Notification>
)