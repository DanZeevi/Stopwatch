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

    fun fabAddClicked() {
        _isAddingLiveData.value = true
    }

    fun getTempItem(): Exercise? = tempItem

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
            _showErrorToast.value = true
        }
    }

    fun addingCancelled() {
        _isAddingLiveData.value = false
        tempItem = null
        _showErrorToast.value = true
    }

    fun updateName(name: String?) {
        if (name != null) {
            if (tempItem == null) {
                tempItem = Exercise()
            }
            tempItem!!.name = name
        } else {
            _showErrorToast.value = true
        }
    }

    fun updateReps(reps: Int) {
        if (tempItem == null) {
            tempItem = Exercise(name = "", reps = reps)
        } else {
            tempItem!!.reps = reps
        }
        _showErrorToast.value = true
    }

    override fun onCleared() {
        repository.onDestroy()
        super.onCleared()
    }
}