package com.katielonsdale.chatterbox.api

import com.katielonsdale.chatterbox.api.data.Circle
import com.katielonsdale.chatterbox.api.data.CirclesResponse
import com.katielonsdale.chatterbox.api.data.CommentRequest
import com.katielonsdale.chatterbox.api.data.CommentResponse
import com.katielonsdale.chatterbox.api.data.NewPostResponse
import com.katielonsdale.chatterbox.api.data.PostRequest
import com.katielonsdale.chatterbox.api.data.PostResponse
import com.katielonsdale.chatterbox.api.data.SignInRequest
import com.katielonsdale.chatterbox.api.data.SignInResponse
import com.katielonsdale.chatterbox.api.data.SignUpRequest
import com.katielonsdale.chatterbox.api.data.SignUpResponse
import com.katielonsdale.chatterbox.api.data.UserRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
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

    @PATCH("users/{userId}")
    fun updateUser(
        @Path("userId") userId: String?,
        @Body userRequest: UserRequest,
    ): Call<Void>

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