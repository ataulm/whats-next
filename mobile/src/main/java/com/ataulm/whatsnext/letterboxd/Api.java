package com.ataulm.whatsnext.letterboxd;

import com.ataulm.whatsnext.Clock;
import com.ataulm.whatsnext.Film;
import com.ataulm.whatsnext.FilmRelationship;
import com.ataulm.whatsnext.FilmSummary;
import com.ataulm.whatsnext.Token;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Api {

    private static final String HTTP_METHOD_POST = "POST";
    private static final String HTTP_METHOD_GET = "GET";

    private final String apiKey;
    private final String apiSecret;
    private final Clock clock;
    private final TokenConverter tokenConverter;
    private final FilmSummaryConverter filmSummaryConverter;
    private final FilmRelationshipConverter filmRelationshipConverter;
    private final OkHttpClient okHttpClient;
    private final Gson gson;

    public Api(String apiKey, String apiSecret, Clock clock, TokenConverter tokenConverter, FilmSummaryConverter filmSummaryConverter, FilmRelationshipConverter filmRelationshipConverter, OkHttpClient okHttpClient, Gson gson) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.clock = clock;
        this.tokenConverter = tokenConverter;
        this.filmSummaryConverter = filmSummaryConverter;
        this.filmRelationshipConverter = filmRelationshipConverter;
        this.okHttpClient = okHttpClient;
        this.gson = gson;
    }

    public Token fetchAccessToken(String username, String password) throws IOException {
        String urlEncodedBody = new LetterboxdBodyBuilder()
                .add("grant_type", "password")
                .add("username", username)
                .add("password", password)
                .build();
        return authTokenRequest(urlEncodedBody);
    }

    public Token refreshAccessToken(String refreshToken) throws IOException {
        String urlEncodedBody = new LetterboxdBodyBuilder()
                .add("grant_type", "refresh_token")
                .add("refresh_token", refreshToken)
                .build();
        return authTokenRequest(urlEncodedBody);
    }

    private Token authTokenRequest(String urlEncodedBody) throws IOException {
        String url = new LetterboxdUrlBuilder(apiKey, clock).path("/auth/token").build();
        Response response = createAndExecuteRequest(HTTP_METHOD_POST, url, urlEncodedBody);
        String responseString = response.body().string();
        ApiAuthResponse deserializedResponse = gson.fromJson(responseString, ApiAuthResponse.class);
        return tokenConverter.convert(deserializedResponse);
    }

    public List<FilmSummary> search(String searchTerm) throws IOException {
        String url = new LetterboxdUrlBuilder(apiKey, clock).path("/search").addQueryParameter("input", searchTerm).build();
        Response response = createAndExecuteRequest(HTTP_METHOD_GET, url);
        ApiSearchResponse deserializedResponse = gson.fromJson(response.body().string(), ApiSearchResponse.class);
        List<FilmSummary> filmSummaries = new ArrayList<>(deserializedResponse.searchItems.size());
        for (ApiSearchResponse.Result searchItem : deserializedResponse.searchItems) {
            if (!"FilmSearchItem".equals(searchItem.type)) {
                continue;
            }
            FilmSummary filmSummary = filmSummaryConverter.convert(searchItem.filmSummary);
            filmSummaries.add(filmSummary);
        }
        return filmSummaries;
    }

    public Film film(String letterboxdId, String accessToken) throws IOException {
        FilmSummary filmSummary = filmSummary(letterboxdId);
        FilmRelationship filmRelationship = filmRelationship(letterboxdId, accessToken);
        return new Film(filmSummary, filmRelationship);
    }

    private FilmSummary filmSummary(String letterboxdId) throws IOException {
        String url = new LetterboxdUrlBuilder(apiKey, clock).path("/film/" + letterboxdId).build();
        Response response = createAndExecuteRequest(HTTP_METHOD_GET, url);
        ApiFilm deserializedResponse = gson.fromJson(response.body().string(), ApiFilm.class);
        return filmSummaryConverter.convert(deserializedResponse);
    }

    private FilmRelationship filmRelationship(String letterboxdId, String accessToken) throws IOException {
        String url = new LetterboxdUrlBuilder(apiKey, clock).path("/film/" + letterboxdId + "/me").build();
        Response response = createAndExecuteUserAuthedRequest(HTTP_METHOD_GET, url, accessToken);
        ApiFilmRelationship deserializedResponse = gson.fromJson(response.body().string(), ApiFilmRelationship.class);
        return filmRelationshipConverter.convert(deserializedResponse);
    }

    public ApiMemberAccountResponse me(String accessToken) throws IOException {
        String url = new LetterboxdUrlBuilder(apiKey, clock).path("/me").build();
        Response response = createAndExecuteUserAuthedRequest(HTTP_METHOD_GET, url, accessToken);
        String responseString = response.body().string();
        return gson.fromJson(responseString, ApiMemberAccountResponse.class);
    }

    public List<FilmSummary> watchlist(String accessToken, String userId) throws IOException {
        String url = new LetterboxdUrlBuilder(apiKey, clock).path("/member/" + userId + "/watchlist").build();
        Response response = createAndExecuteUserAuthedRequest(HTTP_METHOD_GET, url, accessToken);
        String responseString = response.body().string();
        ApiFilmsResponse deserializedResponse = gson.fromJson(responseString, ApiFilmsResponse.class);
        List<FilmSummary> filmSummaries = new ArrayList<>(deserializedResponse.filmSummaries.size());
        for (ApiFilmSummary filmSummary : deserializedResponse.filmSummaries) {
            FilmSummary film = filmSummaryConverter.convert(filmSummary);
            filmSummaries.add(film);
        }
        return filmSummaries;
    }

    private Response createAndExecuteUserAuthedRequest(String httpMethod, String url, String accessToken) throws IOException {
        Request.Builder builder = new Request.Builder()
                .url(url + "&signature=" + generateSignature(httpMethod, url, ""))
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer " + accessToken);

        Request request = builder.build();
        return okHttpClient.newCall(request).execute();
    }

    private Response createAndExecuteRequest(String httpMethod, String url) throws IOException {
        return createAndExecuteRequest(httpMethod, url, "");
    }

    private Response createAndExecuteRequest(String httpMethod, String url, String urlEncodedBody) throws IOException {
        Request.Builder builder = new Request.Builder()
                .url(url)
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Signature " + generateSignature(httpMethod, url, urlEncodedBody));

        if (HTTP_METHOD_POST.equals(httpMethod)) {
            builder.post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), urlEncodedBody));
        }

        Request request = builder.build();
        return okHttpClient.newCall(request).execute();
    }

    private String generateSignature(String httpMethod, String url, String body) {
        String preHashed = String.format(Locale.US, "%s\u0000%s\u0000%s", httpMethod.toUpperCase(Locale.US), url, body);
        return HmacSha256.generateHash(apiSecret, preHashed).toLowerCase(Locale.US);
    }
}
