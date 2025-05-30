package com.katielonsdale.chatterbox.api.data

import com.google.gson.annotations.SerializedName

data class CircleAttributes(
    @SerializedName("id") val id: Int,
    @SerializedName("user_id") val userId: String,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String
)
