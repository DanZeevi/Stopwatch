package com.zdan.stopwatch.ui.exercises

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zdan.stopwatch.data.Exercise
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class ExercisesViewModel : ViewModel() {

    private val realm: Realm by lazy { Realm.getDefaultInstance() }

    private val _showErrorToast = MutableLiveData<Boolean>(false)
    val showErrorToast: LiveData<Boolean> get() = _showErrorToast

    private val _listLiveData =
        MutableLiveData<MutableList<Exercise>>()
    val listLiveData: LiveData<MutableList<Exercise>> get() = _listLiveData

    private val _isAddingLiveData = MutableLiveData<Boolean>(false)
    val isAddingLiveData: LiveData<Boolean> get() = _isAddingLiveData

    private var tempItem: Exercise? = null

    init {
        listOf(
            Exercise(name = "Pull up", reps = 15),
            Exercise(name = "Push up", reps = 15),
            Exercise(name = "Dragonfly", reps = 15),
            Exercise(name = "Squat", reps = 15),
            Exercise(name = "Planck", reps = 15)
        )

            viewModelScope.launch(Dispatchers.IO) {
                _listLiveData.postValue(realm.where<Exercise>().findAll())
                /*realm.executeTransaction { realm ->
                    val newItem = realm.createObject<Exercise>().apply {
                        reps = it.reps
                        name = it.name
                    }
                }*/
            }
    }

    fun fabAddClicked() {
        _isAddingLiveData.value = true
    }

    fun getTempItem(): Exercise? = tempItem

    fun addItem() {
        Timber.d("item: $tempItem")
        tempItem?.let { item ->
            if (item.name.isNotBlank()) {
//                _listLiveData.value?.add(item)
                viewModelScope.launch(Dispatchers.IO) {
                    realm.executeTransaction { realm ->
                        val newItem = realm.createObject<Exercise>()
                        newItem.name = item.name
                        newItem.reps = item.reps
                    }
                }
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
        realm.close()

        super.onCleared()
    }
}