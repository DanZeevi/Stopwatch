package com.zdan.stopwatch.ui.stopwatch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TENTH_SECOND: Long = 100

class StopwatchViewModel : ViewModel() {

    private val _isTimerOnLiveData by lazy { MutableLiveData<Boolean>() }
    val isTimerOnLiveData: LiveData<Boolean> get() = _isTimerOnLiveData

    private val _timeLiveData by lazy { MutableLiveData<Long>() }
    val timeLiveData: LiveData<Long> get() = _timeLiveData

    init {
        _isTimerOnLiveData.value = false
        _timeLiveData.value = 0
    }

    fun buttonStartClicked() {
        _isTimerOnLiveData.value?.let { isOn ->
            _isTimerOnLiveData.value = !isOn
            if (isOn) {
                stopTimer()
            } else {
                startTimer()
            }
        }
    }

    private fun startTimer() {
        viewModelScope.launch(Dispatchers.IO) {
            while (_isTimerOnLiveData.value == true) {
                delay(TENTH_SECOND)
                timeStep()
                }
            }
        }

    private fun timeStep() {
        _timeLiveData.value?.let { time ->
            _timeLiveData.postValue(time + TENTH_SECOND)
        }
    }

    private fun stopTimer() {
        _isTimerOnLiveData.value = false
    }

    fun buttonResetClicked() {
        resetTime()
    }

    private fun resetTime() {
        _timeLiveData.value = 0
    }

    override fun onCleared() {
        stopTimer()
        super.onCleared()
    }
}