package com.ataulm.whatsnext.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

class ApiFilmSummary {

    @SerializedName("name")
    String name;

    @SerializedName("tagline")
    String tagline;

    @SerializedName("description")
    String description;

    @SerializedName("releaseYear")
    int releaseYear;

    @SerializedName("runTime")
    int runTimeMinutes;

    @SerializedName("poster")
    ApiImage poster;

    @SerializedName("backdrop")
    ApiImage backdrop;

    @SerializedName("links")
    List<ApiLink> links;
}
