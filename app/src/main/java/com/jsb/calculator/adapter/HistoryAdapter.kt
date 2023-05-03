package com.jsb.calculator.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jsb.calculator.R
import com.jsb.calculator.databinding.ItemHistoryBinding
import com.jsb.calculator.manager.LocalStorage
import com.jsb.calculator.modules.CalculatorHistory
import com.jsb.calculator.utils.formatNumber
import com.jsb.calculator.utils.isSameDay
import com.jsb.calculator.utils.toFormatted
import java.text.SimpleDateFormat
import java.util.Date

class HistoryAdapter(private var data: MutableList<CalculatorHistory>, private val onItemChange: ((List<CalculatorHistory>) -> Unit)) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemHistoryBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }


    override fun getItemCount(): Int {
        return data.size
    }

    fun clear() {
        data.clear()
        notifyDataSetChanged()
        onItemChange(data)
    }

    inner class ViewHolder(private val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            val item = data[position]

            binding.valueText.text = "${item.value} = ${item.cal.toString().formatNumber()}"
            binding.timeText.text =SimpleDateFormat("hh:mm a").format(Date(item.time))
            binding.calculatedText.text = item.cal.toString().formatNumber()

            when (item.type) {
                0 -> {
                    binding.imageType.setBackgroundResource(R.drawable.ic_calculator2)
                }
                1 -> {
                    binding.imageType.setBackgroundResource(R.drawable.ic_keyboard_white)
                }
                2 -> {
                    binding.imageType.setBackgroundResource(R.drawable.ic_keyboard_white)
                }
                3 -> {
                    binding.imageType.setBackgroundResource(R.drawable.ic_floating_cal_white)
                }
            }
            binding.dateTv.visibility = View.GONE
            if (position == 0) {
                binding.dateTv.visibility = View.VISIBLE
                binding.dateTv.text = Date(item.time).toFormatted()
            } else {
                val time = data[position - 1].time
                if (!Date(time).isSameDay(Date(item.time))) {
                    binding.dateTv.visibility = View.VISIBLE
                    binding.dateTv.text = Date(item.time).toFormatted()
                } else {
                    binding.dateTv.visibility = View.GONE
                }
            }
            binding.deleteBt.setOnClickListener {
                data.remove(item)
                LocalStorage(it.context).saveCalculatorHistoryList(data)
                notifyDataSetChanged()
                onItemChange(data)
            }
        }
    }


}