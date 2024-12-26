package com.example.innercircles.api.data

import com.google.gson.annotations.SerializedName
import java.time.OffsetDateTime

data class UserRequest(
    @SerializedName("email") val id: String? = null,
    @SerializedName("password") val password: String? = null,
    @SerializedName("display_name") val displayName: String? = null,
    @SerializedName("notification_frequency") val notificationFrequency: String? = null,
    @SerializedName("last_tou_acceptance") val lastTouAcceptance: String? = null,
)
