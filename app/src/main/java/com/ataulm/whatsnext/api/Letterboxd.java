package com.ataulm.whatsnext.api;

import com.ataulm.whatsnext.Film;
import com.ataulm.whatsnext.FilmSummary;
import com.ataulm.whatsnext.Token;

import java.io.IOException;
import java.util.List;

@Deprecated // use LetterboxdApi
public interface Letterboxd {

    Token fetchAccessToken(String username, String password) throws IOException;

    Token refreshAccessToken(String refreshToken) throws IOException;

    List<FilmSummary> search(String searchTerm) throws IOException;

    Film film(String letterboxdId, String accessToken) throws IOException;
}
