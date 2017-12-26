package com.ataulm.whatsnext.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

class ApiContribution {

    @SerializedName("type")
    String type;

    @SerializedName("contributors")
    List<ApiContributor> contributors;
}
