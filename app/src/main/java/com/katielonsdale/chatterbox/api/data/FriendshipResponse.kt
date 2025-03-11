package com.katielonsdale.chatterbox.api.data

import com.google.gson.annotations.SerializedName

data class FriendshipResponse(
    val data: FriendshipData
)

data class FriendshipData(
    val id: String,
    val type: String,
    val attributes: FriendshipAttributes
)

data class FriendshipAttributes(
    val id: Int,
    val status: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    val friend: FriendshipUser,
    val user: FriendshipUser
)

data class FriendshipUser(
    val id: Int,
    val email: String,
    @SerializedName("display_name") val displayName: String
) 