package com.katielonsdale.chatterbox.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.katielonsdale.chatterbox.SessionManager
import com.katielonsdale.chatterbox.api.ApiClient
import com.katielonsdale.chatterbox.api.data.CircleMemberRequest
import com.katielonsdale.chatterbox.api.data.CircleMemberResponse
import com.katielonsdale.chatterbox.api.data.UserData
import com.katielonsdale.chatterbox.api.data.UsersResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddFriendViewModel : ViewModel() {
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    private val _searchResults = MutableStateFlow<List<UserData>>(emptyList())
    val searchResults: StateFlow<List<UserData>> = _searchResults.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage.asStateFlow()
    
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
    
    fun searchUsers() {
        if (_searchQuery.value.isBlank()) {
            _errorMessage.value = "Please enter a search term"
            return
        }
        
        _isLoading.value = true
        _errorMessage.value = null
        
        ApiClient.apiService.searchUsers(_searchQuery.value).enqueue(object : Callback<UsersResponse> {
            override fun onResponse(call: Call<UsersResponse>, response: Response<UsersResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    response.body()?.let { usersResponse ->
                        _searchResults.value = usersResponse.data
                    }
                } else {
                    _errorMessage.value = "Error searching for users: ${response.message()}"
                }
            }
            
            override fun onFailure(call: Call<UsersResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "Network error: ${t.message}"
            }
        })
    }
    
    fun sendFriendRequest(userId: Int, circleId: String) {
        _isLoading.value = true
        _errorMessage.value = null
        _successMessage.value = null
        
        val currentUserId = SessionManager.getUserId()
        
        if (currentUserId == null) {
            _errorMessage.value = "You must be logged in to send friend requests"
            _isLoading.value = false
            return
        }
        
        val request = CircleMemberRequest(userId)
        
        ApiClient.apiService.createCircleMember(currentUserId, circleId, request)
            .enqueue(object : Callback<CircleMemberResponse> {
                override fun onResponse(
                    call: Call<CircleMemberResponse>,
                    response: Response<CircleMemberResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        _successMessage.value = "Friend request sent successfully!"
                    } else {
                        _errorMessage.value = "Error sending friend request: ${response.message()}"
                    }
                }
                
                override fun onFailure(call: Call<CircleMemberResponse>, t: Throwable) {
                    _isLoading.value = false
                    _errorMessage.value = "Network error: ${t.message}"
                }
            })
    }
    
    fun clearMessages() {
        _errorMessage.value = null
        _successMessage.value = null
    }
} 