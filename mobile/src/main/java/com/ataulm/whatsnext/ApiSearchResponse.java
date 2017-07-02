package com.ataulm.whatsnext;

import com.google.gson.annotations.SerializedName;

import java.util.List;

class ApiSearchResponse {

    @SerializedName("next")
    String cursorToNextPageOfResults;

    @SerializedName("items")
    List<Object> searchItems;
}
