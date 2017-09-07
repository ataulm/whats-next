package com.ataulm.whatsnext.api;

import com.ataulm.whatsnext.Film;
import com.ataulm.whatsnext.FilmRelationship;
import com.ataulm.whatsnext.FilmSummary;
import com.ataulm.whatsnext.Image;
import com.ataulm.whatsnext.Poster;
import com.ataulm.whatsnext.Token;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FakeLetterboxd implements Letterboxd {

    @Override
    public Token fetchAccessToken(String username, String password) throws IOException {
        return createFakeToken();
    }

    @Override
    public Token refreshAccessToken(String refreshToken) throws IOException {
        return createFakeToken();
    }

    private Token createFakeToken() {
        return new Token("", "", TimeUnit.DAYS.toMillis(365 * 100));
    }

    @Override
    public List<FilmSummary> search(String searchTerm) throws IOException {
        return Arrays.asList(
                new FilmSummary("id1", "film1", "1989", singleImagePoster()),
                new FilmSummary("id2", "film2", "1990", singleImagePoster())
        );
    }

    @Override
    public Film film(String letterboxdId, String accessToken) throws IOException {
        return new Film(
                new FilmSummary("idwhatever", "whatevername", "2011", singleImagePoster()),
                new FilmRelationship(false, false, false, 0)
        );
    }

    private Poster singleImagePoster() {
        return new Poster(
                Collections.singletonList(
                        new Image(70, 105, "http://skyfall.a.ltrbxd.com/resized/film-poster/4/6/2/8/0/46280-the-iron-giant-0-70-0-105-crop.jpg?k=502cb008bb")
                )
        );
    }

    @Override
    public ApiMemberAccountResponse me(String accessToken) throws IOException {
        return null;
    }

    @Override
    public List<FilmSummary> watchlist(String accessToken, String userId) throws IOException {
        return null;
    }
}
