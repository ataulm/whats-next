package com.ataulm.whatsnext.api;

import com.google.gson.annotations.SerializedName;

public class ApiFilmRelationship {

    @SerializedName("watched")
    public boolean watched;

    @SerializedName("liked")
    public boolean liked;

    @SerializedName("inWatchlist")
    public boolean inWatchlist;

    @SerializedName("rating")
    public double rating;
}
