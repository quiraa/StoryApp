package com.quiraadev.storyapp.ui.story

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.textview.MaterialTextView
import com.quiraadev.storyapp.R
import com.quiraadev.storyapp.data.factory.ViewModelFactory
import com.quiraadev.storyapp.databinding.ActivityStoryBinding
import com.quiraadev.storyapp.ui.detail.DetailActivity
import com.quiraadev.storyapp.ui.setting.SettingActivity
import com.quiraadev.storyapp.ui.uploadstory.UploadStoryActivity
import androidx.core.util.Pair

class StoryActivity : AppCompatActivity(R.layout.activity_story) {
    private val binding by viewBinding(ActivityStoryBinding::bind)
    private val viewModel: StoryViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelFactory.getInstance(application)
        ).get(StoryViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
        viewModel.getAllStory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbarStory)


        var img: ImageView = findViewById(R.id.img_story)
        var name: MaterialTextView = findViewById(R.id.tv_story_name)
        var description : MaterialTextView = findViewById(R.id.tv_story_description)

        val rvStory = binding.rvStory
        binding.fabUpload.isVisible = false
        showLoading(true)

        viewModel.storyState.observe(this) { state ->
            when (state) {
                is StoryState.Error -> {
                    Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
                    showLoading(false)
                }

                is StoryState.Loading -> {
                    showLoading(true)
                }

                is StoryState.Success -> {
                    val adapter = StoryAdapter(state.stories) { storyId ->
                        Intent(this@StoryActivity, DetailActivity::class.java).also { intent ->
                            intent.putExtra(STORY_ID, storyId)
                            startActivity(intent)
                        }
                    }
                    rvStory.adapter = adapter
                    rvStory.layoutManager = LinearLayoutManager(this)
                    showLoading(false)

                    binding.fabUpload.isVisible = true
                    binding.fabUpload.setOnClickListener {
                        startActivity(
                            Intent(
                                this@StoryActivity,
                                UploadStoryActivity::class.java,

                            )
                        )
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_story, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_setting -> startActivity(
                Intent(
                    this@StoryActivity,
                    SettingActivity::class.java
                )
            )
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loadingCircle.isVisible = isLoading
    }

    companion object {
        const val STORY_ID = "story_id"
    }
}


