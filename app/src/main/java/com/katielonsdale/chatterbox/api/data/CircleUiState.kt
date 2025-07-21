package com.katielonsdale.chatterbox.api.data

data class CircleUiState (
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val userId: String = "",
    val posts: MutableList<Post> = mutableListOf<Post>(),
)