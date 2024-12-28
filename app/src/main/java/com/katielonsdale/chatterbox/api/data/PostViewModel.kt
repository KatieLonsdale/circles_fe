package com.katielonsdale.chatterbox.api.data

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

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
        val currentComments = _uiState.value.comments
        val newComments = currentComments + comment
        _uiState.update { currentState ->
            currentState.copy(
                comments = newComments
            )
        }
    }
}