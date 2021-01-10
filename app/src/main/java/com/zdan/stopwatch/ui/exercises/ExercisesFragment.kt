package com.zdan.stopwatch.ui.exercises

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.zdan.stopwatch.R
import com.zdan.stopwatch.data.exercise.Exercise
import com.zdan.stopwatch.databinding.DialogExerciseItemBinding
import com.zdan.stopwatch.databinding.FragmentExercisesBinding
import com.zdan.stopwatch.util.addAfterTextChangeListener
import io.realm.RealmResults
import timber.log.Timber

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
            isAddingLiveData.observe(viewLifecycleOwner) { isAdding ->
                if (isAdding) {
                    openItemDialog(viewModel.getTempItem(), false)
                }
            }
            showErrorToast.observe(viewLifecycleOwner) { show ->
                if (show) {
                    showErrorToast()
                }
            }
        }
    }

    private fun showErrorToast() {
        Toast.makeText(binding.root.context, R.string.exercise_fragment_error, LENGTH_SHORT).show()
    }

    private fun openItemDialog(item: Exercise?, isEdit: Boolean = false) {

        // set temp item in view model
        viewModel.setTempItem(item)

        val dialogBinding =
            DialogExerciseItemBinding.inflate(LayoutInflater.from(binding.root.context))
        dialogBinding.apply {
            // edit text listener
            editName.addAfterTextChangeListener { editable ->
                viewModel.updateName(editable?.toString())
            }
            // number picker settings and listener
            pickerReps.apply {
                maxValue = REPS_MAX_VALUE
                minValue = REPS_MIN_VALUE
                setOnValueChangedListener { picker, oldVal, newVal ->
                    viewModel.updateReps(newVal)
                }
            }
            // show initial fields when updating
            item?.let { item ->
                Timber.d("item to update: $item")
                // show item details
                editName.text = Editable.Factory.getInstance().newEditable(item.name)
                pickerReps.wrapSelectorWheel = true
                pickerReps.value = item.reps
            }
        }

        val onDialogClick = DialogInterface.OnClickListener { dialog, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    if (isEdit) {
                        viewModel.updateItem()
                    } else {
                        viewModel.addItem()
                    }
                }
                DialogInterface.BUTTON_NEGATIVE -> {
                    dialog.cancel()
                    viewModel.addingCancelled()
                }
            }
        }
        val title = if (isEdit) {
            getString(R.string.dialog_edit_title)
        } else {
            getString(R.string.dialog_add_title)
        }

        val positiveButtonText = if (isEdit) {
            getString(R.string.btn_dialog_edit_positive)
        } else {
            getString(R.string.btn_dialog_add_positive)
        }

        val alertDialog = AlertDialog.Builder(binding.root.context)
            .setTitle(title)
            .setView(dialogBinding.root)
            .setPositiveButton(positiveButtonText, onDialogClick)
            .setNegativeButton(getString(R.string.btn_dialog_add_negative), onDialogClick)

        alertDialog.show()

    }

    private fun setRecyclerView(context: Context, realmResults: RealmResults<Exercise>) {
        exercisesAdapter = ExercisesAdapter(realmResults, onClick = { position ->
            openItemDialog(viewModel.getItem(position), true)
        })
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
