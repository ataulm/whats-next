package com.ataulm.whatsnext;

import com.google.gson.annotations.SerializedName;

class ApiMemberAccountResponse {

    @SerializedName("member")
    Member member;

    static class Member {
        @SerializedName("id")
        String letterboxId;

        @SerializedName("displayName")
        String displayName;
    }
}
