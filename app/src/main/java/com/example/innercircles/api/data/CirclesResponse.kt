package com.example.innercircles.api.data

import com.google.gson.annotations.SerializedName

data class CirclesResponse(
    @SerializedName("data") val data: List<Circle>
)
