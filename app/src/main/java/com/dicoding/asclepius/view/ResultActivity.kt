package com.dicoding.asclepius.view

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        }
    }

    companion object {
        private const val TAG = "ResultActivity"

        const val EXTRA_IMAGE_URI = "EXTRA_IMAGE_URI"
        const val EXTRA_PREDICTION = "EXTRA_PREDICTION"
        const val EXTRA_CONFIDENCE_SCORE = "EXTRA_CONFIDENCE_SCORE"
    }
}