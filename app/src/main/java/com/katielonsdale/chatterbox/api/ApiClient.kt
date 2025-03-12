package com.katielonsdale.chatterbox.api

import com.katielonsdale.chatterbox.SessionManager
import com.katielonsdale.chatterbox.api.ApiConstants.BASE_URL
import com.katielonsdale.chatterbox.api.ApiConstants.Timeouts
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    lateinit var apiService: ApiService
    
    fun createApiService(sessionManager: SessionManager): ApiService {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(sessionManager))
            .connectTimeout(Timeouts.CONNECT, TimeUnit.MILLISECONDS)
            .readTimeout(Timeouts.READ, TimeUnit.MILLISECONDS)
            .writeTimeout(Timeouts.WRITE, TimeUnit.MILLISECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        apiService = retrofit.create(ApiService::class.java)
        return apiService
    }
}
