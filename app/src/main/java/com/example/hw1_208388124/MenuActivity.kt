package com.example.hw1_208388124

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton

class MenuActivity : AppCompatActivity() {

    private lateinit var menu_BTN_slow: MaterialButton
    private lateinit var menu_BTN_fast: MaterialButton
    private lateinit var menu_BTN_sensors: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        findViews()
        initViews()
    }

    private fun findViews() {
        menu_BTN_slow = findViewById(R.id.menu_BTN_slow)
        menu_BTN_fast = findViewById(R.id.menu_BTN_fast)
        menu_BTN_sensors = findViewById(R.id.menu_BTN_sensors)
    }

    private fun initViews() {
        menu_BTN_slow.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java).apply {
                putExtra(EXTRA_GAME_MODE, GAME_MODE_SLOW)
            })
        }

        // Button Mode - Fast (to be implemented)
        menu_BTN_fast.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java).apply {
                putExtra(EXTRA_GAME_MODE, GAME_MODE_FAST)
            })
        }

        // Sensors Mode (to be implemented)
        menu_BTN_sensors.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java).apply {
                putExtra(EXTRA_GAME_MODE, GAME_MODE_SENSORS)
            })
        }
    }

    companion object {
        const val EXTRA_GAME_MODE = "EXTRA_GAME_MODE"
        const val GAME_MODE_SLOW = "GAME_MODE_SLOW"
        const val GAME_MODE_FAST = "GAME_MODE_FAST"
        const val GAME_MODE_SENSORS = "GAME_MODE_SENSORS"
    }
}