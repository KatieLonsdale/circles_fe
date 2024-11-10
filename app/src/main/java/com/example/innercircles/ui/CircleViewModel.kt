package com.example.innercircles.ui

import androidx.lifecycle.ViewModel
import com.example.innercircles.api.data.Circle
import com.example.innercircles.api.data.CircleUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CircleViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CircleUiState())
    open val uiState: StateFlow<CircleUiState> = _uiState.asStateFlow()

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