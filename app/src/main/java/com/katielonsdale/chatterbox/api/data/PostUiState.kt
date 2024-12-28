package com.katielonsdale.chatterbox.api.data

data class PostUiState(
    val id: String = "",
    val authorDisplayName: String = "",
    val caption: String = "",
    val contents: List<Content> = emptyList(),
    var comments: List<Comment> = emptyList(),
    val createdAt: String = "",
    val updatedAt: String = "",
    val circleId: String = "",
)
