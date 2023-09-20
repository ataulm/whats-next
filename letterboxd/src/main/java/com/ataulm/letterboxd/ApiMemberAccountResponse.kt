package com.ataulm.letterboxd

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiMemberAccountResponse(

    @field:Json(name = "member")
    val member: Member
) {

    @JsonClass(generateAdapter = true)
    class Member(
        @field:Json(name = "id")
        val letterboxId: String,

        @field:Json(name = "displayName")
        val displayName: String
    )
}
