package com.katielonsdale.chatterbox.ui

import androidx.lifecycle.ViewModel
import com.katielonsdale.chatterbox.api.data.NewPostUiState
import com.katielonsdale.chatterbox.api.data.PostRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.katielonsdale.chatterbox.api.data.PostRequestContent


class NewPostViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(NewPostUiState())
    val uiState: StateFlow<NewPostUiState> = _uiState.asStateFlow()

    fun onEvent(event: MyEvent) {
        when (event) {
            is MyEvent.ResetNewPost -> {
                _uiState.value = NewPostUiState()

            }

            is MyEvent.SetContent -> {
                _uiState.update { currentState ->
                    val contentViewModel = ContentViewModel()
                    contentViewModel.setImage(event.content)
                    currentState.copy(
                        contents = contentViewModel
                    )
                }
            }

            is MyEvent.SetCaption -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        caption = event.caption
                    )
                }
            }

            is MyEvent.AddChatter -> {
                _uiState.update { currentState ->
                    val currentChatterIds = _uiState.value.chatterIds
                    currentChatterIds.add(event.chatterId)
                    _uiState.value
                }
            }

            is MyEvent.RemoveChatter -> {
                _uiState.update { currentState ->
                    val currentChatterIds = _uiState.value.chatterIds
                    currentChatterIds.remove(event.chatterId)
                    _uiState.value
                }
            }
        }
    }
}

sealed interface MyEvent {
    object ResetNewPost : MyEvent
    data class SetContent(val content: ByteArray) : MyEvent
    data class SetCaption(val caption: String) : MyEvent
    data class AddChatter(val chatterId: String) : MyEvent
    data class RemoveChatter(val chatterId: String) : MyEvent
}