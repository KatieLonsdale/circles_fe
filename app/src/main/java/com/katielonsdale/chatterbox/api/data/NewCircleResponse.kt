package com.katielonsdale.chatterbox.api.data

import com.google.gson.annotations.SerializedName

data class NewCircleResponse(
    @SerializedName("data") val data: Circle,
)
