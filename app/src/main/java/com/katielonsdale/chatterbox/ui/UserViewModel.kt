package com.katielonsdale.chatterbox.ui

import android.provider.Settings.Global.getString
import androidx.lifecycle.ViewModel
import com.katielonsdale.chatterbox.api.data.AuthenticatedUser
import com.katielonsdale.chatterbox.api.data.UserUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class UserViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(UserUiState())
    val uiState: StateFlow<UserUiState> = _uiState.asStateFlow()

    fun setCurrentUser(user: AuthenticatedUser) {
        _uiState.update { currentState ->
            currentState.copy(
                id = user.id,
                email = user.attributes.email,
                displayName = user.attributes.displayName,
                notificationFrequency = user.attributes.notificationFrequency,
                lastTouAcceptance = user.attributes.lastTouAcceptance,
                notificationsToken = user.attributes.notificationsToken,
            )
        }
    }
}