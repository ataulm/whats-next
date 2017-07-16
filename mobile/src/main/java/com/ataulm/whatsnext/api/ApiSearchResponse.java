package com.ataulm.whatsnext.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

class ApiSearchResponse {

    @SerializedName("next")
    String cursorToNextPageOfResults;

    @SerializedName("items")
    List<Result> searchItems;

    static class Result {

        @SerializedName("type")
        String type;

        @SerializedName("film")
        ApiFilmSummary filmSummary;
    }
}
