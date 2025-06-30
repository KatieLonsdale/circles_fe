package com.katielonsdale.chatterbox.api.data.states

import com.google.gson.annotations.SerializedName

data class NotificationUiState(
    val id: String = "",
    val message: String = "",
    val read: Boolean = false,
    val action: String = "",
    val createdAt: String = "",
    val updatedAt: String = "",
    val circleId: String? = null,
    val circleName: String? = null,
    val notifiableType: String = "",
    val notifiableId: String = "",
    val postId: String? = null,
)
