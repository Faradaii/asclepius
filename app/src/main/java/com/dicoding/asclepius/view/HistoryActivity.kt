package com.dicoding.asclepius.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.adapter.PredictionHistoryAdapter
import com.dicoding.asclepius.database.PredictionDatabase
import com.dicoding.asclepius.database.PredictionHistory
import com.dicoding.asclepius.databinding.ActivityHistoryBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var predictionHistoryAdapter: PredictionHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvHistory.layoutManager = LinearLayoutManager(this)

        loadPredictionHistory()
    }

    private fun loadPredictionHistory() {
        lifecycleScope.launch(Dispatchers.Main) {
            val predictionHistoryList = getPredictionHistoryFromDatabase()

            predictionHistoryAdapter = PredictionHistoryAdapter(predictionHistoryList)
            binding.rvHistory.adapter = predictionHistoryAdapter
        }
    }

    private suspend fun getPredictionHistoryFromDatabase(): List<PredictionHistory> {
        return PredictionDatabase.getInstance(applicationContext).predictionHistoryDao()
            .getAllHistories()
    }
}
