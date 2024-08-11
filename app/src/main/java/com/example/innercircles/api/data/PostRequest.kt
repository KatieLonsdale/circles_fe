package com.example.innercircles.api.data

import com.google.gson.annotations.SerializedName

data class PostRequest (
    @SerializedName("caption") val caption: String?,
    @SerializedName("contents") val contents: PostRequestContent?
)

data class PostRequestContent (
    @SerializedName("image") val image: ByteArray?,
    @SerializedName("video") val video: ByteArray?
)