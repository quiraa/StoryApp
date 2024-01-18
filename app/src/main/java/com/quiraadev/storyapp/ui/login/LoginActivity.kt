package com.quiraadev.storyapp.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import com.chibatching.kotpref.Kotpref
import com.quiraadev.storyapp.R
import com.quiraadev.storyapp.data.factory.ViewModelFactory
import com.quiraadev.storyapp.databinding.ActivityLoginBinding
import com.quiraadev.storyapp.ui.register.RegisterActivity
import com.quiraadev.storyapp.ui.story.StoryActivity

class LoginActivity : AppCompatActivity(R.layout.activity_login) {
    private val viewModel: LoginViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelFactory.getInstance(application)
        )[LoginViewModel::class.java]
    }

    private val binding by viewBinding(ActivityLoginBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Kotpref.init(this)

        showLoading(false)
        setupView()
        playAnimation()
        viewModel.isLoggedIn.observe(this) { isLoggedIn ->
            if (isLoggedIn) {
                startActivity(Intent(this@LoginActivity, StoryActivity::class.java))
                finish()
            }
        }

        binding.textButtonRegister.setOnClickListener {
            startActivity(
                Intent(
                    this@LoginActivity,
                    RegisterActivity::class.java
                )
            )
            finish()
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) Toast.makeText(
                this,
                "All fields must be filled",
                Toast.LENGTH_SHORT
            ).show()
            else viewModel.login(email, password)
        }

        viewModel.loginState.observe(this) { state ->
            when (state) {
                is LoginState.Error -> {
                    showErrorDialog(state.message)
                    showLoading(false)
                }

                is LoginState.Loading -> showLoading(true)
                is LoginState.Success -> {
                    showLoading(false)
                }
            }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imgLogin, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.tvLoginTitle, View.ALPHA, 1f).setDuration(1000)
        val edEmail =
            ObjectAnimator.ofFloat(binding.textInputEmail, View.ALPHA, 1f).setDuration(500)
        val edPassword =
            ObjectAnimator.ofFloat(binding.textInputPassword, View.ALPHA, 1f).setDuration(500)
        val loginBtn = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(500)
        val loginAlternative =
            ObjectAnimator.ofFloat(binding.loginAlternative, View.ALPHA, 1f).setDuration(500)


        AnimatorSet().apply {
            playSequentially(
                title,
                edEmail,
                edPassword,
                loginBtn,
                loginAlternative
            )
            start()
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun showErrorDialog(message: String?): AlertDialog {
        return AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage(message ?: "An Error Occured")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressCircular.isVisible = isLoading
    }

}