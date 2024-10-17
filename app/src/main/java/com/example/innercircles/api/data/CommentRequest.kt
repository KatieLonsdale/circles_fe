package com.example.innercircles.api.data

import com.google.gson.annotations.SerializedName

data class CommentRequest(
    @SerializedName("comment_text") val commentText: String,
    @SerializedName("parent_comment_id") val parentCommentId: String?,
)
