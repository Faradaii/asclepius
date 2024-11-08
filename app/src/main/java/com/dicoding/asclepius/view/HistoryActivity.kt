package com.dicoding.asclepius.view

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.adapter.ItemAdapter
import com.dicoding.asclepius.databinding.ActivityHistoryBinding
import com.dicoding.asclepius.viewModel.HistoryViewModel

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var predictionHistoryAdapter: ItemAdapter

    private val viewModel: HistoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvHistory.layoutManager = LinearLayoutManager(this)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowTitleEnabled(true)
        actionBar?.title = "History"

        viewModel.predictionHistory.observe(this) { predictionHistoryList ->
            if (predictionHistoryList.isNotEmpty()) {
                binding.tvEmpty.visibility = View.GONE
            } else {
                binding.tvEmpty.visibility = View.VISIBLE
            }

            predictionHistoryAdapter = ItemAdapter(predictionHistoryList)
            binding.rvHistory.adapter = predictionHistoryAdapter
        }
    }
}
