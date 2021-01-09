package com.zdan.stopwatch.ui.exercises

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zdan.stopwatch.data.exercise.Exercise
import com.zdan.stopwatch.databinding.LayoutExerciseItemBinding
import io.realm.RealmRecyclerViewAdapter
import io.realm.RealmResults

class ExercisesAdapter(var results: RealmResults<Exercise>) :
    RealmRecyclerViewAdapter<Exercise, ExercisesAdapter.ViewHolder>(
        results, true, true)
{

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

    inner class ViewHolder(private val binding: LayoutExerciseItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            results[position]?.let { item ->
                binding.apply {
                    txtNumber.text = (position + 1).toString()
                    txtDescription.text = item.name
                    txtReps.text = item.reps.toString()
                }
            }
        }
    }
}