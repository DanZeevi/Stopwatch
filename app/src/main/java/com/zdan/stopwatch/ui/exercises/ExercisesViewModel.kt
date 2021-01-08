package com.zdan.stopwatch.ui.exercises

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zdan.stopwatch.data.Exercise
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where
import timber.log.Timber

class ExercisesViewModel : ViewModel() {

    private val realm: Realm by lazy { Realm.getDefaultInstance() }

    private val _showErrorToast = MutableLiveData<Boolean>(false)
    val showErrorToast: LiveData<Boolean> get() = _showErrorToast

/*    private val _listLiveData =
        MutableLiveData<MutableList<Exercise>>()
    val listLiveData: LiveData<MutableList<Exercise>> get() = _listLiveData*/

    val realmResults: RealmResults<Exercise> by lazy {
        realm.where<Exercise>().findAll()
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
                realm.executeTransactionAsync { realm ->
                    realm.insert(item)
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