package com.katielonsdale.chatterbox.api.data

import com.google.gson.annotations.SerializedName

data class NewCircleRequest(
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
)
