package com.zdan.stopwatch.ui.repeaters

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zdan.stopwatch.data.Repeater
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TENTH_SECOND: Long = 100
private const val PRE_TIME_DURATION: Long = 3 * 1000
private const val PRE_TIME_DESCRIPTION: String = "pre-time"

class RepeatersViewModel : ViewModel() {

    val POSITION_PRE_TIME: Int = -1

    private val preTime: Repeater = Repeater(PRE_TIME_DESCRIPTION, PRE_TIME_DURATION)
    private var job: Job? = null
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

    private val _positionLiveData = MutableLiveData<Int>(POSITION_PRE_TIME)
    val positionLiveData: LiveData<Int> get() = _positionLiveData

    private val _timeLiveData = MutableLiveData<Long>()
    val timeLiveData: LiveData<Long> get() = _timeLiveData

    private val _isOn = MutableLiveData<Boolean>(false)
    val isOn: LiveData<Boolean> get() = _isOn

    private val _isPreTimeOn = MutableLiveData<Boolean>(false)
    val isPreTimeOn: LiveData<Boolean> get() = _isPreTimeOn

    fun fabStartClicked() {
        _isOn.value?.let { isOn ->
            if (isOn) {
                stopSession()
            } else {
                startSession()
            }

        }
    }

    fun fabResetClicked() {
        stopSession()
        _positionLiveData.value = POSITION_PRE_TIME
    }

    private fun stopSession() {
        job?.cancel()
        _isOn.value = false
        _isPreTimeOn.value = false
    }

    private fun startSession() {
        _isOn.value = true
        job = viewModelScope.launch(Dispatchers.IO) {
            // save previous position
            val prevPosition = _positionLiveData.value ?: POSITION_PRE_TIME
            // start counter
            // set iterator to beginning of list
            var iterator = list.listIterator()
            // resume from previous position if exists
            if (prevPosition > POSITION_PRE_TIME) {
                iterator = list.listIterator(prevPosition)
            } else {
                // set current position to pre-time
                _isPreTimeOn.postValue(true)
                startItem(preTime)
                _isPreTimeOn.postValue(false)
            }
            while (iterator.hasNext() && isOn.value == true) {
                _positionLiveData.postValue(iterator.nextIndex())
                startItem(iterator.next())
            }
        }
    }

    private suspend fun startItem(item: Repeater) {
        // get current time if paused
        if (time == 0L || item.description == PRE_TIME_DESCRIPTION) {
            time = item.duration
        }

        while (time > 0 && isOn.value == true) {
            _timeLiveData.postValue(time)
            delay(TENTH_SECOND)
            time -= TENTH_SECOND
        }
        _timeLiveData.postValue(time)
    }

    fun getList(): List<Repeater> = list

    fun itemClicked(position: Int) {
        // select item
        _positionLiveData.value = position
        // restart time
        time = 0L

        _isOn.value?.let { isOn ->
            // start from selected time if timer is running
            if (isOn) {
                stopSession()
                startSession()
            }
        }
    }

    override fun onCleared() {
        job?.cancel()
        super.onCleared()
    }


    companion object {
        const val POSITION_PRE_TIME: Int = -1
    }
}