package com.katielonsdale.chatterbox.api.data

import com.google.gson.annotations.SerializedName

data class UsersResponse(
    @SerializedName("data") val data: List<UserData>
)

data class UserData(
    @SerializedName("id") val id: String,
    @SerializedName("type") val type: String,
    @SerializedName("attributes") val attributes: UserAttributes
)

data class UserAttributes(
    @SerializedName("id") val id: Int,
    @SerializedName("email") val email: String,
    @SerializedName("display_name") val displayName: String
) 