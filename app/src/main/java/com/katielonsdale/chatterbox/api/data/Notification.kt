package com.katielonsdale.chatterbox.api.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Notification(
    @SerializedName("id") val id: String,
    @SerializedName("type") val type: String,
    @SerializedName("attributes") val attributes: NotificationAttributes
)

data class NotificationAttributes(
    @SerializedName("id") val id: String,
    @SerializedName("message") val message: String,
    @SerializedName("read") val read: Boolean,
    @SerializedName("action") val action: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("circle_id") val circleId: String?,
    @SerializedName("circle_name") val circleName: String?,
    @SerializedName("notifiable_type") val notifiableType: String,
    @SerializedName("notifiable_id") val notifiableId: String,
)
