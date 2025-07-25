package com.katielonsdale.chatterbox.api.data

import com.google.gson.annotations.SerializedName

data class FriendshipResponse(
    val data: Friendship
)

data class FriendshipsResponse(
    val data: List<Friendship>
)

data class Friendship(
    val id: String,
    val type: String,
    val attributes: FriendshipAttributes
)

data class FriendshipAttributes(
    val id: Int,
    val status: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    val friend: Friend,
    val user: Friend
)

data class Friend(
    val id: Int,
    val email: String,
    @SerializedName("display_name") val displayName: String
) 