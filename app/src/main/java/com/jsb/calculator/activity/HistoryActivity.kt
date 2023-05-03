package com.jsb.calculator.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.jsb.calculator.adapter.HistoryAdapter
import com.jsb.calculator.databinding.ActivityHistoryBinding
import com.jsb.calculator.manager.AdsManager
import com.jsb.calculator.manager.LocalStorage
import com.jsb.calculator.modules.CalculatorHistory
import java.util.Collections

class HistoryActivity : AppCompatActivity() {

    lateinit var binding: ActivityHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AdsManager(this).initialize {
            it.setUpBanner(binding.adView)
        }

        val calHisList: List<CalculatorHistory> = LocalStorage(this).getCalculatorHistoryList().reversed()
        if (calHisList.isEmpty()){
            binding.errorTv.visibility = View.VISIBLE
            binding.deleteAllBt.visibility = View.GONE
        }else{
            binding.errorTv.visibility = View.GONE
            binding.deleteAllBt.visibility = View.VISIBLE
        }
        binding.listView.layoutManager = LinearLayoutManager(this)
        val adapter = HistoryAdapter(calHisList.toMutableList()){
            if (it.isEmpty()){
                binding.errorTv.visibility = View.VISIBLE
                binding.deleteAllBt.visibility = View.GONE
            }else{
                binding.errorTv.visibility = View.GONE
                binding.deleteAllBt.visibility = View.VISIBLE
            }
        }
        binding.listView.adapter = adapter


        binding.deleteAllBt.setOnClickListener {
            LocalStorage(this).saveCalculatorHistoryList(listOf())
            adapter.clear()
        }

    }
}