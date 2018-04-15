package com.ataulm.whatsnext.letterboxd

import io.reactivex.Observable
import retrofit2.http.*

interface LetterboxdApi {

    @FormUrlEncoded
    @POST("auth/token")
    fun accessToken(@Field("username") username: String,
                    @Field("password") password: String,
                    @Field("grant_type") grantType: String = "password"): Observable<AccessToken>

    @FormUrlEncoded
    @POST("auth/token")
    fun refreshAccessToken(@Field("refresh_token") refreshToken: String,
                           @Field("grant_type") grantType: String = "refresh_token"): Observable<AccessToken>

    @GET("search")
    fun search(@Query("input") input: String): Observable<SearchResponse>

    @GET("film/{id}")
    fun film(@Path("id") id: String): Observable<Film>
}

data class AccessToken(val access_token: String, val refresh_token: String, val expires_in: Long)

data class SearchResponse(val next: String, val items: List<AbstractSearchItem>) {

    open class AbstractSearchItem(open val type: String, open val score: Int)

    data class FilmSearchItem(override val type: String, override val score: Int, val film: FilmSummary) : AbstractSearchItem(type, score)
}

data class FilmSummary(val name: String,
                       val releaseYear: Int,
                       val poster: Image?,
                       val links: List<Link>,
                       val relationships: List<MemberFilmRelationship>)

data class Image(val sizes: List<Size>) {

    data class Size(val width: Int, val height: Int, val url: String)
}

data class Link(val type: String, val id: String, val url: String)

data class Genre(val id: String, val name: String)

data class Film(val id: String,
                val name: String,
                val releaseYear: Int,
                val tagline: String?,
                val description: String?,
                val runTime: Int?,
                val poster: Image?,
                val backdrop: Image?,
                val backdropFocalPoint: Double?,
                val genres: List<Genre>?,
                val contributions: List<FilmContributions>,
                val links: List<Link>)

data class FilmContributions(val type: String, val contributors: List<ContributorSummary>) {

    open class ContributorSummary(open val id: String, open val name: String)

    data class Actor(override val id: String, override val name: String, val characterName: String) : ContributorSummary(id, name)

}

data class MemberFilmRelationship(val member: MemberSummary, val relationship: FilmRelationship)

data class MemberSummary(val id: String, val username: String, val avatar: Image?)

data class FilmRelationship(val watched: Boolean,
                            val whenWatched: String?,
                            val liked: Boolean,
                            val favorited: Boolean,
                            val inWatchList: Boolean,
                            val whenAddedToWatchlist: String?,
                            val rating: Double?)
