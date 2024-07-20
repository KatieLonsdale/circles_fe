package com.example.innercircles.api.data

import com.google.gson.annotations.SerializedName

data class PostResponse (
    @SerializedName("data") val data: PostResponsePost
)

data class PostResponsePost (
    @SerializedName("attributes") val attributes: PostAttributes
)

data class PostAttributes (
    @SerializedName("id") val id: Int,
    @SerializedName("author_id") val authorId: Int,
    @SerializedName("caption") val caption: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("contents") val contents: PostResponseContents
    )

data class PostResponseContents (
    @SerializedName("data") val data: List<Content>
)

data class Content (
    @SerializedName("attributes") val attributes: ContentAttributes
)

data class ContentAttributes(
    @SerializedName("id") val id: Int,
    @SerializedName("image") val image: ByteArray,
    @SerializedName("video") val video: ByteArray
)