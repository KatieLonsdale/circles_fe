package com.katielonsdale.chatterbox.ui

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import com.katielonsdale.chatterbox.api.RetrofitClient.apiService
import com.katielonsdale.chatterbox.api.data.Circle
import com.katielonsdale.chatterbox.api.data.CircleUiState
import com.katielonsdale.chatterbox.api.data.NotificationAttributes
import com.katielonsdale.chatterbox.api.data.Post
import com.katielonsdale.chatterbox.api.data.PostsResponse
import com.katielonsdale.chatterbox.api.data.states.NotificationUiState
import com.katielonsdale.chatterbox.api.data.viewModels.NotificationViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CircleViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CircleUiState())
    val uiState: StateFlow<CircleUiState> = _uiState.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    fun onEvent(event: MyEvent) {
        when (event) {
            is MyEvent.GetChatterPosts-> {
                getChatterPosts(
                    chatterId = event.chatterId,
                    userId = event.userId
                )
            }
            is MyEvent.UpdateChatterPosts-> {
                getChatterPosts(
                    chatterId = event.chatterId,
                    userId = event.userId,
                    isRefreshing = event.isRefreshing
                )
            }
        }
    }

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

    fun setChatterPosts(posts: List<Post>) {
        _uiState.update { currentState ->
            currentState.copy(
                posts = posts.toMutableList()
            )
        }
    }

    fun getChatterPosts(
        chatterId: String,
        userId: String?,
        isRefreshing: MutableState<Boolean>? = null
    ) {
        apiService.getPostsForChatter(userId, chatterId).enqueue(object : Callback<PostsResponse> {
            override fun onResponse(call: Call<PostsResponse>, response: Response<PostsResponse>) {
                if (response.isSuccessful) {
                    val chatterPosts = response.body()?.data ?: emptyList()
                    setChatterPosts(chatterPosts)
                } else {
                    Log.e("ChatterViewModel", "Failed to fetch chatter ${chatterId} posts: ${response.errorBody()?.string()}")
                }
                isRefreshing?.value = false
            }

            override fun onFailure(call: Call<PostsResponse>, t: Throwable) {
                Log.e("ChatterViewModel", "Error fetching posts", t)
                isRefreshing?.value = false
            }
        })
    }

    sealed interface MyEvent {
        data class UpdateChatterPosts(
            val chatterId: String,
            val userId: String? = null,
            val isRefreshing: MutableState<Boolean>? = null
        ) : MyEvent
        data class GetChatterPosts(
            val chatterId: String,
            val userId: String? = null,
        ) : MyEvent
    }
}

