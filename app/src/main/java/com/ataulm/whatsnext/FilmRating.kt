package com.ataulm.whatsnext

enum class FilmRating(private val value: Float) {

    UNRATED(0f),
    HALF(0.5f),
    ONE(1.toFloat()),
    ONE_HALF(1.5f),
    TWO(2.toFloat()),
    TWO_HALF(2.5f),
    THREE(3.toFloat()),
    THREE_HALF(3.5f),
    FOUR(4.toFloat()),
    FOUR_HALF(4.5f),
    FIVE(5.toFloat());

    fun toFloat() = value

    companion object {

        fun fromFloat(float: Float?) = values().find { it.value == float } ?: UNRATED
    }
}
