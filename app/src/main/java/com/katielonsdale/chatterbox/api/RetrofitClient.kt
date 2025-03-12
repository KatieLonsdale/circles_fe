package com.katielonsdale.chatterbox.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.katielonsdale.chatterbox.SessionManager
import com.katielonsdale.chatterbox.api.ApiConstants.BASE_URL

object RetrofitClient {

    val retrofitInstance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val sessionManager = SessionManager
    val apiService = ApiClient.createApiService(sessionManager)
}