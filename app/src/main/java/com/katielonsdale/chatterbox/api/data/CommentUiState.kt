package com.katielonsdale.chatterbox.api.data

data class CommentUiState(
    val commentText: String = "",
    val authorDisplayName: String = "Unknown User",
    val parentCommentId: String? = null,
    val updatedAt: String = "",
)
