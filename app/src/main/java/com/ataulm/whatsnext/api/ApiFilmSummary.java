package com.ataulm.whatsnext.api;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiFilmSummary {

    @SerializedName("name")
    public String name;

    @Nullable
    @SerializedName("tagline")
    public String tagline;

    @Nullable
    @SerializedName("description")
    public String description;

    @Nullable
    @SerializedName("releaseYear")
    public Integer releaseYear;

    @Nullable
    @SerializedName("runTime")
    public Integer runtimeMinutes;

    @Nullable
    @SerializedName("poster")
    public ApiImage poster;

    @SerializedName("links")
    public List<ApiLink> links;

    @Nullable
    @SerializedName("genres")
    public List<ApiGenre> genres;

    @Nullable
    @SerializedName("contributions")
    public List<ApiContribution> contributions;
}
