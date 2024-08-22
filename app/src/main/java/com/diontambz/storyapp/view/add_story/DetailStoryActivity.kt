package com.diontambz.storyapp.view.add_story

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.diontambz.storyapp.data.model.Story
import com.diontambz.storyapp.databinding.ActivityDetailStoryBinding
import com.bumptech.glide.Glide

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setupView()
    }

    private fun setupView() {
        val detail = intent.getParcelableExtra<Story>(EXTRA_DETAIL)

        binding.apply {
            tvNameDetail.text = detail?.name
            tvDesc.text = detail?.description
        }
        Glide.with(this).load(detail?.photoUrl).into(binding.imgStoryDetail)
    }

    companion object {
        const val EXTRA_DETAIL = "extra_detail"
    }
}