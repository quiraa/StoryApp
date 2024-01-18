package com.quiraadev.storyapp.ui.detail

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.quiraadev.storyapp.R
import com.quiraadev.storyapp.data.factory.ViewModelFactory
import com.quiraadev.storyapp.databinding.ActivityDetailBinding
import com.quiraadev.storyapp.ui.story.StoryActivity

class DetailActivity : AppCompatActivity(R.layout.activity_detail) {
    private val binding by viewBinding(ActivityDetailBinding::bind)
    private val viewModel: DetailViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelFactory.getInstance(application)
        )[DetailViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val image = binding.ivDetailPhoto
        val name = binding.tvDetailName
        val desc = binding.tvDetailDescription

        val storyId = intent.getStringExtra(StoryActivity.STORY_ID) ?: "vfdjaduh3luda"
        viewModel.getDetailStory(storyId)

        showLoading(false)
        viewModel.detailState.observe(this) { state ->
            when (state) {
                is DetailState.Error -> {
                    Toast.makeText(this@DetailActivity, state.message, Toast.LENGTH_SHORT).show()
                    showLoading(false)
                }

                is DetailState.Loading -> showLoading(true)
                is DetailState.Success -> {
                    showLoading(false)
                    image.load(state.story.photoUrl)
                    name.text = state.story.name
                    desc.text = state.story.description
                }
            }
        }

        binding.btnBack.setOnClickListener { finish() }
    }

    private fun showLoading(isLoadng: Boolean) {
        binding.loadingCircle.isVisible = isLoadng
    }
}