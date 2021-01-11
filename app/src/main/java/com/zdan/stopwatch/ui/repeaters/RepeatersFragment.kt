package com.zdan.stopwatch.ui.repeaters

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.zdan.stopwatch.R
import com.zdan.stopwatch.databinding.FragmentRepeatersBinding
import com.zdan.stopwatch.ui.common.BaseFragment
import com.zdan.stopwatch.util.toPreTimeFormat
import com.zdan.stopwatch.util.toStopwatchFormat
import timber.log.Timber

class RepeatersFragment : BaseFragment(R.layout.fragment_repeaters) {

    private val viewModel: RepeatersViewModel by viewModels()
    private var _binding: FragmentRepeatersBinding? = null
    private val binding: FragmentRepeatersBinding get() = _binding!!
    private lateinit var repeatersAdapter: RepeatersAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentRepeatersBinding.bind(view)

        setViews()
        setObservers()
    }

    private fun setObservers() {
        viewModel.apply {
            positionLiveData.observe(viewLifecycleOwner) { position ->
                // TODO: fix scroll to position
                repeatersAdapter.setCurrent(position)
                binding.recyclerView.post {
                    binding.recyclerView.layoutManager?.scrollToPosition(
                        position
                    )
                }
            }
            timeLiveData.observe(viewLifecycleOwner) { time ->

                if (viewModel.positionLiveData.value == viewModel.POSITION_PRE_TIME) {
                    // update pre-time
                    updatePreTimeTextView(time)
                } else {
                    // update time of repeater item
                    Timber.d("time in fragment: ${time.toStopwatchFormat()}")
                    repeatersAdapter.updateTimeTextView(time)
                }
            }
            isOn.observe(viewLifecycleOwner) { isOn ->
                // keep screen on when timer is on
                keepScreenOn(isOn)
                // change fab icon
                if (isOn) {
                    binding.fabStart.setImageResource(R.drawable.ic_stop)
                } else {
                    binding.fabStart.setImageResource(R.drawable.ic_start)
                }
            }
            isPreTimeOn.observe(viewLifecycleOwner) { isPreTimeOn ->
                showPreTime(isPreTimeOn)
            }
        }
    }

    private fun setViews() {
        setRecyclerView()
        binding.apply {
            fabStart.setOnClickListener {
                viewModel.fabStartClicked()
            }
            fabReset.setOnClickListener {
                viewModel.fabResetClicked()
            }
        }
    }

    private fun setRecyclerView() {
        repeatersAdapter = RepeatersAdapter { position ->
            viewModel.itemClicked(position)
        }
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(binding.root.context)
            adapter = repeatersAdapter
            setHasFixedSize(true)
        }
        repeatersAdapter.submitList(viewModel.getList())
    }

    private fun updatePreTimeTextView(time: Long) {
        binding.txtPreTime.text = time.toPreTimeFormat()
    }

    private fun showPreTime(show: Boolean) {
        binding.layoutPreTime.visibility = if (show) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}