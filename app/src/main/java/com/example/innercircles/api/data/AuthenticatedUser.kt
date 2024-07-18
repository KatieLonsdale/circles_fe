package com.example.innercircles.api.data

import com.google.gson.annotations.SerializedName

data class AuthenticatedUser(
    @SerializedName("id") val id: String
)