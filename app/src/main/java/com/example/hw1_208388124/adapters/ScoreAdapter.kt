package com.example.hw1_208388124.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hw1_208388124.databinding.ScoreItemBinding
import com.example.hw1_208388124.interfaces.ScoreCallback
import com.example.hw1_208388124.models.ScoreRecord
import java.time.format.DateTimeFormatter

class ScoreAdapter(private var scores: List<ScoreRecord>) :
    RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder>() {

    var scoreCallback: ScoreCallback? = null

    inner class ScoreViewHolder(val binding: ScoreItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                scoreCallback?.onScoreClicked(
                    getItem(adapterPosition),
                    adapterPosition
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreViewHolder {
        val binding = ScoreItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ScoreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScoreViewHolder, position: Int) {
        val score = getItem(position)
        with(holder.binding) {
            scoreLBLRank.text = "${position + 1}."
            scoreLBLName.text = score.playerName
            scoreLBLScore.text = "${score.score} YDS"
            scoreLBLDate.text = score.date.format(
                DateTimeFormatter.ofPattern("dd-MMM-yyyy")
            )
        }
    }

    override fun getItemCount() = scores.size

    fun getItem(position: Int) = scores[position]

    fun updateScores(newScores: List<ScoreRecord>) {
        scores = newScores
        notifyDataSetChanged()
    }
}