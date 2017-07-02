package com.ataulm.whatsnext.letterboxd;

import com.google.gson.annotations.SerializedName;

import java.util.List;

// TODO: these should all be package
public class ApiFilmSummary {

    @SerializedName("id")
    public String letterboxId;

    @SerializedName("name")
    public String name;

    @SerializedName("releaseYear")
    public int releaseYear;

    @SerializedName("poster")
    public Image poster;

    static class Image {


        @SerializedName("sizes")
        public List<Size> sizes;

        static class Size {

            @SerializedName("width")
            public int width;

            @SerializedName("height")
            public int height;

            @SerializedName("url")
            public String url;

        }

    }

}
