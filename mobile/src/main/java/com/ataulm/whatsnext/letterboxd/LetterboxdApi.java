package com.ataulm.whatsnext.letterboxd;

import com.ataulm.whatsnext.Clock;
import com.ataulm.whatsnext.Film;
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

public class LetterboxdApi {

    private static final String HTTP_METHOD_POST = "POST";
    private static final String HTTP_METHOD_GET = "GET";

    private final String apiKey;
    private final String apiSecret;
    private final Clock clock;
    private final TokenConverter tokenConverter;
    private final FilmConverter filmConverter;
    private final OkHttpClient okHttpClient;
    private final Gson gson;

    public LetterboxdApi(String apiKey, String apiSecret, Clock clock, TokenConverter tokenConverter, FilmConverter filmConverter, OkHttpClient okHttpClient, Gson gson) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.clock = clock;
        this.tokenConverter = tokenConverter;
        this.filmConverter = filmConverter;
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
        ApiAuthResponse apiAuthResponse = gson.fromJson(responseString, ApiAuthResponse.class);
        return tokenConverter.convert(apiAuthResponse);
    }

    public List<Film> search(String searchTerm) throws IOException {
        String url = new LetterboxdUrlBuilder(apiKey, clock).path("/search").addQueryParameter("input", searchTerm).build();
        Response response = createAndExecuteRequest(HTTP_METHOD_GET, url, "");
        ApiSearchResponse apiSearchResponse = gson.fromJson(response.body().string(), ApiSearchResponse.class);
        List<Film> films = new ArrayList<>(apiSearchResponse.searchItems.size());
        for (ApiSearchResponse.Result searchItem : apiSearchResponse.searchItems) {
            if (!"FilmSearchItem".equals(searchItem.type)) {
                continue;
            }
            Film film = filmConverter.convert(searchItem.filmSummary);
            films.add(film);
        }
        return films;
    }

    public ApiMemberAccountResponse me(String accessToken) throws IOException {
        String url = new LetterboxdUrlBuilder(apiKey, clock).path("/me").build();
        Response response = createAndExecuteUserAuthedRequest(HTTP_METHOD_GET, url, accessToken);
        String responseString = response.body().string();
        return gson.fromJson(responseString, ApiMemberAccountResponse.class);
    }

    public List<Film> watchlist(String accessToken, String userId) throws IOException {
        String url = new LetterboxdUrlBuilder(apiKey, clock).path("/member/" + userId + "/watchlist").build();
        Response response = createAndExecuteUserAuthedRequest(HTTP_METHOD_GET, url, accessToken);
        String responseString = response.body().string();
        ApiFilmsResponse apiFilmsResponse = gson.fromJson(responseString, ApiFilmsResponse.class);
        List<Film> films = new ArrayList<>(apiFilmsResponse.filmSummaries.size());
        for (ApiFilmSummary filmSummary : apiFilmsResponse.filmSummaries) {
            Film film = filmConverter.convert(filmSummary);
            films.add(film);
        }
        return films;
    }

    private Response createAndExecuteUserAuthedRequest(String httpMethod, String url, String accessToken) throws IOException {
        Request.Builder builder = new Request.Builder()
                .url(url + "&signature=" + generateSignature(httpMethod, url, ""))
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer " + accessToken);

        Request request = builder.build();
        return okHttpClient.newCall(request).execute();
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
