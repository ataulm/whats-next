package com.ataulm.whatsnext.api;

import com.google.gson.annotations.SerializedName;

class ApiFilmSummary {

    @SerializedName("id")
    String letterboxId;

    @SerializedName("name")
    String name;

    @SerializedName("releaseYear")
    int releaseYear;

    @SerializedName("poster")
    ApiImage poster;
}
