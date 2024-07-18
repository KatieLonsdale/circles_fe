package com.example.innercircles.api.data

import com.google.gson.annotations.SerializedName

data class Circle(
    @SerializedName("id") val id: String,
    @SerializedName("type") val type: String,
    @SerializedName("attributes") val attributes: Attribute
)
