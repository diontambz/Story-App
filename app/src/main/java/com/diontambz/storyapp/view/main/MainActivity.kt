package com.diontambz.storyapp.view.main

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.diontambz.storyapp.databinding.ActivityMainBinding
import com.diontambz.storyapp.util.ViewModelFactory
import com.diontambz.storyapp.view.adapter.LoadingStateAdapter
import com.diontambz.storyapp.view.adapter.StoryAdapter
import com.diontambz.storyapp.view.add_story.AddStoryActivity
import com.diontambz.storyapp.view.auth.LoginActivity
import com.diontambz.storyapp.view.auth.UserViewModel
import com.diontambz.storyapp.view.maps.MapsActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var storyAdapter: StoryAdapter
    private lateinit var mainViewModel: MainViewModel
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setupViewModel()
        setupView()
        onClick()
    }

    private fun onClick() {
        val menu = binding.fabMenu
        binding.fabLogOut.setOnClickListener {
            userViewModel.logout()
            startActivity(Intent(this, LoginActivity::class.java))
            finishAffinity()
        }

        binding.fabAddStory.setOnClickListener {
            startActivity(Intent(this, AddStoryActivity::class.java))
            menu.close(false)
        }
        binding.fabSetting.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            menu.close(false)
        }
        binding.fabMaps.setOnClickListener {
            startActivity(Intent(this, MapsActivity::class.java))
            menu.close(false)
        }
    }

    private fun setupView() {
        storyAdapter = StoryAdapter()

        mainViewModel.getUser().observe(this@MainActivity) { user ->
            if (user.isLogin) {
                setStory()
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

        with(binding.rvStory) {
            setHasFixedSize(true)
            adapter = storyAdapter.withLoadStateFooter(footer = LoadingStateAdapter {
                storyAdapter.retry()
            })
        }
    }

    private fun setStory() {
        showLoad(true)
        mainViewModel.getStory().observe(this@MainActivity) {
            storyAdapter.submitData(lifecycle, it)
            showLoad(false)
        }
    }

    private fun setupViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)

        mainViewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
        userViewModel = ViewModelProvider(this, factory)[UserViewModel::class.java]
    }

    private fun showLoad(isLoad: Boolean) {
        binding.progressBar.visibility = if (isLoad) View.VISIBLE else View.GONE
    }
}