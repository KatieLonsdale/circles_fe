package com.example.innercircles.api.data

import com.google.gson.annotations.SerializedName

data class AuthenticatedUser(
    @SerializedName("id") val id: String,
    @SerializedName("email") val email: String,
    @SerializedName("display_name") val displayName: String,
    @SerializedName("notification_frequency") val notificationFrequency: String,
    @SerializedName("last_tou_acceptance") val lastTouAcceptance: String,
)