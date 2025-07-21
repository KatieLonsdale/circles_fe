package com.katielonsdale.chatterbox.ui

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import com.katielonsdale.chatterbox.SessionManager
import com.katielonsdale.chatterbox.api.RetrofitClient
import com.katielonsdale.chatterbox.api.RetrofitClient.apiService
import com.katielonsdale.chatterbox.api.data.Circle
import com.katielonsdale.chatterbox.api.data.CirclesResponse
import com.katielonsdale.chatterbox.api.data.Friend
import com.katielonsdale.chatterbox.api.data.FriendshipsResponse
import com.katielonsdale.chatterbox.api.data.Notification
import com.katielonsdale.chatterbox.api.data.NotificationsResponse
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
    val TAG = "User View Model"
    private val _uiState = MutableStateFlow(UserUiState())
    val uiState: StateFlow<UserUiState> = _uiState.asStateFlow()

    fun onEvent(event: MyEvent) {
        when (event) {
            is MyEvent.UpdateMyNotifications -> {
                getUserNotifications()
            }
        }
    }
    sealed interface MyEvent {
        data object UpdateMyNotifications : MyEvent
    }

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
                myChatters = userChatters.toMutableStateList()
            )
        }
    }

    fun setCurrentUserNotifications(userNotifications: List<Notification>) {
        _uiState.update { currentState ->
            currentState.copy(
                myNotifications = userNotifications.toMutableStateList()
            )
        }
    }

    fun setCurrentUserFriends(userFriends: List<Friend>) {
        _uiState.update { currentState ->
            currentState.copy(
                myFriends = userFriends.toMutableStateList()
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
                    getUserChatters()
                    getUserNotifications()
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

    private fun getUserChatters(){
        val userId = getCurrentUser().id
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

    fun getUserNotifications(
        isRefreshing: MutableState<Boolean>? = null
    ){
        val userId = SessionManager.getUserId()
        apiService.getUserNotifications(userId).enqueue(object :
            Callback<NotificationsResponse> {
            override fun onResponse(call: Call<NotificationsResponse>, response: Response<NotificationsResponse>) {
                if (response.isSuccessful) {
                    setCurrentUserNotifications(
                        userNotifications = response.body()!!.data
                    )
                } else {
                    Log.e(com.katielonsdale.chatterbox.ui.notifications.TAG, "Failed to fetch notifications: ${response.errorBody()?.string()}")
                }
                isRefreshing?.value = false
            }

            override fun onFailure(call: Call<NotificationsResponse>, t: Throwable) {
                Log.e(com.katielonsdale.chatterbox.ui.notifications.TAG, "Error fetching notifications", t)
                isRefreshing?.value = false
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