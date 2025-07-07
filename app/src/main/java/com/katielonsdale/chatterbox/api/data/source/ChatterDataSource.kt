package com.katielonsdale.chatterbox.api.data.source

import android.util.Log
import com.katielonsdale.chatterbox.SessionManager
import com.katielonsdale.chatterbox.api.RetrofitClient
import com.katielonsdale.chatterbox.api.data.CirclesResponse
import com.katielonsdale.chatterbox.api.data.NewCircleAttributes
import com.katielonsdale.chatterbox.api.data.NewCircleRequest
import com.katielonsdale.chatterbox.api.data.NewCircleResponse
import com.katielonsdale.chatterbox.ui.TAG
import com.katielonsdale.chatterbox.ui.circles
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object ChatterDataSource {
    fun getChatters(userId: String?) {
        RetrofitClient.apiService.getCircles(userId).enqueue(object : Callback<CirclesResponse> {
            override fun onResponse(
                call: Call<CirclesResponse>,
                response: Response<CirclesResponse>
            ) {
                if (response.isSuccessful) {
                    circles = response.body()?.data ?: emptyList()
                } else {
                    Log.e(
                        "SelectCirclesScreen",
                        "Failed to fetch newsfeed: ${response.errorBody()?.string()}"
                    )
                }
            }

            override fun onFailure(call: Call<CirclesResponse>, t: Throwable) {
                Log.e("SelectCirclesScreen", "Error fetching posts", t)
            }
        })
    }

    fun createChatter(
        chatterName: String,
        chatterDescription: String,
    ) {
        val userId = SessionManager.getUserId()
        val newChatterRequest = NewCircleRequest(
            circle = NewCircleAttributes(
                userId = userId,
                name = chatterName,
                description = chatterDescription,
            )
        )
        RetrofitClient.apiService.createCircle(userId, newChatterRequest).enqueue(object :
            Callback<NewCircleResponse> {
            override fun onResponse(
                call: Call<NewCircleResponse>,
                response: Response<NewCircleResponse>
            ) {
                if (response.isSuccessful) {
                    Log.e(TAG, "Chatter created successfully: ${response.body()}"
                    )
                } else {
                    Log.e(TAG, "Failed to create chatter: ${response.body()} status: ${response.code()}"
                    )
                }
            }

            override fun onFailure(call: Call<NewCircleResponse>, t: Throwable) {
                Log.e(TAG, "Error creating chatter", t)
            }
        })
    }
}