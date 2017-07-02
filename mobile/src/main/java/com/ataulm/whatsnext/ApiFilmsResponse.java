package com.ataulm.whatsnext;

import com.google.gson.annotations.SerializedName;

import java.util.List;

class ApiFilmsResponse {

    @SerializedName("next")
    String cursorToNextPageOfResults;

    @SerializedName("items")
    List<Object> filmSummaries;
}
