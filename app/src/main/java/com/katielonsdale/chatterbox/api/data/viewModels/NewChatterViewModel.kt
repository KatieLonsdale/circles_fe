package com.katielonsdale.chatterbox.api.data.viewModels

import androidx.lifecycle.ViewModel
import com.katielonsdale.chatterbox.api.data.states.NewChatterUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NewChatterViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(NewChatterUiState())
    val uiState: StateFlow<NewChatterUiState> = _uiState.asStateFlow()

    fun onEvent(event: MyEvent) {
        when (event) {
            is MyEvent.SetName -> {
                _uiState.update { currentState ->
                    currentState.copy(name = event.name)
                }
            }
            is MyEvent.SetDescription -> {
                _uiState.update { currentState ->
                    currentState.copy(description = event.description)
                }
            }
            is MyEvent.RemoveMember -> {
                _uiState.update { currentState ->
                    val currentMemberIds = _uiState.value.memberIds
                    currentMemberIds.remove(event.memberId)
                    _uiState.value
                }
            }
            is MyEvent.AddMember -> {
                _uiState.update { currentState ->
                    val currentMemberIds = _uiState.value.memberIds
                    currentMemberIds.add(event.memberId)
                    _uiState.value
                }
            }
        }
    }
}

sealed interface MyEvent {
    data class SetName(val name: String) : MyEvent
    data class SetDescription(val description: String) : MyEvent
    data class AddMember(val memberId: String) : MyEvent
    data class RemoveMember(val memberId: String) : MyEvent
}
