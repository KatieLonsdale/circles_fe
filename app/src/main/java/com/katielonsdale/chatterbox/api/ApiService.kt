package com.katielonsdale.chatterbox.api

import com.katielonsdale.chatterbox.api.data.Circle
import com.katielonsdale.chatterbox.api.data.CirclesResponse
import com.katielonsdale.chatterbox.api.data.CommentRequest
import com.katielonsdale.chatterbox.api.data.CommentResponse
import com.katielonsdale.chatterbox.api.data.NewCircleRequest
import com.katielonsdale.chatterbox.api.data.NewPostResponse
import com.katielonsdale.chatterbox.api.data.PostRequest
import com.katielonsdale.chatterbox.api.data.PostsResponse
import com.katielonsdale.chatterbox.api.data.SignInRequest
import com.katielonsdale.chatterbox.api.data.SignInResponse
import com.katielonsdale.chatterbox.api.data.SignUpRequest
import com.katielonsdale.chatterbox.api.data.SignUpResponse
import com.katielonsdale.chatterbox.api.data.UserRequest
import com.katielonsdale.chatterbox.api.data.UsersResponse
import com.katielonsdale.chatterbox.api.data.CircleMemberRequest
import com.katielonsdale.chatterbox.api.data.CircleMemberResponse
import com.katielonsdale.chatterbox.api.data.FriendshipRequest
import com.katielonsdale.chatterbox.api.data.FriendshipResponse
import com.katielonsdale.chatterbox.api.data.NewCircleResponse
import com.katielonsdale.chatterbox.api.data.NotificationRequest
import com.katielonsdale.chatterbox.api.data.NotificationResponse
import com.katielonsdale.chatterbox.api.data.NotificationsResponse
import com.katielonsdale.chatterbox.api.data.PostResponse
import com.katielonsdale.chatterbox.api.data.UserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

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

    @GET("users/{userId}")
    fun getUser(
        @Path("userId") userId: String?
    ): Call<UserResponse>

    @POST("sessions")
    fun authenticateUser(
        @Body signInRequest: SignInRequest,
        @Header("No-Authorization") noAuth: Boolean = true
    ): Call<SignInResponse>

    @POST("users")
    fun signUpUser(
        @Body signUpRequest: SignUpRequest,
        @Header("No-Authorization") noAuth: Boolean = true
    ): Call<SignUpResponse>

    @PATCH("users/{userId}")
    fun updateUser(
        @Path("userId") userId: String?,
        @Body userRequest: UserRequest,
    ): Call<Void>

    @GET("users/search")
    fun searchUsers(
        @Query("query") searchQuery: String
    ): Call<UsersResponse>

//    POSTS
    @GET("users/{userId}/circles/{circleId}/posts")
    fun getPostsForCircle(
        @Path("userId") userId: String?,
        @Path("circleId") circleId: String
    ): Call<PostsResponse>

    @GET("/api/v0/users/{userId}/newsfeed")
    fun getNewsfeed(
        @Path("userId") userId: String?
    ): Call<PostsResponse>

    @GET("/api/v0/users/{userId}/circles/{circleId}/posts/{postId}")
    fun getPost(
        @Path("userId") userId: String,
        @Path("circleId") circleId: String,
        @Path("postId") postId: String,
    ): Call<PostResponse>

    @POST("users/{authorId}/circles/{circleId}/posts")
    fun createPost(
        @Path("authorId") userId: String?,
        @Path("circleId") circleId: String?,
        @Body postRequest: PostRequest
    ): Call<NewPostResponse>
//    TODO: these should not be nullable

    // COMMENTS
    @POST("users/{userId}/circles/{circleId}/posts/{postsId}/comments")
    fun createComment(
        @Path("userId") userId: String?,
        @Path("circleId") circleId: String,
        @Path("postsId") postsId: String,
        @Body commentRequest: CommentRequest
    ): Call<CommentResponse>

    // CIRCLES
    @POST("users/{userId}/circles")
    fun createCircle(
        @Path("userId") userId: String?,
        @Body circleRequest: NewCircleRequest
    ): Call<NewCircleResponse>
    
    // CIRCLE MEMBERS
    @POST("users/{userId}/circles/{circleId}/circle_members")
    fun createCircleMember(
        @Path("userId") userId: String?,
        @Path("circleId") circleId: String,
        @Body circleMemberRequest: CircleMemberRequest
    ): Call<CircleMemberResponse>
    
    // FRIENDSHIPS
    @POST("users/{userId}/friendships")
    fun createFriendship(
        @Path("userId") userId: String?,
        @Body friendshipRequest: FriendshipRequest
    ): Call<FriendshipResponse>
    
    @GET("users/{userId}/friendships")
    fun getFriendships(
        @Path("userId") userId: String?
    ): Call<UsersResponse>
    
    @GET("users/{userId}/friendships/pending")
    fun getPendingFriendships(
        @Path("userId") userId: String?
    ): Call<UsersResponse>
    
    @PATCH("users/{userId}/friendships/{friendshipId}/accept")
    fun acceptFriendship(
        @Path("userId") userId: String?,
        @Path("friendshipId") friendshipId: String
    ): Call<FriendshipResponse>
    
    @PATCH("users/{userId}/friendships/{friendshipId}/reject")
    fun rejectFriendship(
        @Path("userId") userId: String?,
        @Path("friendshipId") friendshipId: String
    ): Call<FriendshipResponse>
    
    @DELETE("users/{userId}/friendships/{friendshipId}")
    fun deleteFriendship(
        @Path("userId") userId: String?,
        @Path("friendshipId") friendshipId: String
    ): Call<Void>

    // NOTIFICATIONS
    @GET("users/{userId}/notifications")
    fun getUserNotifications(
        @Path("userId") userId: String
    ): Call<NotificationsResponse>

    @PATCH("users/{userId}/notifications/{notificationId}")
    fun updateNotification(
        @Path("userId") userId: String,
        @Path("notificationId") notificationId: String,
        @Body notificationRequest: NotificationRequest
    ): Call<NotificationResponse>
}