package com.katielonsdale.chatterbox.api.data

import com.google.gson.annotations.SerializedName

data class SignUpRequest(
    @SerializedName("email") val email: String,
    @SerializedName("display_name") val displayName: String,
    @SerializedName("password") val password: String,
    @SerializedName("password_confirmation") val confirmPassword: String
)