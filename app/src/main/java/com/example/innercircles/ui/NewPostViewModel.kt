package com.example.innercircles.ui

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import com.example.innercircles.api.data.NewPostUiState
import com.example.innercircles.api.data.PostRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.innercircles.api.data.Content
import com.example.innercircles.api.data.PostRequestContent
import com.example.innercircles.ui.ContentViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


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

    fun setCircleIds(newCircleIds: Array<String>) {
        _uiState.update { currentState ->
            currentState.copy(
                circleIds = newCircleIds
            )
        }
    }

    fun getCircleIds(): Array<String> {
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

@Composable
private fun convertUriToByteArray(uri: Uri): ByteArray? {
    val context = LocalContext.current
    val contentResolver = context.contentResolver
    val scope = rememberCoroutineScope()
    var byteArray by remember { mutableStateOf<ByteArray?>(null) }

    scope.launch {
        byteArray = withContext(Dispatchers.IO) {
            // Convert URI to ByteArray in IO thread
            val inputStream = contentResolver.openInputStream(uri)
            inputStream?.readBytes()
        }
    }
    return byteArray
}
