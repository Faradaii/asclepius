package com.dicoding.asclepius.view

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.helper.FileHelper
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.viewModel.MainViewModel
import org.tensorflow.lite.support.label.Category
import org.tensorflow.lite.task.vision.classifier.Classifications

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            galleryButton.setOnClickListener { startGallery() }
            analyzeButton.setOnClickListener { analyzeImage() }
            historyButton.setOnClickListener {
                val intent = Intent(this@MainActivity, HistoryActivity::class.java)
                startActivity(intent)
            }
        }


        viewModel.currentImageUri?.let { showImage(it) }
    }

    private fun startGallery() {
        launcherIntentGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            showCropTool(uri)
        }
    }

    private fun showCropTool(uri: Uri) {
        viewModel.currentImageUri = uri
        binding.cropConstraint.visibility = android.view.View.VISIBLE

        val bitmapImage = FileHelper.uriToBitmap(uri, this)
        binding.cropImageView.setImageBitmap(bitmapImage)
        val fileName = FileHelper.getFileNameFromUri(uri, this)!!

        binding.rotateButton.setOnClickListener {
            binding.cropImageView.rotateImage(90)
            binding.cropImageView.requestLayout()
        }

        binding.cropButton.setOnClickListener {
            val cropped: Bitmap = binding.cropImageView.getCroppedImage()!!
            val saveCroppedUri = FileHelper.saveCroppedImage(this, cropped, fileName)
            viewModel.currentImageUri = saveCroppedUri

            showImage(saveCroppedUri!!)
            showToast("Image cropped!")
            binding.cropConstraint.visibility = android.view.View.GONE
        }
    }

    private fun showImage(uri: Uri) {
        binding.previewImageView.setImageURI(uri)
    }

    private fun analyzeImage() {
        viewModel.currentImageUri?.let { uri ->
            val imageClassifierHelper = ImageClassifierHelper(
                context = this,
                classifierListener = object : ImageClassifierHelper.ClassifierListener {
                    override fun onError(error: String) {
                        showToast(error)
                    }

                    override fun onResults(results: List<Classifications>?) {
                        val resultString = results?.get(0)?.categories?.get(0)
                        moveToResult(uri.toString(), resultString)
                    }
                }
            )
            imageClassifierHelper.classifyStaticImage(uri)
        } ?: showToast("Please select an image first.")
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
