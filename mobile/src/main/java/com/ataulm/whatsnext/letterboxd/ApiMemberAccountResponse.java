package com.ataulm.whatsnext.letterboxd;

import com.google.gson.annotations.SerializedName;

// TODO: these should all be package
public class ApiMemberAccountResponse {

    @SerializedName("member")
    public Member member;

    public static class Member {
        @SerializedName("id")
        public String letterboxId;

        @SerializedName("displayName")
        public String displayName;
    }
}
