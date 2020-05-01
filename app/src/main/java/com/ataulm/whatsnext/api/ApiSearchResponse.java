package com.ataulm.whatsnext.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiSearchResponse {

    @SerializedName("next")
    public String cursorToNextPageOfResults;

    @SerializedName("items")
    public List<Result> searchItems;

    public static class Result {

        @SerializedName("type")
        public String type;

        @SerializedName("film")
        public ApiFilmSummary filmSummary;
    }
}
