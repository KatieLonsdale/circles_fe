package com.katielonsdale.chatterbox.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import com.katielonsdale.chatterbox.SessionManager
import com.katielonsdale.chatterbox.api.RetrofitClient.apiService
import com.katielonsdale.chatterbox.api.data.Circle
import com.katielonsdale.chatterbox.api.data.CirclesResponse
import com.katielonsdale.chatterbox.api.data.Friend
import com.katielonsdale.chatterbox.api.data.FriendshipsResponse
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

    fun setCurrentUserChatters(userChatters: List<Circle>) {
        _uiState.update { currentState ->
            currentState.copy(
                myChatters = userChatters
            )
        }
    }

    fun setCurrentUserFriends(userFriends: List<Friend>) {
        _uiState.update { currentState ->
            currentState.copy(
                myFriends = userFriends
            )
        }
    }

    fun getCurrentUserChatters(): List<Circle>{
        return _uiState.value.myChatters
    }

    fun getCurrentUserFriends(): List<Friend>{
        return _uiState.value.myFriends
    }

    fun getCurrentUser(): UserUiState {
        return _uiState.value
    }

    // if this function is being called, it is with the intention to set the current user to refer to state of
    fun getUser(
        userId: String,
    ) {
        apiService.getUser(userId).enqueue(object :
            Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    // HTTP 200: Success
                    val userAttributes = response.body()!!.data.attributes
                    setCurrentUser(userAttributes)
                    getUserChatters(userAttributes.id.toString())
                    if (!userAttributes.notificationsToken.isNullOrEmpty()) {
                        SessionManager.saveFcmToken(userAttributes.notificationsToken)
                    }
                    return
                } else if (response.code() == 404) {
                    val errorMessage = response.message()
                    Log.e(TAG, "onResponse: $errorMessage")
                    return
                } else {
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

    fun getUserChatters(
        userId: String,
    ){
        apiService.getCircles(userId).enqueue(object : Callback<CirclesResponse> {
            override fun onResponse(call: Call<CirclesResponse>, response: Response<CirclesResponse>) {
                if (response.isSuccessful){
                    setCurrentUserChatters(
                        userChatters = response.body()!!.data
                    )
                } else {
                    val errorMessage = response.message()
                    Log.e(TAG, "onResponse: $errorMessage")
                    return
                }
            }

            override fun onFailure(call: Call<CirclesResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: $t")
            }
        })
    }

//    fun getUserFriends(
//        userId: String,
//    ){
//        apiService.getFriendships(userId).enqueue(object: Callback<FriendshipsResponse> {
//            override fun onResponse(call: Call<FriendshipsResponse>, response: Response<FriendshipsResponse>) {
//                if (response.isSuccessful){
//                    setCurrentUserFriends(
//                        userFriends = response.body()!!.data
//                    )
//                } else {
//                    val errorMessage = response.message()
//                    Log.e(TAG, "onResponse: $errorMessage")
//                    return
//                }
//            }
//
//            override fun onFailure(call: Call<FriendshipsResponse>, t: Throwable) {
//                Log.e(TAG, "onFailure: $t")
//            }
//        })
//        }
//        )
//    }
}