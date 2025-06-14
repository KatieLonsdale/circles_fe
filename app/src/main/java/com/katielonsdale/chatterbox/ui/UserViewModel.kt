package com.katielonsdale.chatterbox.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import com.katielonsdale.chatterbox.api.RetrofitClient
import com.katielonsdale.chatterbox.api.data.UserAttributes
import com.katielonsdale.chatterbox.api.data.UserResponse
import com.katielonsdale.chatterbox.api.data.UserUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel : ViewModel() {
    val TAG = "UserViewModel"
    private val _uiState = MutableStateFlow(UserUiState())
    val uiState: StateFlow<UserUiState> = _uiState.asStateFlow()

    fun setCurrentUser(user: UserAttributes) {
        _uiState.update { currentState ->
            currentState.copy(
                id = user.id.toString(),
                email = user.email,
                displayName = user.displayName,
                notificationFrequency = user.notificationFrequency,
                lastTouAcceptance = user.lastTouAcceptance,
                notificationsToken = user.notificationsToken,
            )
        }
    }

    fun getCurrentUser(): UserUiState {
        return _uiState.value
    }

    // if this function is being called, it is with the intention to set the current user to refer to state of
    fun getUser(
        userId: String,
    ) {
        RetrofitClient.apiService.getUser(userId).enqueue(object :
            Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    // HTTP 200: Success
                    setCurrentUser(response.body()!!.data.attributes)
                    return
                } else if (response.code() == 404) {
                    val errorMessage = response.message()
                    Log.e(TAG, "onResponse: $errorMessage")
                    return
                } else {
                    // Handle other error codes as needed
                    val errorMessage = response.message()
                    Log.e(TAG, "onResponse: $errorMessage")
                    return
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                // Network or other failure
                Log.e(TAG, "onFailure: $t")
            }
        })
    }
}