package com.example.innercircles.api.data

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id") val id: String,
    @SerializedName("email") val email: String,
    @SerializedName("display_name") val displayName: String,
    @SerializedName("notification_frequency") val notificationFrequency: String
)