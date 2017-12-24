package com.ataulm.whatsnext.api

import com.google.gson.annotations.SerializedName

class ApiMemberAccountResponse {

    @SerializedName("member")
    var member: Member? = null

    class Member {
        @SerializedName("id")
        var letterboxId: String? = null

        @SerializedName("displayName")
        var displayName: String? = null
    }
}
