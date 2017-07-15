package com.ataulm.whatsnext.letterboxd;

import com.google.gson.annotations.SerializedName;

import java.util.List;

class ApiFilmSummary {

    @SerializedName("id")
    String letterboxId;

    @SerializedName("name")
    String name;

    @SerializedName("releaseYear")
    int releaseYear;

    @SerializedName("poster")
    Image poster;

    static class Image {

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
}
