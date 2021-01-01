package com.zdan.stopwatch.ui.repeaters

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zdan.stopwatch.data.Repeater
import com.zdan.stopwatch.util.toStopwatchFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

private const val TENTH_SECOND: Long = 100

class RepeatersViewModel : ViewModel() {

    private var time: Long = 0
    private val list: List<Repeater> = listOf(
        Repeater("Dead hang", 5000),
        Repeater("Rest", 5000),
        Repeater("Dead hang", 5000),
        Repeater("Rest", 5000),
        Repeater("Dead hang", 5000),
        Repeater("Rest", 5000),
        Repeater("Dead hang", 5000),
        Repeater("Rest", 5000),
        Repeater("Dead hang", 5000),
        Repeater("Rest", 5000),
        Repeater("Dead hang", 5000),
        Repeater("Rest", 5000),
        Repeater("Rest", 60000)
    )

    private val _positionLiveData = MutableLiveData<Int>(-1)
    val positionLiveData: LiveData<Int> get() = _positionLiveData

    private val _timeLiveData = MutableLiveData<Long>()
    val timeLiveData: LiveData<Long> get() = _timeLiveData

    private val _isOn = MutableLiveData<Boolean>(false)
    val isOn: LiveData<Boolean> get() = _isOn

    fun fabClicked() {
        _isOn.value?.let { isOn ->
            _isOn.value = !isOn
            if (isOn) {
                stopSession()
            } else {
                startSession()
            }

        }
    }

    private fun stopSession() {
        // no_op
    }

    private fun startSession() {
        viewModelScope.launch(Dispatchers.IO) {
            var iterator = list.listIterator()
            _positionLiveData.value?.let { position ->
                if (position > -1) {
                    iterator = list.listIterator(position)
                }
            }
            while (iterator.hasNext() && isOn.value == true) {
                _positionLiveData.postValue(iterator.nextIndex())
                startItem(iterator.next())
            }
        }
    }

    private suspend fun startItem(item: Repeater) {
        // get current time if paused
        if (time == 0L) {
            time = item.duration
        }
        Timber.d("viewModel time: ${time.toStopwatchFormat()}")
        while (time > 0 && isOn.value == true) {
            _timeLiveData.postValue(time)
            delay(TENTH_SECOND)
            time -= TENTH_SECOND
        }
        _timeLiveData.postValue(time)
    }

    fun getList(): List<Repeater> = list

}