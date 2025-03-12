package com.katielonsdale.chatterbox.api.data

import com.google.gson.annotations.SerializedName

data class FriendshipRequest(
    @SerializedName("friend_id") val friendId: Int
) 