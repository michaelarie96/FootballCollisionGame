package com.example.hw1_208388124

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.hw1_208388124.logic.GameManager
import com.example.hw1_208388124.utillities.SignalManager
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var main_IMG_lives: Array<AppCompatImageView>

    private lateinit var main_IMG_players: Array<AppCompatImageView>

    private lateinit var main_BTN_left: MaterialButton

    private lateinit var main_BTN_right: MaterialButton

    private lateinit var main_IMG_blocks: Array<Array<AppCompatImageView>>

    private lateinit var gameManager: GameManager

    private var timerJob: Job? = null
    private val GAME_TICK = 1000L // 1 second

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViews()
        gameManager = GameManager(main_IMG_lives.size)
        initViews()
        startGame()
    }

    private fun findViews() {
        main_IMG_lives = arrayOf(
            findViewById(R.id.main_IMG_life1),
            findViewById(R.id.main_IMG_life2),
            findViewById(R.id.main_IMG_life3)
        )

        main_IMG_players = arrayOf(
            findViewById(R.id.main_IMG_left_player),
            findViewById(R.id.main_IMG_middle_player),
            findViewById(R.id.main_IMG_right_player)
        )

        main_IMG_blocks = arrayOf(
            arrayOf(
                findViewById(R.id.main_IMG_left_block1),
                findViewById(R.id.main_IMG_left_block2),
                findViewById(R.id.main_IMG_left_block3),
                findViewById(R.id.main_IMG_left_block4),
                findViewById(R.id.main_IMG_left_block5),
                findViewById(R.id.main_IMG_left_block6)
            ),
            arrayOf(
                findViewById(R.id.main_IMG_middle_block1),
                findViewById(R.id.main_IMG_middle_block2),
                findViewById(R.id.main_IMG_middle_block3),
                findViewById(R.id.main_IMG_middle_block4),
                findViewById(R.id.main_IMG_middle_block5),
                findViewById(R.id.main_IMG_middle_block6)
            ),
            arrayOf(
                findViewById(R.id.main_IMG_right_block1),
                findViewById(R.id.main_IMG_right_block2),
                findViewById(R.id.main_IMG_right_block3),
                findViewById(R.id.main_IMG_right_block4),
                findViewById(R.id.main_IMG_right_block5),
                findViewById(R.id.main_IMG_right_block6)
            )
        )

        main_BTN_left = findViewById(R.id.main_BTN_left)
        main_BTN_right = findViewById(R.id.main_BTN_right)

    }

    private fun initViews() {
        updatePlayerVisibility()
        main_BTN_left.setOnClickListener { movePlayer(-1) }
        main_BTN_right.setOnClickListener { movePlayer(1) }
    }

    private fun startGame() {
        timerJob?.cancel()

        timerJob = lifecycleScope.launch {
            while (isActive) {
                gameManager.updateBlockPositions()
                updateBlocksVisibility()
                refreshUI()
                delay(GAME_TICK)
            }
        }
    }

    private fun stopGame() {
        timerJob?.cancel()
        timerJob = null
    }

    private fun updateBlocksVisibility() {
        for (lane in 0..2) {
            for (position in 0..5) {
                main_IMG_blocks[lane][position].visibility =
                    if (gameManager.isBlockVisible(lane, position)) View.VISIBLE
                    else View.INVISIBLE
            }
        }
    }

    private fun movePlayer(direction: Int) {
        val moved = if (direction < 0) {
            gameManager.moveLeft()
        } else {
            gameManager.moveRight()
        }

        if (moved)
            updatePlayerVisibility()
    }

    private fun updatePlayerVisibility() {
        main_IMG_players.forEach { it.visibility = View.INVISIBLE }
        main_IMG_players[gameManager.getCurrentPosition()].visibility = View.VISIBLE
    }

    private fun refreshUI() {
        if (gameManager.hasCollided) {
            SignalManager.getInstance().vibrate()
            if (!gameManager.isGameOverCollision())
                SignalManager.getInstance().toast("Lost the ball! Careful")
            gameManager.resetCollisionFlag()
        }

        if (gameManager.getLives() < 3) {
            for (i in gameManager.getLives() until main_IMG_lives.size) {
                main_IMG_lives[i].visibility = View.INVISIBLE
            }
        }

        if (gameManager.isGameOver) {
            gameOver()
        }
    }

    private fun gameOver() {
        stopGame()
        SignalManager.getInstance().vibrate()
        SignalManager.getInstance().toast("Game Over!")

        lifecycleScope.launch {
            delay(2000)
            restartGame()
        }
    }

    private fun restartGame() {
        gameManager.resetGame()

        for (life in main_IMG_lives) {
            life.visibility = View.VISIBLE
        }

        for (lane in main_IMG_blocks) {
            for (block in lane) {
                block.visibility = View.INVISIBLE
            }
        }

        updatePlayerVisibility()
        startGame()
    }

    override fun onPause() {
        super.onPause()
        stopGame()
    }

    override fun onResume() {
        super.onResume()
        startGame()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopGame()
    }
}