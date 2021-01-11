package com.zdan.stopwatch.ui.exercises

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zdan.stopwatch.data.exercise.Exercise
import com.zdan.stopwatch.data.exercise.ExercisesRepository
import io.realm.RealmResults
import timber.log.Timber

class ExercisesViewModel : ViewModel() {

    private val repository: ExercisesRepository by lazy { ExercisesRepository.instance() }

    private val _showErrorToast = MutableLiveData<Boolean>(false)
    val showErrorToast: LiveData<Boolean> get() = _showErrorToast

    val realmResults: RealmResults<Exercise> by lazy {
        repository.getAllItems()
    }

    private val _isAddingLiveData = MutableLiveData<Boolean>(false)
    val isAddingLiveData: LiveData<Boolean> get() = _isAddingLiveData

    private var tempItem: Exercise? = null

    fun getItem(position: Int): Exercise? = realmResults[position]

    fun getTempItem(): Exercise? = tempItem

    fun setTempItem(item: Exercise?) {
        tempItem = if (item == null) {
            Exercise()
        } else {
            Exercise(item)
        }
    }

    fun fabAddClicked() {
        _isAddingLiveData.value = true
    }

    fun addItem() {
        Timber.d("item: $tempItem")
        tempItem?.let { item ->
            if (item.name.isNotBlank()) {
                repository.addItem(item)
                _isAddingLiveData.value = false
                _showErrorToast.value = false
                tempItem = null
            } else {
                _showErrorToast.value = true
            }
        } ?: run {
            Timber.e("update item null")
        }
    }

    fun addingCancelled() {
        _isAddingLiveData.value = false
        tempItem = null
        _showErrorToast.value = false
    }

    fun updateName(name: String?) {
        Timber.d("update name: $name")
        tempItem?.name = name ?: ""
    }

    fun updateReps(reps: Int) {
        Timber.d("update reps: $reps")
        tempItem?.reps = reps
    }

    fun updateItem() {
        Timber.d("item: $tempItem")
        if (isExerciseValid(tempItem)) {
            repository.updateItem(tempItem!!)
            _isAddingLiveData.value = false
            _showErrorToast.value = false
            tempItem = null
        } else {
            _showErrorToast.value = true
        }
    }

    fun onItemSwiped(position: Int) {
        // delete
        realmResults[position]?.let { item ->
            repository.deleteItem(item.id)
        }
    }

    private fun isExerciseValid(item: Exercise?): Boolean {
        return if (item != null) {
            if (item.name.isNotBlank()) {
                true
            } else {
                false
            }
        } else {
            false
        }
    }

    override fun onCleared() {
        Timber.d("view model clearing")
        repository.onDestroy()
        super.onCleared()
    }

}