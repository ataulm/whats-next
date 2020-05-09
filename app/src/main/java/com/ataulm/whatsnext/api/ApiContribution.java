package com.ataulm.whatsnext.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiContribution {

    @SerializedName("type")
    public String type;

    @SerializedName("contributors")
    public List<ApiContributor> contributors;
}
