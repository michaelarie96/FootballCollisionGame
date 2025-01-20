package com.example.hw1_208388124.models

import java.time.LocalDate

data class ScoreRecord(
    val playerName: String,
    val score: Int,
    val date: LocalDate,
    val latitude: Double,
    val longitude: Double
) {
    class Builder(
        var playerName: String = "",
        var score: Int = 0,
        var date: LocalDate = LocalDate.now(),
        var latitude: Double = 0.0,
        var longitude: Double = 0.0
    ) {
        fun playerName(name: String) = apply { this.playerName = name }
        fun score(score: Int) = apply { this.score = score }
        fun date(date: LocalDate) = apply { this.date = date }
        fun location(lat: Double, lon: Double) = apply {
            this.latitude = lat
            this.longitude = lon
        }

        fun build() = ScoreRecord(
            playerName,
            score,
            date,
            latitude,
            longitude
        )
    }
}
