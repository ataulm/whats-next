package com.ataulm.whatsnext.api;

import com.google.gson.annotations.SerializedName;

class ApiAuthResponse {

    @SerializedName("access_token")
    String accessToken;

    @SerializedName("expires_in")
    long secondsUntilExpiry;

    @SerializedName("refresh_token")
    String refreshToken;
}
