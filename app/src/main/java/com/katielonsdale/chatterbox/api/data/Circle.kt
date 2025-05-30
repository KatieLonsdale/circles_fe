package com.katielonsdale.chatterbox.api.data

import com.google.gson.annotations.SerializedName

data class Circle(
    @SerializedName("id") val id: String,
    @SerializedName("type") val type: String,
    @SerializedName("attributes") val attributes: CircleAttributes
)
