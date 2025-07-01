package com.katielonsdale.chatterbox.api.data.source

import android.util.Log
import com.katielonsdale.chatterbox.api.RetrofitClient
import com.katielonsdale.chatterbox.api.data.CirclesResponse
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
}