package com.katielonsdale.chatterbox.utils

import android.util.Log
import com.katielonsdale.chatterbox.SessionManager
import com.katielonsdale.chatterbox.api.RetrofitClient
import com.katielonsdale.chatterbox.api.data.Comment
import com.katielonsdale.chatterbox.api.data.CommentAttributes
import com.katielonsdale.chatterbox.api.data.CommentRequest
import com.katielonsdale.chatterbox.api.data.CommentResponse
import com.katielonsdale.chatterbox.api.data.CommentUiState
import com.katielonsdale.chatterbox.api.data.PostUiState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object CommentCreator {
    fun createComment(
        comment: CommentUiState,
        postId: String,
        circleId: String,
        addCommentToPost: (Comment) -> Unit,
    ){
        val userId = SessionManager.getUserId()
        val commentRequest = createCommentRequest(comment)

        RetrofitClient.apiService.createComment(userId, circleId, postId, commentRequest).enqueue(object :
            Callback<CommentResponse> {
            override fun onResponse(
                call: Call<CommentResponse>,
                response: Response<CommentResponse>
            ) {
                if (response.isSuccessful) {
                    val commentResponse = response.body()?.data
                    val commentResponseAttributes = commentResponse?.attributes
                    val commentAttributes = CommentAttributes(
                        commentResponse?.id ?: "",
                        commentResponseAttributes?.authorId ?: "",
                        commentResponseAttributes?.parentCommentId ?: "",
                        commentResponseAttributes?.postId ?: "",
                        commentResponseAttributes?.commentText ?: "",
                        commentResponseAttributes?.createdAt ?: "",
                        commentResponseAttributes?.updatedAt ?: "",
                        commentResponseAttributes?.authorDisplayName ?: "",
                        commentResponseAttributes?.replies,
                    )
                    val newComment = Comment(
                        id = commentResponse?.id ?: "",
                        attributes = commentAttributes
                    )
                    addCommentToPost(newComment)
                    Log.e(
                        "DisplayPostScreen",
                        "Comment created successfully: ${response.body()}"
                    )
                } else {
                    Log.e(
                        "DisplayPostScreen",
                        "Failed to create comment: ${response.body()} status: ${response.code()}"
                    )
                }
            }

            override fun onFailure(call: Call<CommentResponse>, t: Throwable) {
                Log.e("SelectCirclesScreen", "Error creating comment", t)
            }
        })
    }

    private fun createCommentRequest(comment: CommentUiState): CommentRequest {
        val commentText = comment.commentText
        val parentCommentId = comment.parentCommentId
        val commentRequest = CommentRequest(
            commentText,
            parentCommentId
        )
        return commentRequest
    }
}