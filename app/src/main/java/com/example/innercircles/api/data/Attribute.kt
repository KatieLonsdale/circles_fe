package com.example.innercircles.api.data

import com.google.gson.annotations.SerializedName

data class Attribute(
    @SerializedName("id") val id: Int,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String
)
