package com.katielonsdale.chatterbox.api.data

import com.google.gson.annotations.SerializedName

data class CommentResponse(
    @SerializedName("data") val data: Comment
)

//comment is defined in PostResponse.kt

//comment attributes is defined in PostResponse.kt
