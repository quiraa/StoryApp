package com.quiraadev.storyapp.ui.uploadstory

import android.app.Activity
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.github.dhaval2404.imagepicker.ImagePicker

import com.quiraadev.storyapp.R
import com.quiraadev.storyapp.data.factory.ViewModelFactory
import com.quiraadev.storyapp.databinding.ActivityUploadStoryBinding

class UploadStoryActivity : AppCompatActivity(R.layout.activity_upload_story) {
    private val binding by viewBinding(ActivityUploadStoryBinding::bind)
    private val viewModel: UploadStoryViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelFactory.getInstance(application)
        ).get(UploadStoryViewModel::class.java)
    }

    private var uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbarUpload)

        binding.btnUpload.setOnClickListener {
            val description = binding.edDescription.text.toString()

            if (it == null) {
                Toast.makeText(this, "Please select an Image", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(description.isEmpty()) {
                binding.edDescription.error = "Please fill this field"
                return@setOnClickListener
            }

            viewModel.uploadStory(description, null, null)
        }

        binding.fabAddImage.setOnClickListener {
            ImagePicker.with(this)
                .compress(1000)
                .setDismissListener {
                    Toast.makeText(this, "Dismissed", Toast.LENGTH_SHORT).show()
                }
                .galleryMimeTypes(arrayOf("image/*"))
                .createIntent { intent ->
                    startForImageResult.launch(intent)
                }
        }

        binding.toolbarUpload.setNavigationOnClickListener { finish() }

        showLoading(false)
        viewModel.uploadState.observe(this) { state ->
            when(state) {
                is UploadStoryState.Error -> {
                    showLoading(false)
                    Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
                }
                is UploadStoryState.Loading -> showLoading(true)
                is UploadStoryState.Success -> {
                    showLoading(false)
                    finish()
                }
            }
        }
    }

    private val startForImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val data = result.data
            when (result.resultCode) {
                Activity.RESULT_OK -> {
                    uri = data?.data!!
                    val imageBitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        ImageDecoder.decodeBitmap(
                            ImageDecoder.createSource(
                                this.contentResolver,
                                uri!!
                            )
                        )
                    } else {
                        MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                    }

                    binding.imgPreview.load(imageBitmap)
                    viewModel.setImageBitmap(imageBitmap)
                }

                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                }

                else -> { }
            }
        }

    private fun showLoading(isLoading: Boolean) {
        binding.loadingCircle.isVisible = isLoading
    }
}