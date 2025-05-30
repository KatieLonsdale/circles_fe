package com.katielonsdale.chatterbox.api.data

import com.google.gson.annotations.SerializedName

data class NewCircleRequest(
    @SerializedName("circle") val circle: NewCircleAttributes,
)

data class NewCircleAttributes(
    @SerializedName("user_id") val userId: String,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String
)
