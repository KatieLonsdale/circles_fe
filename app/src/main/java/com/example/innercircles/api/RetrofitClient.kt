package com.example.innercircles.api
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.innercircles.SessionManager

object RetrofitClient {

    private const val BASE_URL = "http://10.0.2.2:3000/api/v0/"

    val retrofitInstance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val sessionManager = SessionManager
    val apiService = ApiClient.createApiService(sessionManager)
}