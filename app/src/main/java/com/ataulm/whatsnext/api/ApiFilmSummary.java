package com.ataulm.whatsnext.api;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiFilmSummary {

    @SerializedName("name")
    public String name;

    @Nullable
    @SerializedName("releaseYear")
    public Integer releaseYear;

    @Nullable
    @SerializedName("directors")
    public List<ApiContributor> directors;

    @Nullable
    @SerializedName("poster")
    public ApiImage poster;

    @SerializedName("links")
    public List<ApiLink> links;
}
