package com.example.innercircles.api.data

import com.google.gson.annotations.SerializedName

data class PostResponse (
    @SerializedName("data") val data: List<Post>
)

data class Post(
    @SerializedName("id") val id: String,
    @SerializedName("type") val type: String,
    @SerializedName("attributes") val attributes: PostAttributes
)

data class PostAttributes(
    @SerializedName("id") val id: Int,
    @SerializedName("author_id") val authorId: Int,
    @SerializedName("caption") val caption: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("contents") val contents: Contents,
    @SerializedName("comments") val comments: Comments
)

data class Contents(
    @SerializedName("data") val data: List<Content>
)

data class Content(
    @SerializedName("id") val id: String,
    @SerializedName("type") val type: String,
    @SerializedName("attributes") val attributes: ContentAttributes
)

data class ContentAttributes(
    @SerializedName("id") val id: Int,
    @SerializedName("video_url") val videoUrl: String?,
    @SerializedName("image_url") val imageUrl: String?
)

data class Comments(
    @SerializedName("data") val data: List<Comment>
)

data class Comment(
    @SerializedName("id") val id: String,
    @SerializedName("type") val type: String,
    @SerializedName("attributes") val attributes: CommentAttributes
)

data class CommentAttributes(
    @SerializedName("id") val id: Int,
    @SerializedName("content") val content: String,
    @SerializedName("author_id") val authorId: Int,
    @SerializedName("created_at") val createdAt: String
)
