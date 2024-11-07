package com.dicoding.asclepius.view

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.asclepius.R
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        predictionDatabase = PredictionDatabase.getInstance(applicationContext)

        val imageUri = intent.getStringExtra("EXTRA_IMAGE_URI")?.let { Uri.parse(it) }
        val predictions = intent.getStringExtra("EXTRA_PREDICTION")
        val confidenceScore = (intent.getFloatExtra("EXTRA_CONFIDENCE_SCORE", 0f) * 100).toInt()


        binding.apply {
            resultImage.setImageURI(imageUri)
            resultText.text = buildString {
                append(predictions)
                append("\n")
                append(confidenceScore)
                append("%")
            }
            backButton.setOnClickListener { finish() }
        }

        savePredictionHistory(imageUri.toString(), predictions ?: "", confidenceScore)
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
        private const val TAG = "ResultActivity"

        const val EXTRA_IMAGE_URI = "EXTRA_IMAGE_URI"
        const val EXTRA_PREDICTION = "EXTRA_PREDICTION"
        const val EXTRA_CONFIDENCE_SCORE = "EXTRA_CONFIDENCE_SCORE"
    }
}