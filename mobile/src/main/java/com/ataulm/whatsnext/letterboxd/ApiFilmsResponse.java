package com.ataulm.whatsnext.letterboxd;

import com.google.gson.annotations.SerializedName;

import java.util.List;

// TODO: these should all be package
public class ApiFilmsResponse {

    @SerializedName("next")
    public String cursorToNextPageOfResults;

    @SerializedName("items")
    public List<ApiFilmSummary> filmSummaries;
}
