package com.example.hw1_208388124

import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hw1_208388124.fragments.ScoreListFragment
import com.example.hw1_208388124.fragments.ScoreMapFragment

class ScoreboardActivity : AppCompatActivity() {
    private lateinit var scoreboard_FRAME_list: FrameLayout
    private lateinit var scoreboard_FRAME_map: FrameLayout

    private lateinit var scoreListFragment: ScoreListFragment
    private lateinit var scoreMapFragment: ScoreMapFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scoreboard)

        val playerName = intent.getStringExtra(EXTRA_PLAYER_NAME) ?: ""
        val score = intent.getIntExtra(EXTRA_SCORE, 0)

        findViews()
        initFragments(playerName, score)
    }

    private fun findViews() {
        scoreboard_FRAME_list = findViewById(R.id.scoreboard_FRAME_list)
        scoreboard_FRAME_map = findViewById(R.id.scoreboard_FRAME_map)
    }

    private fun initFragments(playerName: String, score: Int) {
        scoreListFragment = ScoreListFragment().apply {
            arguments = Bundle().apply {
                putString(ScoreListFragment.ARG_PLAYER_NAME, playerName)
                putInt(ScoreListFragment.ARG_SCORE, score)
            }
        }

        scoreMapFragment = ScoreMapFragment()

        supportFragmentManager.beginTransaction()
            .add(R.id.scoreboard_FRAME_list, scoreListFragment)
            .add(R.id.scoreboard_FRAME_map, scoreMapFragment)
            .commit()
    }

    fun showLocation(latitude: Double, longitude: Double) {
        scoreMapFragment.showLocation(latitude, longitude)
    }

    companion object {
        const val EXTRA_PLAYER_NAME = "EXTRA_PLAYER_NAME"
        const val EXTRA_SCORE = "EXTRA_SCORE"
    }
}