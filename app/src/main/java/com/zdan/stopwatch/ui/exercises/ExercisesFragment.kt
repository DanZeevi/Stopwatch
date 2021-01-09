package com.zdan.stopwatch.ui.exercises

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.zdan.stopwatch.R
import com.zdan.stopwatch.data.exercise.Exercise
import com.zdan.stopwatch.databinding.DialogAddExerciseBinding
import com.zdan.stopwatch.databinding.FragmentExercisesBinding
import com.zdan.stopwatch.util.addAfterTextChangeListener
import io.realm.RealmResults

private const val REPS_MIN_VALUE: Int = 1
private const val REPS_MAX_VALUE: Int = 30

class ExercisesFragment : Fragment(R.layout.fragment_exercises) {

    private var _binding: FragmentExercisesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ExercisesViewModel by viewModels()
    private lateinit var exercisesAdapter: ExercisesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentExercisesBinding.bind(view)

        setViews()
        setObservers()
    }

    private fun setViews() {
        binding.apply {
            setRecyclerView(root.context, viewModel.realmResults)
            fabAdd.setOnClickListener {
                viewModel.fabAddClicked()
            }
        }
    }

    private fun setObservers() {
        viewModel.apply {
            /*    listLiveData.observe(viewLifecycleOwner) { list ->
                    exercisesAdapter.submitList(list)
                }*/
            isAddingLiveData.observe(viewLifecycleOwner) { isAdding ->
                if (isAdding) {
                    openAddingDialog(viewModel.getTempItem())
                }
            }
        }
    }

    private fun openAddingDialog(addingItem: Exercise?) {

        val dialogBinding =
            DialogAddExerciseBinding.inflate(LayoutInflater.from(binding.root.context))
        dialogBinding.apply {
            addingItem?.let { item ->
                editName.text = Editable.Factory.getInstance().newEditable(item.name)
                pickerReps.value = item.reps
            }
            editName.addAfterTextChangeListener { editable ->
                viewModel.updateName(editable?.toString())
            }

            pickerReps.apply {
                maxValue = REPS_MAX_VALUE
                minValue = REPS_MIN_VALUE
                setOnValueChangedListener { picker, oldVal, newVal ->
                    viewModel.updateReps(newVal)
                }
            }
        }

        val onDialogClick = DialogInterface.OnClickListener { dialog, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> viewModel.addItem()
                DialogInterface.BUTTON_NEGATIVE -> {
                    dialog.cancel()
                    viewModel.addingCancelled()
                }
            }
        }

        val alertDialog = AlertDialog.Builder(binding.root.context)
            .setTitle(getString(R.string.dialog_add_title))
            .setView(dialogBinding.root)
            .setPositiveButton(getString(R.string.btn_dialog_add_positive), onDialogClick)
            .setNegativeButton(getString(R.string.btn_dialog_add_negative), onDialogClick)

        alertDialog.show()

    }

    private fun setRecyclerView(context: Context, realmResults: RealmResults<Exercise>) {
        exercisesAdapter = ExercisesAdapter(realmResults)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = exercisesAdapter
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}
