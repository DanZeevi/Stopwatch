package com.zdan.stopwatch.ui.exercises

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zdan.stopwatch.data.Exercise
import com.zdan.stopwatch.databinding.LayoutExerciseItemBinding

class ExercisesAdapter : RecyclerView.Adapter<ExercisesAdapter.ViewHolder>() {

    var list: List<Exercise> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExercisesAdapter.ViewHolder =
        ViewHolder(
            LayoutExerciseItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ExercisesAdapter.ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount() = list.size
    fun submitList(newList: List<Exercise>) {
        list = newList
    }

    inner class ViewHolder(private val binding: LayoutExerciseItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val item = list[position]
            binding.apply{
                txtNumber.text = (position + 1).toString()
                txtDescription.text = item.name
                txtReps.text = item.reps.toString()
            }
        }
    }
}