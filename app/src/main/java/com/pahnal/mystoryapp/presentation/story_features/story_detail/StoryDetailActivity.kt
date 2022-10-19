package com.pahnal.mystoryapp.presentation.story_features.story_detail

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.pahnal.mystoryapp.R
import com.pahnal.mystoryapp.databinding.ActivityStoryDetailBinding
import com.pahnal.mystoryapp.databinding.MainToolbarBinding
import com.pahnal.mystoryapp.domain.model.Story
import com.pahnal.mystoryapp.utils.convertTimeStampToDisplay
import com.pahnal.mystoryapp.utils.goBack

class StoryDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryDetailBinding
    private var toolbarLayout: MainToolbarBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        toolbarLayout = binding.toolbarLayout
        setContentView(binding.root)
        setupUI()
    }

    private fun setupUI() {
        setupToolbar()
        with(binding) {
            val extras = intent.extras
            val story: Story? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                extras?.getParcelable(EXTRA_STORY, Story::class.java)
            } else {
                extras?.getParcelable(EXTRA_STORY)
            }
            story?.let { storyData ->
                storyName.text = storyData.name
                storyDescription.text = storyData.description
                storyDate.text = storyData.createdAt.convertTimeStampToDisplay()

                Glide.with(this@StoryDetailActivity)
                    .load(storyData.photoUrl)

                    .skipMemoryCache(true)
                    .error(R.drawable.ic_baseline_broken_image_24)
                    .centerCrop()
                    .into(storyImage)
            }
        }
    }

    private fun setupToolbar() {
        toolbarLayout?.root?.title = getString(R.string.story_detail)
        setSupportActionBar(toolbarLayout?.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> goBack()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        toolbarLayout = null
    }

    companion object {
        const val EXTRA_STORY = "extraStory"
    }

}