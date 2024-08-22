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
import com.diontambz.storyapp.data.model.User
import com.diontambz.storyapp.databinding.ActivityLoginBinding
import com.diontambz.storyapp.util.ViewModelFactory
import com.diontambz.storyapp.view.main.MainActivity


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setupViewModel()
        setupAction()
        setAnimation()
    }

    private fun setupAction() {
        binding.btnLogin.setOnClickListener {
            if (valid()) {
                val email = binding.etEmail.text.toString()
                val password = binding.etPass.text.toString()


                userViewModel.userLogin(email, password).observe(this) {
                    when (it) {
                        is Result.Success -> {
                            showLoad(false)
                            val response = it.data
                            saveUserData(
                                User(
                                    response.loginResult?.name.toString(),
                                    response.loginResult?.token.toString(),
                                    true
                                )
                            )
                            startActivity(Intent(this, MainActivity::class.java))
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

        binding.tvSignup.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

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
        val tvEmail = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f)
            .setDuration(ALPHA_ANIMATION_DURATION)
        val emailEditTextLayout = ObjectAnimator.ofFloat(binding.etEmail, View.ALPHA, 1f)
            .setDuration(ALPHA_ANIMATION_DURATION)
        val tvPassword = ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f)
            .setDuration(ALPHA_ANIMATION_DURATION)
        val passwordEditTextLayout = ObjectAnimator.ofFloat(binding.etPass, View.ALPHA, 1f)
            .setDuration(ALPHA_ANIMATION_DURATION)
        val login = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f)
            .setDuration(ALPHA_ANIMATION_DURATION)
        val tvRegisterMessage = ObjectAnimator.ofFloat(binding.tvDontHaveAcc, View.ALPHA, 1f)
            .setDuration(ALPHA_ANIMATION_DURATION)
        val tvRegister = ObjectAnimator.ofFloat(binding.tvSignup, View.ALPHA, 1f)
            .setDuration(ALPHA_ANIMATION_DURATION)

        val together = AnimatorSet().apply {
            playTogether(tvRegisterMessage, tvRegister)
        }

        AnimatorSet().apply {
            playSequentially(
                logo,
                tvTitle,
                tvMessage,
                tvEmail,
                emailEditTextLayout,
                tvPassword,
                passwordEditTextLayout,
                login,
                together
            )
            startDelay = START_DELAY
        }.start()
    }

    private fun saveUserData(user: User) {
        userViewModel.saveUser(user)
    }

    private fun setupViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        userViewModel = ViewModelProvider(this, factory)[UserViewModel::class.java]
    }

    private fun showLoad(isLoad: Boolean) {
        binding.progressBar.visibility = if (isLoad) View.VISIBLE else View.GONE
    }

    private fun valid() =
        binding.etEmail.error == null && binding.etPass.error == null && !binding.etEmail.text.isNullOrEmpty() && !binding.etPass.text.isNullOrEmpty()


    companion object {
        private const val TRANSLATION_DISTANCE = -30f
        private const val ANIMATION_DURATION = 6000L
        private const val ALPHA_ANIMATION_DURATION = 500L
        private const val START_DELAY = 500L
    }
}