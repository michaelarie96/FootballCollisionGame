package com.example.hw1_208388124.logic

class GameManager(private val lifeCount: Int = 3) {
    private var currentPosition = 1

    private var lives = lifeCount

    private var isPlaying = true

    val isGameOver: Boolean
        get() = lives <= 0

    private val lanes = Array(3) { BooleanArray(6) { false } }

    private var currentTick = 0

    private val spawnFrequency = 3

    var hasCollided = false

    private var isGameOverCollision = false

    init {
        spawnNewBlock()
    }

    fun getCurrentPosition(): Int = currentPosition

    fun getLives(): Int = lives

    fun updateBlockPositions() {
        if (!isPlaying) return

        for (lane in 0..2) {
            if (lanes[lane][5] && isPlayerInLane(lane)) {
                handleCollision()
            }

            for (pos in 5 downTo 1) {
                lanes[lane][pos] = lanes[lane][pos - 1]
            }
            lanes[lane][0] = false
        }

        currentTick++
        if (currentTick % spawnFrequency == 0) {
            spawnNewBlock()
        }
    }

    private fun spawnNewBlock() {
        val randomLane = (0..2).random()
        lanes[randomLane][0] = true
    }

    fun isBlockVisible(lane: Int, position: Int): Boolean {
        return lanes[lane][position]
    }

    fun moveLeft(): Boolean {
        if (currentPosition > 0 && isPlaying) {
            currentPosition--
            return true
        }
        return false
    }

    fun moveRight(): Boolean {
        if (currentPosition < 2 && isPlaying) {
            currentPosition++
            return true
        }
        return false
    }

    fun handleCollision() {
        if (isPlaying) {
            lives--
            hasCollided = true
            isGameOverCollision = (lives <= 0)
            if (isGameOverCollision)
                isPlaying = false
        }
    }

    fun isGameOverCollision(): Boolean = isGameOverCollision

    fun resetCollisionFlag() {
        hasCollided = false
    }

    fun isPlayerInLane(lane: Int): Boolean {
        return currentPosition == lane
    }

    fun resetGame() {
        currentPosition = 1
        lives = lifeCount
        isPlaying = true
        hasCollided = false
        isGameOverCollision = false
    }
}