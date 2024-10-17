package com.example.innercircles.api.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class PostResponse (
    @SerializedName("data") val data: List<Post>
)

data class NewPostResponse (
    @SerializedName("data") val data: Post
)

@Parcelize
data class Post(
    @SerializedName("id") val id: String,
    @SerializedName("type") val type: String,
    @SerializedName("attributes") val attributes: PostAttributes
) : Parcelable

@Parcelize
data class PostAttributes(
    @SerializedName("id") val id: Int,
    @SerializedName("author_id") val authorId: Int,
    @SerializedName("caption") val caption: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("contents") val contents: Contents,
    @SerializedName("comments") val comments: Comments,
    @SerializedName("author_display_name") val authorDisplayName: String,
    @SerializedName("circle_id") val circleId: String,
    @SerializedName("circle_name") val circleName: String,
) : Parcelable

@Parcelize
data class Contents(
    @SerializedName("data") val data: List<Content>
) : Parcelable

@Parcelize
data class Content(
    @SerializedName("id") val id: String,
    @SerializedName("type") val type: String,
    @SerializedName("attributes") val attributes: ContentAttributes
) : Parcelable

@Parcelize
data class ContentAttributes(
    @SerializedName("id") val id: Int,
    @SerializedName("video_url") val videoUrl: String?,
    @SerializedName("presigned_image_url") val imageUrl: String?
) : Parcelable

@Parcelize
data class Comments(
    @SerializedName("data") val data: List<Comment>
) : Parcelable

@Parcelize
data class Comment(
    @SerializedName("id") val id: String,
    @SerializedName("attributes") val attributes: CommentAttributes
) : Parcelable

@Parcelize
data class CommentAttributes(
    @SerializedName("id") val id: String,
    @SerializedName("comment_text") val commentText: String,
    @SerializedName("author_id") val authorId: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("author_display_name") val authorDisplayName: String,
    @SerializedName("parent_comment_id") val parentCommentId: String?,
) : Parcelable
