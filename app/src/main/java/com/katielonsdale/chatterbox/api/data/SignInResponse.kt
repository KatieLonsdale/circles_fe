package com.katielonsdale.chatterbox.api.data

import com.google.gson.annotations.SerializedName

data class SignInResponse(
    @SerializedName("token") val token: String,
    @SerializedName("user") val user: UserResponse
)
