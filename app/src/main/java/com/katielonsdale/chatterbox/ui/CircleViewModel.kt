package com.katielonsdale.chatterbox.ui

import androidx.lifecycle.ViewModel
import com.katielonsdale.chatterbox.api.data.Circle
import com.katielonsdale.chatterbox.api.data.CircleUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CircleViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CircleUiState())
    val uiState: StateFlow<CircleUiState> = _uiState.asStateFlow()

    fun setCurrentCircle(circle: Circle) {
        _uiState.update { currentState ->
            currentState.copy(
                id = circle.id,
                name = circle.attributes.name,
                description = circle.attributes.description,
                userId = circle.attributes.userId,
            )
        }
    }
}

