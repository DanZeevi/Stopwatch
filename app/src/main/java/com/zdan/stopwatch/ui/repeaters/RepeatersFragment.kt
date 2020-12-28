package com.zdan.stopwatch.ui.repeaters

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.zdan.stopwatch.R
import com.zdan.stopwatch.databinding.FragmentRepeatersBinding
import com.zdan.stopwatch.ui.common.BaseFragment
import com.zdan.stopwatch.util.toStopwatchFormat
import timber.log.Timber

class RepeatersFragment : BaseFragment(R.layout.fragment_repeaters) {

    private val viewModel: RepeatersViewModel by viewModels()
    private var _binding: FragmentRepeatersBinding? = null
    private val binding: FragmentRepeatersBinding get() = _binding!!
    private lateinit var repeatersAdapter: RepeatersRecyclerViewAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentRepeatersBinding.bind(view)

        setViews()
        setObservers()
    }

    private fun setObservers() {
        viewModel.apply {
            positionLiveData.observe(viewLifecycleOwner) { position ->
                repeatersAdapter.setCurrent(position)
                binding.recyclerView.scrollToPosition(position)
            }
            timeLiveData.observe(viewLifecycleOwner) {
                Timber.d("time in fragment: ${it.toStopwatchFormat()}")
                repeatersAdapter.updateTimeTextView(it)
            }
            isOn.observe(viewLifecycleOwner) { isOn ->
                // keep screen on when timer is on
                keepScreenOn(isOn)

                if (isOn) {
                    binding.fabStart.setImageResource(R.drawable.ic_stop)
                } else {
                    binding.fabStart.setImageResource(R.drawable.ic_start)
                }
            }
        }
    }

    private fun setViews() {
        setRecyclerView()
        binding.apply {
            fabStart.setOnClickListener {
                viewModel.fabClicked()
            }
        }
    }

    private fun setRecyclerView() {
        repeatersAdapter = RepeatersRecyclerViewAdapter()

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(binding.root.context)
            adapter = repeatersAdapter
            setHasFixedSize(true)
        }
        repeatersAdapter.submitList(viewModel.getList())
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}