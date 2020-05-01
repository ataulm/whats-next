package com.ataulm.whatsnext.api

import com.ataulm.whatsnext.*
import java.io.IOException
import java.util.concurrent.TimeUnit

class FakeLetterboxd : Letterboxd {

    @Throws(IOException::class)
    override fun fetchAccessToken(username: String, password: String): Token {
        return createFakeToken()
    }

    @Throws(IOException::class)
    override fun refreshAccessToken(refreshToken: String): Token {
        return createFakeToken()
    }

    private fun createFakeToken(): Token {
        return Token("", "", TimeUnit.DAYS.toMillis((365 * 100).toLong()))
    }

    @Throws(IOException::class)
    override fun search(searchTerm: String): List<FilmSummary> {
        return listOf(ironGiantSummary())
    }

    @Throws(IOException::class)
    override fun film(letterboxdId: String, accessToken: String): Film {
        return ironGiantFilm()
    }

    private fun ironGiantFilm(): Film {
        return Film(
                ironGiantSummary(),
                FilmRelationship(false, false, false, 4.5 / 5)
        )
    }

    private fun ironGiantIds() = Ids("1Wow", "tt0129167", "10386")

    private fun ironGiantSummary() = FilmSummary(
            ironGiantIds(),
            "The Iron Giant",
            "1999",
            86,
            "It came from outer space!",
            "In the small town of Rockwell, Maine in October 1957, a giant metal machine befriends a nine-year-old boy and ultimately finds its humanity by unselfishly saving people from their own fears and prejudices.",
            ironGiantPoster(),
            ironGiantBackdrop(),
            ironGiantGenres(),
            ironGiantCast(),
            ironGiantCrew()
    )

    private fun ironGiantPoster() = Images(
            listOf(
                    Image(70, 105, "http://skyfall.a.ltrbxd.com/resized/film-poster/4/6/2/8/0/46280-the-iron-giant-0-70-0-105-crop.jpg?k=502cb008bb"),
                    Image(100, 150, "http://oblivion.a.ltrbxd.com/resized/sm/upload/e1/sm/uk/vm/4UjYQnGhdDUir1Jll3vPFMmGyCv-0-100-0-150-crop.jpg?k=6ec66573fb"),
                    Image(140, 210, "http://primer.a.ltrbxd.com/resized/sm/upload/e1/sm/uk/vm/4UjYQnGhdDUir1Jll3vPFMmGyCv-0-140-0-210-crop.jpg?k=ae380506c7"),
                    Image(200, 300, "http://skyfall.a.ltrbxd.com/resized/sm/upload/e1/sm/uk/vm/4UjYQnGhdDUir1Jll3vPFMmGyCv-0-200-0-300-crop.jpg?k=74fc07310e"),
                    Image(250, 375, "http://zardoz.a.ltrbxd.com/resized/sm/upload/e1/sm/uk/vm/4UjYQnGhdDUir1Jll3vPFMmGyCv-0-250-0-375-crop.jpg?k=b9f33f6520"),
                    Image(300, 450, "http://primer.a.ltrbxd.com/resized/sm/upload/e1/sm/uk/vm/4UjYQnGhdDUir1Jll3vPFMmGyCv-0-300-0-450-crop.jpg?k=b9f33f6520"),
                    Image(400, 600, "http://zardoz.a.ltrbxd.com/resized/sm/upload/e1/sm/uk/vm/4UjYQnGhdDUir1Jll3vPFMmGyCv-0-400-0-600-crop.jpg?k=7e781c7097"),
                    Image(500, 750, "http://skyfall.a.ltrbxd.com/resized/sm/upload/e1/sm/uk/vm/4UjYQnGhdDUir1Jll3vPFMmGyCv-0-500-0-750-crop.jpg?k=3e55356fd1")

            )
    )

    private fun ironGiantBackdrop() = Images(
            listOf(
                    Image(960, 540, "https://a.ltrbxd.com/resized/sm/upload/xq/b9/aq/52/the-iron-giant-960-960-540-540-crop-000000.jpg?k=bbef331a7e"),
                    Image(1200, 675, "https://a.ltrbxd.com/resized/sm/upload/xq/b9/aq/52/the-iron-giant-1200-1200-675-675-crop-000000.jpg?k=efb0ae3ec5")
            )
    )

    private fun ironGiantGenres(): List<String> {
        return listOf("Family", "Animation", "Science Fiction", "Fantasy", "Adventure")
    }

    private fun ironGiantCast() = listOf(
            Actor("Hogarth Hughes (voice)", Person("6uzf", "Eli Marienthal")),
            Actor("Annie Hughes (voice)", Person("xch", "Jennifer Aniston")),
            Actor("The Iron Giant (voice)", Person("16fZ", "Vin Diesel")),
            Actor("Kent Mansley (voice)", Person("43UL", "Christopher McDonald")),
            Actor("Dean McCoppin (voice)", Person("3VwB", "Harry Connick Jr.")),
            Actor("General Rogard (voice)", Person("3tql", "John Mahoney")),
            Actor("Mrs. Lynley Tensedge (voice)", Person("3OAV", "Cloris Leachman")),
            Actor("Foreman Marv Loach / Floyd Turbeaux (voice)", Person("3sPz", "James Gammon")),
            Actor("Earl Stutz (voice)", Person("3kA7", "M. Emmet Walsh")),
            Actor("Additional Voices", Person("415r", "Jack Angel")),
            Actor("Additional Voices", Person("4z3p", "Bob Bergen")),
            Actor("Additional Voices", Person("6fGb", "Mary Kay Bergman")),
            Actor("Additional Voices", Person("9irf", "Michael Bird")),
            Actor("Additional Voices", Person("9irp", "Devon Cole Borisoff")),
            Actor("Additional Voices", Person("3JIv", "Rodger Bumpass")),
            Actor("Additional Voices", Person("3kSF", "Robert Clotworthy")),
            Actor("Additional Voices", Person("6mxP", "Jennifer Darling")),
            Actor("Additional Voices (voice)", Person("fTMV", "Zack Eginton")),
            Actor("Additional Voices (voice)", Person("5CJF", "Paul Eiding")),
            Actor("Additional Voices (voice)", Person("3VM5", "Bill Farmer")),
            Actor("Additional Voices (voice)", Person("3V9x", "Charles Howerton")),
            Actor("Additional Voices (voice)", Person("i3mR", "Ollie Johnston")),
            Actor("Additional Voices (voice)", Person("7qh9", "Sherry Lynn")),
            Actor("Additional Voices (voice)", Person("6wcx", "Mickie McGowan")),
            Actor("Additional Voices (voice)", Person("9izj", "Ryan O'Donohue")),
            Actor("Additional Voices (voice)", Person("217j", "Phil Proctor")),
            Actor("Additional Voices (voice)", Person("i3nb", "Frank Thomas")),
            Actor("Additional Voices (voice)", Person("bLgp", "Patti Tippo")),
            Actor("Additional Voices (voice)", Person("6ww3", "Brian Tochi"))
    )

    private fun ironGiantCrew() = listOf(
            Contributor("Director", Person("1ouX", "Brad Bird")),
            Contributor("Producer", Person("40UN", "Pete Townshend")),
            Contributor("Producer", Person("fISt", "Allison Abbate")),
            Contributor("Producer", Person("2qPZ", "Des McAnuff")),
            Contributor("Writer", Person("iDJn", "Ted Hughes")),
            Contributor("Writer", Person("1ouX", "Brad Bird")),
            Contributor("Writer", Person("1ID1", "Tim McCanlies")),
            Contributor("Composer", Person("5acN", "Michael Kamen")),
            Contributor("Cinematography", Person("iDJx", "Steven Wilzbach")),
            Contributor("Editor", Person("iqMX", "Darren T. Holmes"))
    )
}
