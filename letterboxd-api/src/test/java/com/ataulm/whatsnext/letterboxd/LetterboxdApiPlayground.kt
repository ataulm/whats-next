package com.ataulm.whatsnext.letterboxd

import com.ataulm.support.Clock2
import org.junit.Test
import java.io.File
import java.io.FileReader
import java.util.*

class LetterboxdApiPlayground {

    @Test
    fun film() {
        val response = signedInApi().film("1Wow").blockingFirst()
        print(response)
    }

    @Test
    fun search() {
        val response = signedInApi().search("iron giant").blockingFirst()
        print(response)
    }

    private fun signedInApi(): LetterboxdApi {
        val tokenStore = object : TokenStore {

            var token: AccessToken? = null

            override fun store(token: AccessToken) {
                this.token = token
            }

            override fun clear() {
                token = null
            }

            override fun token(): AccessToken? {
                return token
            }
        }

        val properties = Properties()
        properties.load(FileReader(File("letterboxd.properties")))

        val api = LetterboxdApiFactory(
                tokenStore,
                properties.getProperty("apiKey"),
                properties.getProperty("apiSecret"),
                Clock2()
        ).create()

        val accessToken = api.accessToken(properties.getProperty("username"), properties.getProperty("password")).blockingFirst()
        tokenStore.store(accessToken)
        return api
    }
}