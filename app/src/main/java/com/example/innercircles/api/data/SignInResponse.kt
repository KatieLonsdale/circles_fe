package com.example.innercircles.api.data

import com.google.gson.annotations.SerializedName

data class SignInResponse(
    @SerializedName("data") val data: AuthenticatedUser
)
