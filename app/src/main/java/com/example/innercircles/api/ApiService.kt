package com.example.innercircles.api

import com.example.innercircles.api.data.CirclesResponse
import com.example.innercircles.api.data.SignInRequest
import com.example.innercircles.api.data.SignInResponse
import com.example.innercircles.api.data.SignUpRequest
import com.example.innercircles.api.data.SignUpResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("users/{userId}/circles")
    fun getCircles(@Path("userId") userId: String?): Call<CirclesResponse>

    @POST("users/authenticate")
    fun authenticateUser(@Body signInRequest: SignInRequest): Call<SignInResponse>

    @POST("users")
    fun signUp(@Body signUpRequest: SignUpRequest): Call<SignUpResponse>
}