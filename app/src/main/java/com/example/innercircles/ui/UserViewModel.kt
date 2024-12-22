package com.example.innercircles.ui

import androidx.lifecycle.ViewModel
import com.example.innercircles.api.data.UserUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class UserViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(UserUiState())
    val uiState: StateFlow<UserUiState> = _uiState.asStateFlow()

    fun setAttributes(attributes: Map<String, String>) {
        _uiState.update { currentState ->
            currentState.copy(
                id = attributes["id"].toString(),
                email = attributes["email"].toString(),
                displayName = attributes["displayName"].toString(),
                notificationFrequency = attributes["notificationFrequency"].toString(),
                lastTouAcceptance = attributes["lastTouAcceptance"].toString(),
            )
        }
    }

}