package com.katielonsdale.chatterbox.api.data

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CommentViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CommentUiState())
    val uiState: StateFlow<CommentUiState> = _uiState.asStateFlow()

    fun setCommentText(text: String) {
        _uiState.value = _uiState.value.copy(
            commentText = text
        )
    }

    fun setParentCommentId(id: String) {
        _uiState.value = _uiState.value.copy(
            parentCommentId = id
        )
    }

    fun resetComment(){
        _uiState.value = CommentUiState()
    }
}