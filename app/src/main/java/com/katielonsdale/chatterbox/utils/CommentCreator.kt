package com.katielonsdale.chatterbox.utils

import android.util.Log
import com.katielonsdale.chatterbox.SessionManager
import com.katielonsdale.chatterbox.api.RetrofitClient
import com.katielonsdale.chatterbox.api.data.Comment
import com.katielonsdale.chatterbox.api.data.CommentAttributes
import com.katielonsdale.chatterbox.api.data.CommentRequest
import com.katielonsdale.chatterbox.api.data.CommentResponse
import com.katielonsdale.chatterbox.api.data.CommentUiState
import com.katielonsdale.chatterbox.api.data.Post
import com.katielonsdale.chatterbox.api.data.PostAttributes
import com.katielonsdale.chatterbox.api.data.PostResponse
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
                    if (commentResponseAttributes != null) {
                        val commentAttributes = CommentAttributes(
                            commentResponse.id,
                            commentResponseAttributes.authorId,
                            commentResponseAttributes.parentCommentId ?: "",
                            commentResponseAttributes.postId,
                            commentResponseAttributes.commentText,
                            commentResponseAttributes.createdAt,
                            commentResponseAttributes.updatedAt,
                            commentResponseAttributes.authorDisplayName,
                            commentResponseAttributes.replies,
                        )
                        if (commentResponseAttributes.parentCommentId != null) {
                            getPost(
                                postId,
                                circleId,
                                userId.toString(),
                            )
                        }
                        val newComment = Comment(
                            id = commentResponse.id,
                            attributes = commentAttributes
                        )
                        addCommentToPost(newComment)
                        Log.e(
                            "CommentCreator",
                            "Comment created successfully"
                        )
                    }
                } else {
                    Log.e(
                        "CommentCreator",
                        "Failed to create comment: ${response.body()} status: ${response.code()}"
                    )
                }
            }

            override fun onFailure(call: Call<CommentResponse>, t: Throwable) {
                Log.e("CommentCreator", "Error creating comment", t)
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

    fun getPost(
        postId: String,
        circleId: String,
        userId: String,
    ){
        RetrofitClient.apiService.getPost(userId, circleId, postId).enqueue(object: Callback<PostResponse>{
            override fun onResponse(
                call: Call<PostResponse>,
                response: Response<PostResponse>
            ) {
                if (response.isSuccessful) {
                    val postResponse = response.body()?.data
                    val postResponseAttributes = postResponse?.attributes
                    if (postResponseAttributes != null) {
                        val postAttributes = PostAttributes(
                            postResponse.id.toInt(),
                            postResponseAttributes.authorId,
                            postResponseAttributes.caption,
                            postResponseAttributes.createdAt,
                            postResponseAttributes.updatedAt,
                            postResponseAttributes.circleId,
                            postResponseAttributes.authorDisplayName,
                            postResponseAttributes.circleName,
                            postResponseAttributes.contents,
                            postResponseAttributes.comments
                        )
                        val newPost = Post(
                            id = postResponse.id,
                            type = postResponse.type,
                            attributes = postAttributes
                        )

                        Log.e(
                            "CommentCreator",
                            "Post updated successfully"
                        )
                    }
                } else {
                    Log.e(
                        "CommentCreator",
                        "Failed to update post: ${response.body()} status: ${response.code()}"
                    )
                }
            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                Log.e("CommentCreator", "Error creating comment", t)
            }
        })
    }
}