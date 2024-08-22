package com.diontambz.storyapp.view.auth

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.diontambz.storyapp.R
import com.diontambz.storyapp.data.Result
import com.diontambz.storyapp.databinding.ActivityRegisterBinding
import com.diontambz.storyapp.util.ViewModelFactory

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setupViewModel()
        setupAction()
        setAnimation()
    }

    private fun setupAction() {
        binding.tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        binding.btnSignup.setOnClickListener {
            if (valid()) {
                val name = binding.etName.text.toString()
                val email = binding.etEmail.text.toString()
                val password = binding.etPass.text.toString()
                userViewModel.userRegister(name, email, password).observe(this) {
                    when (it) {
                        is Result.Success -> {
                            showLoad(false)
                            Toast.makeText(this, it.data.message, Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, LoginActivity::class.java))
                            finishAffinity()
                        }

                        is Result.Loading -> showLoad(true)
                        is Result.Error -> {
                            Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
                            showLoad(false)
                        }
                    }
                }
            } else {
                Toast.makeText(
                    this, resources.getString(R.string.check_input), Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun valid() =
        binding.etEmail.error == null && binding.etPass.error == null && binding.etName.error == null && !binding.etEmail.text.isNullOrEmpty() && !binding.etPass.text.isNullOrEmpty() && !binding.etName.text.isNullOrEmpty()

    private fun setAnimation() {
        ObjectAnimator.ofFloat(
            binding.icon, View.TRANSLATION_X, TRANSLATION_DISTANCE, -TRANSLATION_DISTANCE
        ).apply {
            duration = ANIMATION_DURATION
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val logo = ObjectAnimator.ofFloat(binding.icon, View.ALPHA, 1f)
            .setDuration(ALPHA_ANIMATION_DURATION)
        val tvTitle = ObjectAnimator.ofFloat(binding.tvStory, View.ALPHA, 1f)
            .setDuration(ALPHA_ANIMATION_DURATION)
        val tvMessage = ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f)
            .setDuration(ALPHA_ANIMATION_DURATION)
        val tvName = ObjectAnimator.ofFloat(binding.tvName, View.ALPHA, 1f)
            .setDuration(ALPHA_ANIMATION_DURATION)
        val nameEditTextLayout = ObjectAnimator.ofFloat(binding.etName, View.ALPHA, 1f)
            .setDuration(ALPHA_ANIMATION_DURATION)
        val tvEmail = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f)
            .setDuration(ALPHA_ANIMATION_DURATION)
        val emailEditTextLayout = ObjectAnimator.ofFloat(binding.etEmail, View.ALPHA, 1f)
            .setDuration(ALPHA_ANIMATION_DURATION)
        val tvPassword = ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f)
            .setDuration(ALPHA_ANIMATION_DURATION)
        val passwordEditTextLayout = ObjectAnimator.ofFloat(binding.etPass, View.ALPHA, 1f)
            .setDuration(ALPHA_ANIMATION_DURATION)
        val register = ObjectAnimator.ofFloat(binding.btnSignup, View.ALPHA, 1f)
            .setDuration(ALPHA_ANIMATION_DURATION)
        val tvLoginMessage = ObjectAnimator.ofFloat(binding.tvHaveAcc, View.ALPHA, 1f)
            .setDuration(ALPHA_ANIMATION_DURATION)
        val tvLogin = ObjectAnimator.ofFloat(binding.tvLogin, View.ALPHA, 1f)
            .setDuration(ALPHA_ANIMATION_DURATION)

        val together = AnimatorSet().apply {
            playTogether(tvLoginMessage, tvLogin)
        }

        AnimatorSet().apply {
            playSequentially(
                logo,
                tvTitle,
                tvMessage,
                tvName,
                nameEditTextLayout,
                tvEmail,
                emailEditTextLayout,
                tvPassword,
                passwordEditTextLayout,
                register,
                together
            )
            startDelay = START_DELAY
        }.start()
    }

    private fun setupViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        userViewModel = ViewModelProvider(this, factory)[UserViewModel::class.java]
    }

    private fun showLoad(isLoad: Boolean) {
        binding.progressBar.visibility = if (isLoad) View.VISIBLE else View.GONE
    }

    companion object {
        private const val TRANSLATION_DISTANCE = -30f
        private const val ANIMATION_DURATION = 6000L
        private const val ALPHA_ANIMATION_DURATION = 500L
        private const val START_DELAY = 500L
    }
}