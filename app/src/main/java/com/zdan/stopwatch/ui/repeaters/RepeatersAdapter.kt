package com.zdan.stopwatch.ui.repeaters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.zdan.stopwatch.R
import com.zdan.stopwatch.data.Repeater
import com.zdan.stopwatch.databinding.LayoutRepeatersItemBinding
import com.zdan.stopwatch.ui.repeaters.RepeatersAdapter.RepeaterViewHolder
import com.zdan.stopwatch.util.toStopwatchFormat
import com.zdan.stopwatch.util.toTextFormat
import timber.log.Timber

class RepeatersAdapter() : RecyclerView.Adapter<RepeaterViewHolder>() {

    var highlightPosition: Int = -1

    var list = listOf<Repeater>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        RepeaterViewHolder(
            LayoutRepeatersItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: RepeaterViewHolder, position: Int) =
        holder.bind(position)

    override fun onBindViewHolder(
        holder: RepeaterViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        Timber.d("Payloads: $payloads")
        if (payloads.isNotEmpty()) {
            for (payload in payloads) {
                Timber.d("Payload: $payload")
                handlePayload(payload, holder)
            }
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    private fun handlePayload(
        payload: Any,
        holder: RepeaterViewHolder
    ) {
        when (payload) {
            is Long ->
                holder.updateTimeTextView(payload)
            is Boolean ->
                holder.highlight(payload)
        }
    }

    override fun getItemCount() = list.size

    fun submitList(newList: List<Repeater>) {
        list = newList
        notifyDataSetChanged()
    }

    fun setCurrent(position: Int) {
        Timber.d("current: $position")
        // unhighlight previous
        if (highlightPosition >= 0) {
            notifyItemChanged(highlightPosition, false)
        }
        // highlight current
        highlightPosition = position
        notifyItemChanged(highlightPosition, true)
    }

    fun updateTimeTextView(time: Long) {
        Timber.d("time: ${time.toStopwatchFormat()}")
        notifyItemChanged(highlightPosition, time)
    }

    inner class RepeaterViewHolder(private val binding: LayoutRepeatersItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val item = list[position]
            Timber.d("item: $item")
            binding.apply {
                txtNumber.text = item.number.toString()
                txtDescription.text = item.description
                txtTime.text = item.duration.toTextFormat()
            }
        }

        fun updateTimeTextView(time: Long) {
            binding.txtTime.text = time.toStopwatchFormat()
        }

        fun highlight(boolean: Boolean) {
            binding.root.apply {
                background = if (boolean) {
                    ContextCompat.getDrawable(context, R.color.highlight)
                } else {
                    ContextCompat.getDrawable(context, R.color.white)
                }
            }
        }
    }

}



