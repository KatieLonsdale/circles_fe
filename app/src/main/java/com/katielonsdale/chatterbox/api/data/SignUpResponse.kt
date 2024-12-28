package com.katielonsdale.chatterbox.api.data

import com.google.gson.annotations.SerializedName

data class SignUpResponse(
    @SerializedName("data") val data: User
)