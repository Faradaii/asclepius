package com.dicoding.asclepius.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.adapter.ItemAdapter
import com.dicoding.asclepius.database.PredictionDatabase
import com.dicoding.asclepius.database.PredictionHistory
import com.dicoding.asclepius.databinding.ActivityHistoryBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var predictionHistoryAdapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvHistory.layoutManager = LinearLayoutManager(this)

        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = "History"

        loadPredictionHistory()
    }

    private fun loadPredictionHistory() {
        lifecycleScope.launch(Dispatchers.Main) {
            val predictionHistoryList = getPredictionHistoryFromDatabase()
            if (predictionHistoryList.isNotEmpty()) {
                binding.tvEmpty.visibility = View.GONE
            }

            predictionHistoryAdapter = ItemAdapter(predictionHistoryList)
            binding.rvHistory.adapter = predictionHistoryAdapter

        }
    }

    private suspend fun getPredictionHistoryFromDatabase(): List<PredictionHistory> {
        return PredictionDatabase.getInstance(applicationContext).predictionHistoryDao()
            .getAllHistories()
    }
}
