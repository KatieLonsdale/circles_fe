package com.katielonsdale.chatterbox.api.data

import com.google.gson.annotations.SerializedName

data class CircleMemberRequest(
    @SerializedName("new_member_id") val newMemberId: Int
) 