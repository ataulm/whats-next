package com.ataulm.whatsnext.letterboxd

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.lang.reflect.Type

internal class AbstractSearchItemDeserializer : JsonDeserializer<SearchResponse.AbstractSearchItem> {

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): SearchResponse.AbstractSearchItem {
        val type = json.asJsonObject.get("type").asString
        val score = json.asJsonObject.get("score").asInt
        return if ("FilmSearchItem" == type) {
            val deserialize = context.deserialize<FilmSummary>(json.asJsonObject.getAsJsonObject("film"), FilmSummary::class.java)
            SearchResponse.FilmSearchItem(type, score, deserialize)
        } else {
            SearchResponse.AbstractSearchItem(type, score)
        }
    }
}
