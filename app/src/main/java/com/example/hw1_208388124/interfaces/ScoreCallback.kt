package com.example.hw1_208388124.interfaces

import com.example.hw1_208388124.models.ScoreRecord

interface ScoreCallback {
    fun onScoreClicked(score: ScoreRecord, position: Int)
}