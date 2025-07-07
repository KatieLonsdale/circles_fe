package com.katielonsdale.chatterbox.api.data

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.katielonsdale.chatterbox.SessionManager
import com.katielonsdale.chatterbox.api.RetrofitClient
import com.katielonsdale.chatterbox.ui.MyEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostViewModel : ViewModel() {
    val TAG = "PostViewModel"

    private val _uiState = MutableStateFlow(PostUiState())
    val uiState: StateFlow<PostUiState> = _uiState.asStateFlow()
    private val _errorChannel = Channel<String>(Channel.BUFFERED)
    val errorFlow = _errorChannel.receiveAsFlow()

    fun onEvent(event: MyEvent) {
        when (event) {
            is MyEvent.GetPost -> {
                getPost(event.postId, event.circleId)
            }
        }
    }

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
                        viewModelScope.launch {
                            _errorChannel.send("Post failed to load.")
                        }
                    }
                }
                override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                    Log.e("PostViewModel.addComment()", "Error updating post", t)
                    viewModelScope.launch {
                        _errorChannel.send("Could not connect. Please try again.")
                    }
                }
            })
        }
    }

    fun getPost(
        postId: String,
        circleId: String,
    ) {
        resetPost()
        val userId = SessionManager.getUserId()
        RetrofitClient.apiService.getPost(userId, circleId, postId).enqueue(object : Callback<PostResponse> {
            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                if (response.isSuccessful) {
                    val post = response.body()?.data
                    if (post != null) {
                        // we assume if we are getting a singular post, it is so it can be the current post
                        setCurrentPost(post)
                    } else {
                        Log.e(TAG, "Post is null")
                    }
                } else {
                    Log.e(TAG, "Failed to fetch post: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                Log.e(TAG, "Error fetching post", t)
            }
        })
    }

    sealed interface MyEvent {
        data class GetPost(
            val postId: String,
            val circleId: String,
        ) : MyEvent
    }
}