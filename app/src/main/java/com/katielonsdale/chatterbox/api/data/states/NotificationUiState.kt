package com.katielonsdale.chatterbox.api.data.states

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.google.gson.annotations.SerializedName

data class NotificationUiState(
    val id: String = "",
    val message: String = "",
    val read: MutableState<Boolean> = mutableStateOf(false),
    val action: String = "",
    val createdAt: String = "",
    val updatedAt: String = "",
    val circleId: String? = null,
    val circleName: String? = null,
    val notifiableType: String = "",
    val notifiableId: String = "",
    val postId: String? = null,
)
