package com.ataulm.whatsnext.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

class ApiImage {

    @SerializedName("sizes")
    List<Size> sizes;

    static class Size {

        @SerializedName("width")
        int width;

        @SerializedName("height")
        int height;

        @SerializedName("url")
        String url;
    }
}
