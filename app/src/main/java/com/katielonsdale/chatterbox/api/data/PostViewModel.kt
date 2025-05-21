package com.katielonsdale.chatterbox.api.data

import android.util.Log
import androidx.lifecycle.ViewModel
import com.katielonsdale.chatterbox.SessionManager
import com.katielonsdale.chatterbox.api.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(PostUiState())
    val uiState: StateFlow<PostUiState> = _uiState.asStateFlow()

    fun setCurrentPost(post: Post) {
        val postAttributes = post.attributes
        val postUiState = PostUiState(
            post.id,
            postAttributes.authorDisplayName,
            postAttributes.caption,
            postAttributes.contents.data,
            postAttributes.comments.data,
            postAttributes.createdAt,
            postAttributes.updatedAt,
            postAttributes.circleId,
        )
        _uiState.value = postUiState
    }

    fun resetPost() {
        _uiState.value = PostUiState()
    }

    fun addComment(comment: Comment) {
        if (comment.attributes.parentCommentId == null) {
            // Add top-level comment
            val currentComments = _uiState.value.comments
            val newComments = currentComments + comment
            _uiState.update { currentState ->
                currentState.copy(
                    comments = newComments
                )
            }
        } else {
            // For replies, fetch the updated post from the backend
            val userId = SessionManager.getUserId()
            val circleId = _uiState.value.circleId
            val postId = _uiState.value.id
            RetrofitClient.apiService.getPost(userId, circleId, postId).enqueue(object : Callback<PostResponse> {
                override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                    if (response.isSuccessful) {
                        response.body()?.data?.let { updatedPost ->
                            setCurrentPost(updatedPost)
                        }
                        Log.e(
                            "PostViewModel.addComment()",
                            "Post updated successfully"
                        )
                    } else {
                        Log.e(
                            "PostViewModel.addComment()",
                            "Failed to update post"
                        )
                    }
                }
                override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                    Log.e("PostViewModel.addComment()", "Error updating post", t)
                }
            })
        }
    }
}