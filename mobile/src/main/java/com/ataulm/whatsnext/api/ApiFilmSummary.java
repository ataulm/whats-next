package com.ataulm.whatsnext.api;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

class ApiFilmSummary {

    @SerializedName("name")
    String name;

    @Nullable
    @SerializedName("tagline")
    String tagline;

    @Nullable
    @SerializedName("description")
    String description;

    @Nullable
    @SerializedName("releaseYear")
    Integer releaseYear;

    @Nullable
    @SerializedName("runTime")
    Integer runtimeMinutes;

    @Nullable
    @SerializedName("poster")
    ApiImage poster;

    @Nullable
    @SerializedName("backdrop")
    ApiImage backdrop;

    @SerializedName("links")
    List<ApiLink> links;

    @Nullable
    @SerializedName("contributions")
    List<ApiContribution> contributions;
}
