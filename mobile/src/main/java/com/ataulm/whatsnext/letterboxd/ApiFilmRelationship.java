package com.ataulm.whatsnext.letterboxd;

import com.google.gson.annotations.SerializedName;

class ApiFilmRelationship {

    @SerializedName("watched")
    boolean watched;

    @SerializedName("liked")
    boolean liked;

    @SerializedName("inWatchlist")
    boolean inWatchlist;

    @SerializedName("rating")
    double rating;
}
