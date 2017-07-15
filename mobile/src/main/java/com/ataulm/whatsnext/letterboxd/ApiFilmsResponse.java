package com.ataulm.whatsnext.letterboxd;

import com.google.gson.annotations.SerializedName;

import java.util.List;

class ApiFilmsResponse {

    @SerializedName("next")
    String cursorToNextPageOfResults;

    @SerializedName("items")
    List<ApiFilmSummary> filmSummaries;
}
