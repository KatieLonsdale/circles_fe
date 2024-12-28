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

    fun setCaption(newCaption: String) {
        _uiState.update { currentState ->
            currentState.copy(
                caption = newCaption
            )
        }
    }

    fun getCaption(): String {
        return _uiState.value.caption
    }

    fun setContent(contentViewModel: ContentViewModel){
        _uiState.update { currentState ->
            currentState.copy(
                contents = contentViewModel
            )
        }
    }

    fun createPostRequestContent(): PostRequestContent {
        val contents = _uiState.value.contents
        val postRequestContent = PostRequestContent(
            image = contents?.uiState?.value?.image,
            video = contents?.uiState?.value?.video,
        )
        return postRequestContent
    }

    fun setCircleIds(newCircleIds: List<String>) {
        _uiState.update { currentState ->
            currentState.copy(
                circleIds = newCircleIds
            )
        }
    }

    fun getCircleIds(): List<String> {
        return _uiState.value.circleIds
    }

    fun createPostRequest(): PostRequest {
        val caption = getCaption()
        val content = createPostRequestContent()
        val postRequest = PostRequest(caption,content)
        return postRequest
    }

    fun resetNewPost() {
        _uiState.value = NewPostUiState()
    }

}