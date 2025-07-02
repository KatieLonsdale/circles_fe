package com.katielonsdale.chatterbox.api.data.source

import android.util.Log
import com.katielonsdale.chatterbox.SessionManager
import com.katielonsdale.chatterbox.api.RetrofitClient
import com.katielonsdale.chatterbox.api.data.NewPostResponse
import com.katielonsdale.chatterbox.api.data.NewPostUiState
import com.katielonsdale.chatterbox.api.data.PostRequest
import com.katielonsdale.chatterbox.api.data.PostRequestContent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val TAG = "Post Data Source"

object PostDataSource {
    fun createPost(
        newPostUiState: NewPostUiState
    ) {
        val userId = SessionManager.getUserId()
        val postRequest = createPostRequest(newPostUiState)
        val chatterIds = newPostUiState.chatterIds

        for (chatterId in chatterIds) {
            RetrofitClient.apiService.createPost(userId, chatterId, postRequest).enqueue(object : Callback<NewPostResponse> {
                override fun onResponse(
                    call: Call<NewPostResponse>,
                    response: Response<NewPostResponse>
                ) {
                    if (response.isSuccessful) {
                        Log.e(TAG, "Post created successfully: ${response.body()}")
                    } else {
                        Log.e(TAG,"Failed to createPost: ${response.body()} status: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<NewPostResponse>, t: Throwable) {
                    Log.e(TAG, "Error creating posts", t)
                }
            })
        }
    }

    fun createPostRequest(newPostUiState: NewPostUiState): PostRequest {
        val caption = newPostUiState.caption
        val image = newPostUiState.contents?.getImage()
        if (image != null) {
            val postRequestContent = PostRequestContent(image, null)
            val postRequest = PostRequest(caption,postRequestContent)
            return postRequest
        } else {
            return PostRequest(caption,null)
        }
    }
}