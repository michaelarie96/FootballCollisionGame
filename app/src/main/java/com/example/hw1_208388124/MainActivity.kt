package com.example.hw1_208388124

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.hw1_208388124.interfaces.TiltCallback
import com.example.hw1_208388124.logic.GameManager
import com.example.hw1_208388124.models.ScoreManager
import com.example.hw1_208388124.models.ScoreRecord
import com.example.hw1_208388124.utillities.SignalManager
import com.example.hw1_208388124.utillities.SingleSoundPlayer
import com.example.hw1_208388124.utillities.TiltDetector
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import android.net.Uri
import android.provider.Settings
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {
    private lateinit var main_IMG_lives: Array<AppCompatImageView>
    private lateinit var main_IMG_players: Array<AppCompatImageView>
    private lateinit var main_BTN_left: MaterialButton
    private lateinit var main_BTN_right: MaterialButton
    private lateinit var main_IMG_blocks: Array<Array<AppCompatImageView>>
    private lateinit var main_IMG_barriers: Array<Array<AppCompatImageView>>
    private lateinit var main_TXT_distance: MaterialTextView

    private lateinit var gameManager: GameManager
    private lateinit var soundPlayer: SingleSoundPlayer
    private var gameTiltDetector: TiltDetector? = null

    private var timerJob: Job? = null
    private var currentGameSpeed: Long = 1000L
    private val MIN_GAME_SPEED = 200L
    private val MAX_GAME_SPEED = 2000L
    private val SPEED_CHANGE_STEP = 200L

    private var gameMode: String = MenuActivity.GAME_MODE_SLOW

    private var currentLatitude: Double = 0.0
    private var currentLongitude: Double = 0.0
    private val LOCATION_PERMISSION_REQUEST_CODE = 1234

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkLocationPermission()

        gameMode =
            intent.getStringExtra(MenuActivity.EXTRA_GAME_MODE) ?: MenuActivity.GAME_MODE_SLOW
        currentGameSpeed = when (gameMode) {
            MenuActivity.GAME_MODE_FAST -> 500L
            MenuActivity.GAME_MODE_SENSORS -> 750L
            else -> 1000L
        }

        soundPlayer = SingleSoundPlayer(this)
        findViews()
        gameManager = GameManager(main_IMG_lives.size, soundPlayer)
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
            findViewById(R.id.main_IMG_far_left_player),
            findViewById(R.id.main_IMG_left_player),
            findViewById(R.id.main_IMG_middle_player),
            findViewById(R.id.main_IMG_right_player),
            findViewById(R.id.main_IMG_far_right_player)
        )

        main_IMG_blocks = arrayOf(
            arrayOf(
                findViewById(R.id.main_IMG_far_left_block1),
                findViewById(R.id.main_IMG_far_left_block2),
                findViewById(R.id.main_IMG_far_left_block3),
                findViewById(R.id.main_IMG_far_left_block4),
                findViewById(R.id.main_IMG_far_left_block5),
                findViewById(R.id.main_IMG_far_left_block6),
                findViewById(R.id.main_IMG_far_left_block7),
                findViewById(R.id.main_IMG_far_left_block8)
            ),
            arrayOf(
                findViewById(R.id.main_IMG_left_block1),
                findViewById(R.id.main_IMG_left_block2),
                findViewById(R.id.main_IMG_left_block3),
                findViewById(R.id.main_IMG_left_block4),
                findViewById(R.id.main_IMG_left_block5),
                findViewById(R.id.main_IMG_left_block6),
                findViewById(R.id.main_IMG_left_block7),
                findViewById(R.id.main_IMG_left_block8)
            ),
            arrayOf(
                findViewById(R.id.main_IMG_middle_block1),
                findViewById(R.id.main_IMG_middle_block2),
                findViewById(R.id.main_IMG_middle_block3),
                findViewById(R.id.main_IMG_middle_block4),
                findViewById(R.id.main_IMG_middle_block5),
                findViewById(R.id.main_IMG_middle_block6),
                findViewById(R.id.main_IMG_middle_block7),
                findViewById(R.id.main_IMG_middle_block8)
            ),
            arrayOf(
                findViewById(R.id.main_IMG_right_block1),
                findViewById(R.id.main_IMG_right_block2),
                findViewById(R.id.main_IMG_right_block3),
                findViewById(R.id.main_IMG_right_block4),
                findViewById(R.id.main_IMG_right_block5),
                findViewById(R.id.main_IMG_right_block6),
                findViewById(R.id.main_IMG_right_block7),
                findViewById(R.id.main_IMG_right_block8)
            ),
            arrayOf(
                findViewById(R.id.main_IMG_far_right_block1),
                findViewById(R.id.main_IMG_far_right_block2),
                findViewById(R.id.main_IMG_far_right_block3),
                findViewById(R.id.main_IMG_far_right_block4),
                findViewById(R.id.main_IMG_far_right_block5),
                findViewById(R.id.main_IMG_far_right_block6),
                findViewById(R.id.main_IMG_far_right_block7),
                findViewById(R.id.main_IMG_far_right_block8)
            )
        )

        main_IMG_barriers = arrayOf(
            arrayOf(
                findViewById(R.id.main_IMG_far_left_barrier1),
                findViewById(R.id.main_IMG_far_left_barrier2),
                findViewById(R.id.main_IMG_far_left_barrier3),
                findViewById(R.id.main_IMG_far_left_barrier4),
                findViewById(R.id.main_IMG_far_left_barrier5),
                findViewById(R.id.main_IMG_far_left_barrier6),
                findViewById(R.id.main_IMG_far_left_barrier7),
                findViewById(R.id.main_IMG_far_left_barrier8)
            ),
            arrayOf(
                findViewById(R.id.main_IMG_left_barrier1),
                findViewById(R.id.main_IMG_left_barrier2),
                findViewById(R.id.main_IMG_left_barrier3),
                findViewById(R.id.main_IMG_left_barrier4),
                findViewById(R.id.main_IMG_left_barrier5),
                findViewById(R.id.main_IMG_left_barrier6),
                findViewById(R.id.main_IMG_left_barrier7),
                findViewById(R.id.main_IMG_left_barrier8)
            ),
            arrayOf(
                findViewById(R.id.main_IMG_middle_barrier1),
                findViewById(R.id.main_IMG_middle_barrier2),
                findViewById(R.id.main_IMG_middle_barrier3),
                findViewById(R.id.main_IMG_middle_barrier4),
                findViewById(R.id.main_IMG_middle_barrier5),
                findViewById(R.id.main_IMG_middle_barrier6),
                findViewById(R.id.main_IMG_middle_barrier7),
                findViewById(R.id.main_IMG_middle_barrier8)
            ),
            arrayOf(
                findViewById(R.id.main_IMG_right_barrier1),
                findViewById(R.id.main_IMG_right_barrier2),
                findViewById(R.id.main_IMG_right_barrier3),
                findViewById(R.id.main_IMG_right_barrier4),
                findViewById(R.id.main_IMG_right_barrier5),
                findViewById(R.id.main_IMG_right_barrier6),
                findViewById(R.id.main_IMG_right_barrier7),
                findViewById(R.id.main_IMG_right_barrier8)
            ),
            arrayOf(
                findViewById(R.id.main_IMG_far_right_barrier1),
                findViewById(R.id.main_IMG_far_right_barrier2),
                findViewById(R.id.main_IMG_far_right_barrier3),
                findViewById(R.id.main_IMG_far_right_barrier4),
                findViewById(R.id.main_IMG_far_right_barrier5),
                findViewById(R.id.main_IMG_far_right_barrier6),
                findViewById(R.id.main_IMG_far_right_barrier7),
                findViewById(R.id.main_IMG_far_right_barrier8)
            )
        )

        main_TXT_distance = findViewById(R.id.main_TXT_distance)
        main_BTN_left = findViewById(R.id.main_BTN_left)
        main_BTN_right = findViewById(R.id.main_BTN_right)

    }

    private fun initViews() {
        updatePlayerVisibility()

        when (gameMode) {
            MenuActivity.GAME_MODE_SENSORS -> {
                main_BTN_left.visibility = View.GONE
                main_BTN_right.visibility = View.GONE
                initTiltControls()
            }

            else -> {
                main_BTN_left.visibility = View.VISIBLE
                main_BTN_right.visibility = View.VISIBLE
                main_BTN_left.setOnClickListener { movePlayer(-1) }
                main_BTN_right.setOnClickListener { movePlayer(1) }
            }
        }
    }

    private fun startGame() {
        timerJob?.cancel()

        timerJob = lifecycleScope.launch {
            while (isActive) {
                gameManager.updateBlockPositions()
                updateBlocksVisibility()
                refreshUI()
                delay(currentGameSpeed)
            }
        }
    }

    private fun stopGame() {
        timerJob?.cancel()
        timerJob = null
    }

    private fun initTiltControls() {
        gameTiltDetector = TiltDetector(this, object : TiltCallback {
            override fun onTiltLeft() {
                movePlayer(-1)
            }

            override fun onTiltRight() {
                movePlayer(1)
            }

            override fun onTiltForward() {
                changeGameSpeed(-SPEED_CHANGE_STEP)
            }

            override fun onTiltBackward() {
                changeGameSpeed(SPEED_CHANGE_STEP)
            }
        })
    }

    private fun changeGameSpeed(delta: Long) {
        val newSpeed = (currentGameSpeed + delta).coerceIn(MIN_GAME_SPEED, MAX_GAME_SPEED)
        if (newSpeed != currentGameSpeed) {
            currentGameSpeed = newSpeed
        }
    }

    private fun updateBlocksVisibility() {
        for (lane in 0..4) {
            for (position in 0..7) {
                main_IMG_blocks[lane][position].visibility =
                    if (gameManager.isBlockVisible(lane, position)) View.VISIBLE
                    else View.INVISIBLE

                main_IMG_barriers[lane][position].visibility =
                    if (gameManager.isBarrierVisible(lane, position)) View.VISIBLE
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
        updateDistanceDisplay()

        if (gameManager.hasCollided) {
            SignalManager.getInstance().vibrate()
            if (gameManager.isBarrierCollision()) {
                SignalManager.getInstance().toast("Extra life collected!")
            } else if (!gameManager.isGameOverCollision()) {
                SignalManager.getInstance().toast("Lost the ball! Careful")
            }
            gameManager.resetCollisionFlag()
        }

        for (i in main_IMG_lives.indices) {
            main_IMG_lives[i].visibility =
                if (i < gameManager.getLives()) View.VISIBLE else View.INVISIBLE
        }

        if (gameManager.isGameOver) {
            gameOver()
        }
    }

    private fun updateDistanceDisplay() {
        main_TXT_distance.text = "${gameManager.getDistance()} YDS"
    }

    private fun checkLocationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                getCurrentLocation { }
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                MaterialAlertDialogBuilder(this)
                    .setTitle("Location Permission Needed")
                    .setMessage("This app needs location permission to record where high scores are achieved.")
                    .setPositiveButton("OK") { _, _ ->
                        requestLocationPermission()
                    }
                    .create()
                    .show()
            }

            else -> {
                requestLocationPermission()
            }
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation { }
                } else {
                    MaterialAlertDialogBuilder(this)
                        .setTitle("Permission Denied")
                        .setMessage("Location permission was denied. High scores will be recorded without location data. You can enable location permission in Settings.")
                        .setPositiveButton("Settings") { _, _ ->
                            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                data = Uri.fromParts("package", packageName, null)
                                startActivity(this)
                            }
                        }
                        .setNegativeButton("OK", null)
                        .create()
                        .show()
                }
            }
        }
    }

    private fun getCurrentLocation(onComplete: () -> Unit) {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // If permission not granted, use a default location
            currentLatitude = 0.0
            currentLongitude = 0.0
            onComplete()
            return
        }

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { location ->
                if (location != null) {
                    currentLatitude = location.latitude
                    currentLongitude = location.longitude
                }
                onComplete()
            }
            .addOnFailureListener {
                currentLatitude = 0.0
                currentLongitude = 0.0
                onComplete()
            }
    }

    private fun gameOver() {
        stopGame()
        SignalManager.getInstance().vibrate()
        SignalManager.getInstance().toast("Game Over!")

        getCurrentLocation {
            val dialogView = layoutInflater.inflate(R.layout.dialog_player_name, null)

            val dialog = MaterialAlertDialogBuilder(this, R.style.BlackWhiteDialog)
                .setView(dialogView)
                .setCancelable(false)
                .create()

            dialog.window?.apply {
                setBackgroundDrawableResource(R.drawable.dialog_background)
            }

            val nameInput = dialogView.findViewById<TextInputEditText>(R.id.dialog_EDT_name)
            nameInput?.apply {
                requestFocus()
                setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        submitScore(dialog, nameInput.text.toString())
                        true
                    } else {
                        false
                    }
                }
            }

            dialogView.findViewById<MaterialButton>(R.id.dialog_BTN_submit)?.setOnClickListener {
                submitScore(dialog, nameInput?.text.toString() ?: "Unknown")
            }

            dialog.show()
        }
    }

    private fun submitScore(dialog: AlertDialog, playerName: String) {
        val newScore = ScoreRecord.Builder()
            .playerName(playerName.ifEmpty { "Unknown" })
            .score(gameManager.getDistance())
            .location(currentLatitude, currentLongitude)
            .build()

        ScoreManager.addScore(newScore)

        val intent = Intent(this, ScoreboardActivity::class.java).apply {
            putExtra(ScoreboardActivity.EXTRA_PLAYER_NAME, playerName)
            putExtra(ScoreboardActivity.EXTRA_SCORE, gameManager.getDistance())
        }
        startActivity(intent)
        dialog.dismiss()
        finish()
    }

    fun restartGame() {
        gameManager.resetGame()

        updateDistanceDisplay()

        for (life in main_IMG_lives) {
            life.visibility = View.VISIBLE
        }

        for (lane in main_IMG_blocks) {
            for (block in lane) {
                block.visibility = View.INVISIBLE
            }
        }

        for (lane in main_IMG_barriers) {
            for (barrier in lane) {
                barrier.visibility = View.INVISIBLE
            }
        }

        updatePlayerVisibility()
        startGame()
    }

    override fun onPause() {
        super.onPause()
        gameTiltDetector?.stop()
        stopGame()
    }

    override fun onResume() {
        super.onResume()
        gameTiltDetector?.start()
        startGame()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopGame()
    }
}