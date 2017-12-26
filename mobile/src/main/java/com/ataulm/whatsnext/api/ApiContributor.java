package com.ataulm.whatsnext.api;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

class ApiContributor {

    @SerializedName("id")
    String id;

    @SerializedName("name")
    String name;

    @Nullable // only on actors
    @SerializedName("characterName")
    String characterName;
}
