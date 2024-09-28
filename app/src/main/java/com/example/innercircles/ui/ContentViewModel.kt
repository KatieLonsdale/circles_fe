package com.example.innercircles.ui

import android.content.ContentResolver
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.innercircles.api.data.ContentUiState
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
