package com.katielonsdale.chatterbox.api.data

data class UserUiState (
        val id: String = "",
        val email: String = "",
        val displayName: String = "",
        val notificationFrequency: String = "",
        val lastTouAcceptance: String = "",
)
