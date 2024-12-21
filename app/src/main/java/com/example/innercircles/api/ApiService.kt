package com.example.innercircles.api

import com.example.innercircles.api.data.Circle
import com.example.innercircles.api.data.CirclesResponse
import com.example.innercircles.api.data.CommentRequest
import com.example.innercircles.api.data.CommentResponse
import com.example.innercircles.api.data.NewPostResponse
import com.example.innercircles.api.data.PostRequest
import com.example.innercircles.api.data.PostResponse
import com.example.innercircles.api.data.SignInRequest
import com.example.innercircles.api.data.SignInResponse
import com.example.innercircles.api.data.SignUpRequest
import com.example.innercircles.api.data.SignUpResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
//    LOGIN
    @POST("sessions")
    fun getNewToken(
        @Body signInRequest: SignInRequest,
        @Header("No-Authorization") noAuth: Boolean = true
    ): Call<SignInResponse>

//    CIRCLES
    @GET("users/{userId}/circles")
    fun getCircles(
        @Path("userId") userId: String?
    ): Call<CirclesResponse>

    @GET("users/{userId}/circles/{circleId}")
    fun getCircle(
        @Path("userId") userId: String?,
        @Path("circleId") circleId: String
    ): Call<Circle>

//    USERS

    @POST("sessions")
    fun authenticateUser(
        @Body signInRequest: SignInRequest,
        @Header("No-Authorization") noAuth: Boolean = true
    ): Call<SignInResponse>

    @POST("users")
    fun signUpUser(
        @Body signUpRequest: SignUpRequest
    ): Call<SignUpResponse>

//    POSTS
    @GET("users/{userId}/circles/{circleId}/posts")
    fun getPostsForCircle(
        @Path("userId") userId: String?,
        @Path("circleId") circleId: String
    ): Call<PostResponse>

    @GET("/api/v0/users/{userId}/newsfeed")
    fun getNewsfeed(
        @Path("userId") userId: String?
    ): Call<PostResponse>

    @POST("users/{authorId}/circles/{circleId}/posts")
    fun createPost(
        @Path("authorId") userId: String?,
        @Path("circleId") circleId: String?,
        @Body postRequest: PostRequest
    ): Call<NewPostResponse>
//    TODO: these should not be nullable

    @POST("users/{userId}/circles/{circleId}/posts/{postsId}/comments")
    fun createComment(
        @Path("userId") userId: String?,
        @Path("circleId") circleId: String,
        @Path("postsId") postsId: String,
        @Body commentRequest: CommentRequest
    ): Call<CommentResponse>
}