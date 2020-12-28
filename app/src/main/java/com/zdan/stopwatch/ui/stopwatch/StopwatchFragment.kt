package com.zdan.stopwatch.ui.stopwatch

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.zdan.stopwatch.R
import com.zdan.stopwatch.databinding.FragmentStopwatchBinding
import com.zdan.stopwatch.ui.common.BaseFragment
import com.zdan.stopwatch.util.toStopwatchFormat
import timber.log.Timber

class StopwatchFragment : BaseFragment(R.layout.fragment_stopwatch) {

    private var _binding : FragmentStopwatchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: StopwatchViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentStopwatchBinding.bind(view)

        setViews()
        setObservers()
    }

    private fun setViews() {
        binding.apply {
            // set text to current time
            textViewTime.text = (viewModel.timeLiveData.value ?: 0).toStopwatchFormat()
            // button start on click
            buttonStart.setOnClickListener {
                viewModel.buttonStartClicked()
            }
            // button lap on click
            buttonReset.setOnClickListener {
                viewModel.buttonResetClicked()
            }
        }
    }

    private fun setObservers() {
        viewModel.apply {
            isTimerOnLiveData.observe(viewLifecycleOwner) { isOn ->
                updateStartButton(isOn)
                // keep screen on when timer is on
                keepScreenOn(isOn)
            }
            timeLiveData.observe(viewLifecycleOwner) { timeLong ->
                Timber.d("time: $timeLong")
                updateTimeView(timeLong)
            }
        }
    }

    private fun updateStartButton(isOn: Boolean) {
        if (isOn) {
            (binding.buttonStart).setImageResource(R.drawable.ic_stop)
        } else {
            (binding.buttonStart).setImageResource(R.drawable.ic_start)
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun updateTimeView(timeLong: Long) {
        binding.textViewTime.text = timeLong.toStopwatchFormat()
    }
}