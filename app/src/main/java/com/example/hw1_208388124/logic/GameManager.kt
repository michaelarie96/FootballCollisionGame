package com.example.hw1_208388124.logic

import com.example.hw1_208388124.R
import com.example.hw1_208388124.utillities.SingleSoundPlayer

class GameManager(
    private val lifeCount: Int = 3,
    private val soundPlayer: SingleSoundPlayer? = null
) {
    private var currentPosition = 2

    private var lives = lifeCount
    private val maxLives = 3

    private var isPlaying = true

    val isGameOver: Boolean
        get() = lives <= 0

    private val lanes = Array(5) { BooleanArray(8) { false } }
    private val barriers = Array(5) { BooleanArray(8) { false } }

    private var currentTick = 0

    private val spawnFrequency = 3

    var hasCollided = false
    private var isGameOverCollision = false
    private var isBarrierCollision = false

    private var distanceInYards = 0;

    init {
        spawnNewBlock()
    }

    fun getCurrentPosition(): Int = currentPosition
    fun getLives(): Int = lives
    fun getDistance(): Int = distanceInYards

    fun updateBlockPositions() {
        if (!isPlaying) return

        checkCollisions()

        moveObjectsDown()

        distanceInYards += 5

        handleSpawning()
    }

    private fun checkCollisions() {
        for (lane in 0..4) {
            if (isPlayerInLane(lane)) {
                if (barriers[lane][7]) {
                    collectBarrier()
                }
                else if (lanes[lane][7]) {
                    handleCollision()
                }
            }
        }
    }

    private fun moveObjectsDown() {
        for (lane in 0..4) {
            for (pos in 7 downTo 1) {
                lanes[lane][pos] = lanes[lane][pos - 1]
                barriers[lane][pos] = barriers[lane][pos - 1]
            }
            lanes[lane][0] = false
            barriers[lane][0] = false
        }
    }

    private fun handleSpawning() {
        currentTick++
        if (currentTick % spawnFrequency == 0) {
            if (Math.random() < 0.2 && lives < maxLives) { // 20% chance and player needs lives
                spawnNewBarrier()
            }
            spawnNewBlock()
        }
    }

    private fun spawnNewBlock() {
        val availableLanes = (0..4).filter { !barriers[it][0] }.toList()
        if (availableLanes.isNotEmpty()) {
            val randomLane = availableLanes.random()
            lanes[randomLane][0] = true
        }
    }

    private fun spawnNewBarrier() {
        val availableLanes = (0..4).filter { !lanes[it][0] }.toList()
        if (availableLanes.isNotEmpty()) {
            val randomLane = availableLanes.random()
            barriers[randomLane][0] = true
        }
    }

    fun isBlockVisible(lane: Int, position: Int): Boolean {
        return lanes[lane][position]
    }

    fun isBarrierVisible(lane: Int, position: Int): Boolean {
        return barriers[lane][position]
    }

    fun moveLeft(): Boolean {
        if (currentPosition > 0 && isPlaying) {
            currentPosition--
            return true
        }
        return false
    }

    fun moveRight(): Boolean {
        if (currentPosition < 4 && isPlaying) {
            currentPosition++
            return true
        }
        return false
    }

    private fun collectBarrier() {
        if (lives < maxLives) {
            lives++
            hasCollided = true
            isBarrierCollision = true
            soundPlayer?.playSound(R.raw.yeah)
        }
    }

    private fun handleCollision() {
        if (isPlaying) {
            lives--
            hasCollided = true
            isGameOverCollision = (lives <= 0)
            soundPlayer?.playSound(R.raw.crash)
            if (isGameOverCollision)
                isPlaying = false
        }
    }

    fun isGameOverCollision(): Boolean = isGameOverCollision
    fun isBarrierCollision(): Boolean = isBarrierCollision

    fun resetCollisionFlag() {
        hasCollided = false
        isBarrierCollision = false
    }

    private fun isPlayerInLane(lane: Int): Boolean {
        return currentPosition == lane
    }

    fun resetGame() {
        currentPosition = 2
        lives = lifeCount
        isPlaying = true
        hasCollided = false
        isGameOverCollision = false
        isBarrierCollision = false
        distanceInYards = 0

        for (lane in lanes.indices) {
            for (pos in lanes[lane].indices) {
                lanes[lane][pos] = false
                barriers[lane][pos] = false
            }
        }
    }
}