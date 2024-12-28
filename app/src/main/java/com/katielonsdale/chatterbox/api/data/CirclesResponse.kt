package com.katielonsdale.chatterbox.api.data

import com.google.gson.annotations.SerializedName

data class CirclesResponse(
    @SerializedName("data") val data: List<Circle>
)
