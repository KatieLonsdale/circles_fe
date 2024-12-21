package com.example.innercircles.api.data

import com.google.gson.annotations.SerializedName

data class SignInResponse(
    @SerializedName("token") val token: String,
    @SerializedName("user") val user: nestedData
)

data class nestedData(
    @SerializedName("data") val data: AuthenticatedUser
)
