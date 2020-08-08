package com.ataulm.whatsnext.api;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class ApiFilmRelationship {

    @SerializedName("watched")
    public boolean watched;

    @SerializedName("liked")
    public boolean liked;

    @SerializedName("inWatchlist")
    public boolean inWatchlist;

    @Nullable
    @SerializedName("rating")
    public Float rating;
}
