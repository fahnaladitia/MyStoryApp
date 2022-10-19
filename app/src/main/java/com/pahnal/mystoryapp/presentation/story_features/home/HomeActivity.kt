package com.pahnal.mystoryapp.presentation.story_features.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pahnal.mystoryapp.R
import com.pahnal.mystoryapp.databinding.ActivityHomeBinding
import com.pahnal.mystoryapp.databinding.ItemStoryBinding
import com.pahnal.mystoryapp.databinding.MainToolbarBinding
import com.pahnal.mystoryapp.domain.model.Story
import com.pahnal.mystoryapp.domain.model.User
import com.pahnal.mystoryapp.presentation.auth_features.login.LoginActivity
import com.pahnal.mystoryapp.presentation.story_features.add_story.AddStoryActivity
import com.pahnal.mystoryapp.presentation.story_features.home.adapter.ListStoryAdapter
import com.pahnal.mystoryapp.presentation.story_features.home.adapter.LoadingStateAdapter
import com.pahnal.mystoryapp.presentation.story_features.maps_story_marker.MapsStoryMarkerActivity
import com.pahnal.mystoryapp.presentation.story_features.story_detail.StoryDetailActivity
import com.pahnal.mystoryapp.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val viewModel by lazy {
        val factory = ViewModelFactory.getInstance()
        ViewModelProvider(this, factory)[HomeViewModel::class.java]
    }
    private var toolbar: MainToolbarBinding? = null
    private lateinit var adapter: ListStoryAdapter
    private val storyList = ArrayList<Story>()
    private lateinit var userLogin: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        toolbar = binding.toolbarLayout
        setContentView(binding.root)
        secureIsLogin()
    }

    private fun secureIsLogin() {
        DataPref.getUser(this).asLiveData().observe(this) { user ->
            if (user.token.isEmpty()) {
                goTo(LoginActivity::class.java, true)
            } else {
                userLogin = user
                setupUI()
            }
        }
    }

    private fun setupUI() {
        setupToolbar()
        with(binding) {
            fabAddStory.setOnClickListener {
                val intent = Intent(this@HomeActivity, AddStoryActivity::class.java)
                launcherToAddStoryActivity.launch(intent)
            }
            initAdapters()
            srlStory.setOnRefreshListener { adapter.refresh() }
        }
    }

    private fun setupToolbar() {
        toolbar?.root?.title = getString(R.string.story)
        setSupportActionBar(toolbar?.root)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> dialogAlertConfirmLogout()
            R.id.maps_story -> this.goToWithDataParcelizeList(
                MapsStoryMarkerActivity::class.java,
                MapsStoryMarkerActivity.EXTRA_STORY_LIST,
                storyList,
            )
        }
        return super.onOptionsItemSelected(item)
    }

    private fun dialogAlertConfirmLogout() {
        MaterialAlertDialogBuilder(
            this,
            R.style.Theme_MyStoryApp_AlertDialogConfirmation,
        ).setTitle(getString(R.string.confirm_logout))
            .setMessage(getString(R.string.confirm_logout_message))
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ -> dialog.dismiss() }
            .setPositiveButton(R.string.yes) { _, _ -> logout() }.show()
    }


    private fun initAdapters() {
        with(binding) {
            rvStory.layoutManager = LinearLayoutManager(this@HomeActivity)
            adapter = ListStoryAdapter()

            rvStory.adapter =
                adapter.withLoadStateFooter(footer = LoadingStateAdapter { adapter.retry() })

            adapter.apply {
                onStoryClickListener = { story, itemBinding ->
                    navigateToStoryDetail(story, itemBinding)
                }
            }
            adapter.addLoadStateListener {
                val isRefreshLoading = it.refresh is LoadState.Loading
                val isRefreshError = it.refresh is LoadState.Error
                val isAppendLoading = it.append is LoadState.Loading
                val isAppendError = it.append is LoadState.Error
                val isPrependLoading = it.prepend is LoadState.Loading
                val isPrependError = it.prepend is LoadState.Error
                srlStory.isRefreshing = isRefreshLoading
                emptyMsg.isVisible =
                    !isRefreshError && !isAppendError && !isPrependError && !isRefreshLoading && !isAppendLoading && !isPrependLoading && adapter.itemCount == 0
                errorMsg.isVisible = isRefreshError
                if (isRefreshError) {
                    toast((it.refresh as LoadState.Error).error.message ?: "Error message")
                }
                storyList.clear()
                storyList.addAll(adapter.snapshot().items)
            }
            viewModel.listStories(userLogin.token).observe(this@HomeActivity) { stories ->
                adapter.submitData(this@HomeActivity.lifecycle, stories)
            }
        }
    }

    private fun navigateToStoryDetail(story: Story, itemBinding: ItemStoryBinding) {
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this,
            Pair(itemBinding.storyName, "story_name"),
            Pair(itemBinding.storyPhoto, "story_image"),
            Pair(itemBinding.storyDate, "story_date"),
            Pair(itemBinding.storyDescription, "story_description"),
        ).toBundle()
        val intent = Intent(this, StoryDetailActivity::class.java)
        intent.putExtra(StoryDetailActivity.EXTRA_STORY, story)
        startActivity(intent, options)
    }


    private val launcherToAddStoryActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { activityResult ->
        if (activityResult.resultCode == RESULT_OK) {
            val data = activityResult.data?.getBooleanExtra(EXTRA_IS_RELOAD, false)
            if (data == true) {
                if (this::adapter.isInitialized) {
                    binding.rvStory.scrollToPosition(0)
                    adapter.refresh()
                }

            }
        }
    }


    private fun logout() {
        this.lifecycleScope.launch(Dispatchers.IO) {
            DataPref.deleteUser(this@HomeActivity)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        toolbar = null
    }

    companion object {
        const val EXTRA_IS_RELOAD = "extraIsReload"
    }

}