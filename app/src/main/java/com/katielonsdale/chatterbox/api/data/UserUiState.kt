package com.katielonsdale.chatterbox.api.data

data class UserUiState (
        val id: String = "",
        val email: String = "",
        val displayName: String = "",
        val notificationFrequency: String = "",
        val lastTouAcceptance: String? = null,
        val notificationsToken: String? = null,
        val myChatters: MutableList<Circle> = mutableListOf<Circle>(),
        val myFriends: MutableList<Friend> = mutableListOf<Friend>(),
        val myNotifications: MutableList<Notification> = mutableListOf<Notification>(),
)
