package com.dicoding.asclepius.view

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.helper.FileHelper
import com.dicoding.asclepius.helper.ImageClassifierHelper
import org.tensorflow.lite.support.label.Category
import org.tensorflow.lite.task.vision.classifier.Classifications

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var imageClassifierHelper: ImageClassifierHelper

    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            galleryButton.setOnClickListener { startGallery() }
            analyzeButton.setOnClickListener { analyzeImage() }
        }
    }

    private fun startGallery() {
        launcherIntentGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri

            binding.cropConstraint.visibility = android.view.View.VISIBLE

            val bitmapImage = FileHelper.uriToBitmap(currentImageUri!!, this)
            binding.cropImageView.setImageBitmap(bitmapImage)
            val fileName = FileHelper.getFileNameFromUri(currentImageUri!!, this)!!

            binding.cropButton.setOnClickListener {
                val cropped: Bitmap = binding.cropImageView.getCroppedImage()!!
                val saveCroppedUri = FileHelper.saveCroppedImage(this, cropped, fileName)
                currentImageUri = saveCroppedUri

                showImage()
                binding.cropConstraint.visibility = android.view.View.GONE
            }

        }
    }

    private fun showImage() {
        binding.previewImageView.setImageURI(currentImageUri)
    }

    private fun analyzeImage() {
        if (currentImageUri != null) {
            imageClassifierHelper = ImageClassifierHelper(
                context = this,
                classifierListener = object : ImageClassifierHelper.ClassifierListener {
                    override fun onError(error: String) {
                        showToast(error)
                    }

                    override fun onResults(results: List<Classifications>?) {
                        showToast(results?.get(0).toString())
                        val resultString = results?.get(0)?.categories?.get(0)
                        moveToResult(currentImageUri.toString(), resultString)
                    }
                }
            )
            imageClassifierHelper.classifyStaticImage(currentImageUri!!)
        } else {
            showToast("Please select an image first.")
        }
    }

    private fun moveToResult(imageUri: String, results: Category?) {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra(ResultActivity.EXTRA_IMAGE_URI, imageUri)
        intent.putExtra(ResultActivity.EXTRA_PREDICTION, results?.label)
        intent.putExtra(ResultActivity.EXTRA_CONFIDENCE_SCORE, results?.score)
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}