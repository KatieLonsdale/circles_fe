package com.example.innercircles.api

import com.example.innercircles.api.data.CirclesResponse
import com.example.innercircles.api.data.PostRequest
import com.example.innercircles.api.data.PostResponse
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
//    CIRCLES
    @GET("users/{userId}/circles")
    fun getCircles(@Path("userId") userId: String?): Call<CirclesResponse>

//    USERS

    @POST("users/authenticate")
    fun authenticateUser(@Body signInRequest: SignInRequest): Call<SignInResponse>

    @POST("users")
    fun signUp(@Body signUpRequest: SignUpRequest): Call<SignUpResponse>

//    POSTS
    @GET("users/{userId}/circles/{circleId}/posts")
    fun getPostsForCircle(@Path("userId") userId: String,
                 @Path("circleId") circleId: String
    ): Call<List<PostResponse>>

    @GET("/api/v0/users/{userId}/newsfeed")
    fun getNewsfeed(@Path("userId") userId: String): Call<PostResponse>

    @POST("users/{authorId}/circles/{circleId}/posts")
    fun createPost(@Path("authorId") userId: String?,
                   @Path("circleId") circleId: String?,
                   @Body postRequest: PostRequest
    ): Call<PostResponse>
}