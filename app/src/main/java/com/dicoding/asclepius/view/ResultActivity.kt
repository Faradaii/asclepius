package com.dicoding.asclepius.view

import ResultViewModel
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.asclepius.database.PredictionDatabase
import com.dicoding.asclepius.database.PredictionHistory
import com.dicoding.asclepius.databinding.ActivityResultBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private lateinit var predictionDatabase: PredictionDatabase

    private val viewModel: ResultViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowTitleEnabled(true)
        actionBar?.title = "Result"

        predictionDatabase = PredictionDatabase.getInstance(applicationContext)

        if (viewModel.imageUri == null) {
            viewModel.imageUri = intent.getStringExtra(EXTRA_IMAGE_URI)?.let { Uri.parse(it) }
            viewModel.predictions = intent.getStringExtra(EXTRA_PREDICTION)
            viewModel.confidenceScore =
                (intent.getFloatExtra(EXTRA_CONFIDENCE_SCORE, 0f) * 100).toInt()
        }

        binding.apply {
            resultImage.setImageURI(viewModel.imageUri ?: Uri.EMPTY)
            resultText.text = buildString {
                append(viewModel.predictions ?: "Something went wrong")
                append(" ")
                append(viewModel.confidenceScore)
                append(if (viewModel.predictions != null) "%" else "(No Data)")
            }
            backButton.setOnClickListener { finish() }
            newsButton.setOnClickListener {
                val fragment = HealthNewsFragment()
                fragment.show(supportFragmentManager, "dialog")
            }
        }

        savePredictionHistory(
            viewModel.imageUri.toString(),
            viewModel.predictions ?: "",
            viewModel.confidenceScore
        )
    }

    private fun savePredictionHistory(imagePath: String, result: String, confidenceScore: Int) {
        val history = PredictionHistory(
            imagePath = imagePath,
            result = result,
            confidenceScore = confidenceScore,
            date = Date()
        )
        CoroutineScope(Dispatchers.IO).launch {
            predictionDatabase.predictionHistoryDao().insertHistory(history)
        }
    }

    companion object {
        const val EXTRA_IMAGE_URI = "EXTRA_IMAGE_URI"
        const val EXTRA_PREDICTION = "EXTRA_PREDICTION"
        const val EXTRA_CONFIDENCE_SCORE = "EXTRA_CONFIDENCE_SCORE"
    }
}
