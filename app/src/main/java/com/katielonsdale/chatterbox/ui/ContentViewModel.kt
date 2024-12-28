package com.katielonsdale.chatterbox.ui

import androidx.lifecycle.ViewModel
import com.katielonsdale.chatterbox.api.data.ContentUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class ContentViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ContentUiState())
    val uiState: StateFlow<ContentUiState> = _uiState.asStateFlow()

    fun setImage(byteArray: ByteArray) {
        _uiState.update { currentState ->
            currentState.copy(
                image = byteArray
            )
        }

    }

    fun getImage() : ByteArray? {
        return _uiState.value.image
    }
}
