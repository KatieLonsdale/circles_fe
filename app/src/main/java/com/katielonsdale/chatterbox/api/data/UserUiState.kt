package com.katielonsdale.chatterbox.api.data

data class UserUiState (
        val id: String = "",
        val email: String = "",
        val displayName: String = "",
        val notificationFrequency: String = "",
        val lastTouAcceptance: String? = null,
        val notificationsToken: String? = null,
        val myChatters: List<Circle> = emptyList<Circle>(),
        val myFriends: List<Friend> = emptyList<Friend>()
)
