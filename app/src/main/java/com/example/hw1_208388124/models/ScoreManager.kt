package com.example.hw1_208388124.models

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object ScoreManager {
    private const val PREFS_NAME = "game_scores"
    private const val SCORES_KEY = "scores"
    private var scores = mutableListOf<ScoreRecord>()
    private lateinit var sharedPrefs: SharedPreferences

    private val gson = GsonBuilder()
        .registerTypeAdapter(LocalDate::class.java, object : TypeAdapter<LocalDate>() {
            override fun write(out: JsonWriter, value: LocalDate?) {
                if (value == null) {
                    out.nullValue()
                } else {
                    out.value(value.format(DateTimeFormatter.ISO_LOCAL_DATE))
                }
            }

            override fun read(`in`: JsonReader): LocalDate? {
                try {
                    val dateStr = `in`.nextString()
                    return if (dateStr.isNullOrEmpty()) LocalDate.now()
                    else LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE)
                } catch (e: Exception) {
                    return LocalDate.now()
                }
            }
        })
        .create()

    fun init(context: Context) {
        sharedPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        try {
            loadScores()
        } catch (e: Exception) {
            scores = mutableListOf()
            sharedPrefs.edit().clear().apply()
        }
    }

    private fun loadScores() {
        try {
            val scoresJson = sharedPrefs.getString(SCORES_KEY, "[]")
            val type = object : TypeToken<MutableList<ScoreRecord>>() {}.type
            val loadedScores = gson.fromJson<MutableList<ScoreRecord>>(scoresJson, type)
            scores = loadedScores ?: mutableListOf()
        } catch (e: Exception) {
            scores = mutableListOf()
        }
    }

    private fun saveScores() {
        try {
            val scoresJson = gson.toJson(scores)
            sharedPrefs.edit().putString(SCORES_KEY, scoresJson).apply()
        } catch (e: Exception) {
        }
    }

    fun addScore(score: ScoreRecord) {
        scores.add(score)
        scores.sortByDescending { it.score }
        while (scores.size > 10) {
            scores.removeAt(scores.lastIndex)
        }
        saveScores()
    }

    fun getTopScores(): List<ScoreRecord> = scores.toList()
}