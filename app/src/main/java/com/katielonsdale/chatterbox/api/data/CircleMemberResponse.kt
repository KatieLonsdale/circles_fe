package com.katielonsdale.chatterbox.api.data

import com.google.gson.annotations.SerializedName

data class CircleMemberResponse(
    @SerializedName("data") val data: CircleMemberData
)

data class CircleMemberData(
    @SerializedName("id") val id: String,
    @SerializedName("type") val type: String,
    @SerializedName("attributes") val attributes: CircleMemberAttributes
)

data class CircleMemberAttributes(
    @SerializedName("id") val id: Int,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("circle_id") val circleId: Int
) 