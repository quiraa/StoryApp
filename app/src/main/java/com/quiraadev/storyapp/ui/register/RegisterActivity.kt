package com.quiraadev.storyapp.ui.register

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
import com.quiraadev.storyapp.R
import com.quiraadev.storyapp.data.factory.ViewModelFactory
import com.quiraadev.storyapp.databinding.ActivityRegisterBinding
import com.quiraadev.storyapp.ui.login.LoginActivity
import com.quiraadev.storyapp.utils.DialogType


class RegisterActivity : AppCompatActivity(R.layout.activity_register) {
    private val viewModel: RegisterViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelFactory.getInstance(application)
        )[RegisterViewModel::class.java]
    }

    private val binding by viewBinding(ActivityRegisterBinding::bind)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        showLoading(false)
        setupView()
        binding.btnRegister.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()
            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) Toast.makeText(
                this,
                "All fields must be filled",
                Toast.LENGTH_SHORT
            ).show()
            else viewModel.register(name, email, password)
        }

        binding.textButtonLogin.setOnClickListener {
            startActivity(
                Intent(
                    this@RegisterActivity,
                    LoginActivity::class.java
                )
            )
            finish()
        }

        viewModel.registerState.observe(this) { state ->
            when (state) {
                is RegisterState.Error -> {
                    createDialog(DialogType.ERROR, state.message)
                    showLoading(false)
                }

                is RegisterState.Loading -> showLoading(true)
                is RegisterState.Success -> {
                    createDialog(DialogType.SUCCESS, "Successful")
                    showLoading(false)
                }

                null -> {}
            }
        }
        playAnimation()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imgRegister, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.tvRegisterTitle, View.ALPHA, 1f).setDuration(1000)
        val edUsername =
            ObjectAnimator.ofFloat(binding.tilRegisterUsername, View.ALPHA, 1f).setDuration(500)
        val edEmail =
            ObjectAnimator.ofFloat(binding.tilRegisterEmail, View.ALPHA, 1f).setDuration(500)
        val edPassword =
            ObjectAnimator.ofFloat(binding.tilRegisterPassword, View.ALPHA, 1f).setDuration(500)
        val registerBtn =
            ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(500)
        val registerAlternative =
            ObjectAnimator.ofFloat(binding.registerAlternative, View.ALPHA, 1f).setDuration(500)


        AnimatorSet().apply {
            playSequentially(
                title,
                edUsername,
                edEmail,
                edPassword,
                registerBtn,
                registerAlternative
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

    private fun createDialog(dialogType: DialogType, message: String?) {
        val dialog = when (dialogType) {
            DialogType.SUCCESS -> showSuccessDialog(message)
            DialogType.ERROR -> showErrorDialog(message)
        }
        dialog.show()
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

    private fun showSuccessDialog(message: String?): AlertDialog {
        return AlertDialog.Builder(this)
            .setTitle("Success")
            .setMessage(message ?: "Operation Successful")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                finish()
            }
            .create()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressCircular.isVisible = isLoading
    }
}